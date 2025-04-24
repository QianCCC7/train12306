<template>
  <div class="wrapper">
    <div class="header">
      <h2>车次管理</h2>
      <div class="button-group">
        <a-button @click="handleRefresh" class="refresh-button">
          <reload-outlined /> 刷新
        </a-button>
        <a-button type="primary" @click="handleAdd" class="add-button">
          <plus-outlined /> 新增车次
        </a-button>
      </div>
    </div>
    <div>
      <a-table :dataSource="dataSource" :columns="columns" :pagination="pagination" @change="handleTableChange" :loading="loading">
        <template #bodyCell="{ column,  record }">
          <template v-if="column.dataIndex === 'operation'">
            <a-space>
              <a @click="handleEdit(record)">编辑</a>
              <a-popconfirm title="删除后不可恢复，确认删除?" @confirm="handleDelete(record)" ok-text="确认" cancel-text="取消">
                <a style="color: red">删除</a>
              </a-popconfirm>
              <a-popconfirm title="生成座位将删除已有记录，确认生成座位?" @confirm="generateTrainSeats(record)" ok-text="确认" cancel-text="取消">
                <a style="color: green">生成座位</a>
              </a-popconfirm>
            </a-space>
          </template>
        </template>
      </a-table>
    </div>

    <a-modal
        v-model:open="visible"
        title="车次"
        @ok="handleOk"
        @cancel="handleCancel"
        :confirmLoading="confirmLoading"
        ok-text="确认"
        cancel-text="取消"
    >
      <a-form
          :model="formData"
          :rules="rules"
          ref="formRef"
          layout="vertical"
      >
        <a-form-item name="code" label="车次编码">
          <a-input v-model:value="formData.code" placeholder="请输入车次编码" :disabled="!!formData.id"/>
        </a-form-item>
        <a-form-item name="type" label="车次类型">
          <a-select v-model:value="formData.type" placeholder="请选择车次类型">
            <a-select-option v-for="item in typeMap" :key="item.key" :value="item.key">{{item.value}}</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item name="start" label="始发站">
          <station-select v-model="formData.start" width="300px"></station-select>
        </a-form-item>
        <a-form-item name="startPinyin" label="始发站拼音">
          <a-input v-model:value="formData.startPinyin" placeholder="请输入始发站拼音" disabled />
        </a-form-item>
        <a-form-item name="startTime" label="出发时间">
          <a-time-picker v-model:value="formData.startTime" value-format="HH:mm:ss" />
        </a-form-item>
        <a-form-item name="end" label="终点站">
          <station-select v-model="formData.end" width="300px"></station-select>
        </a-form-item>
        <a-form-item name="endPinyin" label="终点站拼音">
          <a-input v-model:value="formData.endPinyin" placeholder="请输入终点站拼音" disabled />
        </a-form-item>
        <a-form-item name="endTime" label="到站时间">
          <a-time-picker v-model:value="formData.endTime" value-format="HH:mm:ss" />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup>
import {ref, reactive, onMounted, watch} from 'vue';
import {PlusOutlined} from '@ant-design/icons-vue';
import axios from "axios";
import {message} from "ant-design-vue";
import {pinyin} from "pinyin-pro";
import StationSelect from "@/components/StationSelect.vue";

const visible = ref(false);
const confirmLoading = ref(false);
const formRef = ref(null);
const dataSource = ref([]);
const columns = [
  {
    title: '车次编码',
    dataIndex: 'code',
    key: 'code',
  },
  {
    title: '车次类型',
    dataIndex: 'type',
    key: 'type',
  },
  {
    title: '始发站',
    dataIndex: 'start',
    key: 'start',
  },
  {
    title: '始发站拼音',
    dataIndex: 'startPinyin',
    key: 'startPinyin',
  },
  {
    title: '出发时间',
    dataIndex: 'startTime',
    key: 'startTime',
  },
  {
    title: '终点站',
    dataIndex: 'end',
    key: 'end',
  },
  {
    title: '终点站拼音',
    dataIndex: 'endPinyin',
    key: 'endPinyin',
  },
  {
    title: '到站时间',
    dataIndex: 'endTime',
    key: 'endTime',
  },
  {
    title: '操作',
    dataIndex: 'operation',
    key: 'operation',
  },
]
const formData = ref({})
const rules = {
  code: [
    {required: true, message: '请输入车次编码', trigger: 'blur'}
  ],
  type: [
    {required: true, message: '请选择车次类型', trigger: 'blur'},
  ],
  start: [
    {required: true, message: '请输入始发站', trigger: 'change'}
  ],
  startPinyin: [
    {required: true, message: '请输入始发站拼音', trigger: 'change'}
  ],
  startTime: [
    {required: true, message: '请输入出发时间', trigger: 'change'}
  ],
  end: [
    {required: true, message: '请输入终点站', trigger: 'change'}
  ],
  endPinyin: [
    {required: true, message: '请输入终点站拼音', trigger: 'change'}
  ],
  endTime: [
    {required: true, message: '请输入到站时间', trigger: 'change'}
  ],
};
const pagination = reactive({
  total: 0, // 数据总条数
  current: 1, // 当前页码
  pageSize: 2, // 每页条数
})
const typeMap = window.TRAIN_TYPE_ARRAY
const loading = ref(false)

const handleAdd = () => {
  formData.value = {}
  visible.value = true;
}

const handleOk = () => {
  formRef.value.validate().then(() => {
    confirmLoading.value = true;
    axios.post('/business/admin/train/saveTrain', formData.value)
        .then(res => {
          if (res.data.code === 200) {
            message.success("保存成功");
            resetForm();
            formData.value = {}
            visible.value = false;
            listTrains(pagination.current, pagination.pageSize)
          } else {
            message.error(res.data.msg);
          }
        })
        .catch(err => {
          message.error(`保存失败:${err.message || err}`);
        })
    confirmLoading.value = false;
  }).catch(error => {
    confirmLoading.value = false;
    message.error('验证失败:', error);
  });
};

const handleCancel = () => {
  resetForm()
};

const resetForm = () => {
  formRef.value?.resetFields();
};
// 分页查询
const listTrains = (pageNum, pageSize) => {
  loading.value = true;
  axios.get('/business/admin/train/listTrains', {
    params: { pageNum: pageNum, pageSize: pageSize }
  }).then(res => {
    loading.value = false;
    dataSource.value = res.data.data.rows
    pagination.current = pageNum
    pagination.pageSize = pageSize
    pagination.total = res.data.data.totalRecords
  }).catch(err => {
    loading.value = false;
    message.error(`加载列表出现错误: ${err.message || err}`);
  })
}
// 页码变化
const handleTableChange = (page) => {
  listTrains(page.current, page.pageSize)
}
// 刷新
const handleRefresh = () => {
  listTrains(1, pagination.pageSize)
}
// 编辑
const handleEdit = (record) => {
  let type = null;
  for (const e of typeMap) {
    if (e.value === record.type) {
      type = e.key
      break
    }
  }
  formData.value = {...record, type: type};
  visible.value = true;
}
// 删除
const handleDelete = (record) => {
  axios.delete(`/business/admin/train/deleteById/${record.id}`).then(res => {
    if (res.data.code === 200) {
      message.success('删除成功');
      listTrains(pagination.current, pagination.pageSize)
    }
  }).catch(err => {
    message.error(`删除数据出现错误: ${err.message || err}`);
  })
}
// 生成座位
const generateTrainSeats = (record) => {
  loading.value = true
  axios.get(`/business/admin/train/generateTrainSeats/${record.code}`).then(res => {
    loading.value = false;
    if (res.data.code === 200) {
      message.success('生成成功');
    } else {
      message.success('生成失败：', res.data.msg);
    }
  }).catch(err => {
    message.error(`生成座位出现错误: ${err.message || err}`);
  })
}

onMounted(() => {
  listTrains(pagination.current, pagination.pageSize)
})
watch(() => formData.value.start, () => {
  if (formData.value.start) {
    formData.value.startPinyin = pinyin(formData.value.start, {toneType: "none"}).replaceAll(" ", "")
  }
}, {immediate: true})
watch(() => formData.value.end, () => {
  if (formData.value.end) {
    formData.value.endPinyin = pinyin(formData.value.end, {toneType: "none"}).replaceAll(" ", "")
  } else {
    formData.value.endPinyin = ''
  }
}, {immediate: true})
</script>

<style scoped>
.wrapper {
  padding: 24px;
  background: #fff;
  border-radius: 4px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #f0f0f0;
}

.header h2 {
  margin: 0;
  font-size: 18px;
  font-weight: 500;
  color: rgba(0, 0, 0, 0.85);
}
.button-group {
  display: flex;
  gap: 12px;
  align-items: center;
}
.refresh-button {
  display: flex;
  align-items: center;
}
.add-button {
  display: flex;
  align-items: center;
}
</style>