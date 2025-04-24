<template>
  <div class="wrapper">
    <div class="header">
      <h2>每日车次管理</h2>
      <div class="button-group">
        <train-select v-model="queryParams.trainCode" width="400px"></train-select>
        <a-date-picker v-model:value="queryParams.date" value-format="YYYY-MM-DD" />
        <a-button @click="handleRefresh" class="refresh-button">
          <reload-outlined /> 刷新
        </a-button>
        <a-button type="primary" @click="handleAdd" class="add-button">
          <plus-outlined /> 新增每日车次
        </a-button>
        <a-button type="primary" danger @click="handleGenDailyTrain" class="add-button">
          手动生成车次信息
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
            </a-space>
          </template>
        </template>
      </a-table>
    </div>

    <a-modal
        v-model:open="visible"
        title="每日车次"
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
          <train-select v-model:value="formData.code" width="400px" @updateModalValue="trainCodeChange"></train-select>
        </a-form-item>
        <a-form-item name="date" label="日期">
          <a-date-picker v-model:value="formData.date" value-format="YYYY-MM-DD" />
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
    <a-modal v-model:open="genDailyTrainVisible"
             title="生成车次信息"
             @ok="handleGenOk"
             @cancel="handleGenCancel"
             :confirmLoading="genConfirmLoading"
             ok-text="确认"
             cancel-text="取消">
      <a-form :model="genFormData">
        <a-form-item name="date" label="日期">
          <a-date-picker v-model:value="genFormData.date" value-format="YYYY-MM-DD" placeholder="请选择日期"/>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup>
import {ref, onMounted, watch} from 'vue';
import {PlusOutlined} from '@ant-design/icons-vue';
import axios from "axios";
import {message} from "ant-design-vue";
import {pinyin} from "pinyin-pro";
import StationSelect from "@/components/StationSelect.vue";
import TrainSelect from "@/components/TrainSelect.vue";
import {deepCopy} from "@/utils/copyUtils";

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
    title: '日期',
    dataIndex: 'date',
    key: 'date',
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
  date: [
    {required: true, message: '请输入日期', trigger: 'blur'}
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
const pagination = ref({
  total: 0, // 数据总条数
  current: 1, // 当前页码
  pageSize: 2, // 每页条数
  showSizeChanger: true,
  pageSizeOptions: ['2', '5', '10', '20'],
  showQuickJumper: true, // 是否可以快速跳转到指定页
  showTotal: total => `共 ${total} 条`,
})
const typeMap = window.TRAIN_TYPE_ARRAY
const loading = ref(false)
const queryParams = ref({})
const genDailyTrainVisible = ref(false)
const genFormData = ref({})
const genConfirmLoading = ref(false)

const handleAdd = () => {
  formData.value = {}
  visible.value = true;
}

const handleOk = () => {
  formRef.value.validate().then(() => {
    confirmLoading.value = true;
    axios.post('/business/admin/daily-train/saveDailyTrain', formData.value)
        .then(res => {
          if (res.data.code === 200) {
            message.success("保存成功");
            resetForm();
            formData.value = {}
            visible.value = false;
            listDailyTrainPage(pagination.value.current, pagination.value.pageSize)
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
const listDailyTrainPage = (pageNum, pageSize) => {
  loading.value = true;
  axios.get('/business/admin/daily-train/listDailyTrainPage', {
    params: { pageNum: pageNum, pageSize: pageSize, code: queryParams.value.trainCode, date: queryParams.value.date}
  }).then(res => {
    loading.value = false;
    dataSource.value = res.data.data.rows
    pagination.value.current = pageNum
    pagination.value.pageSize = pageSize
    pagination.value.total = res.data.data.totalRecords
  }).catch(err => {
    loading.value = false;
    message.error(`加载列表出现错误: ${err.message || err}`);
  })
}
// 页码变化
const handleTableChange = (page) => {
  pagination.value.pageSize = page.pageSize
  listDailyTrainPage(page.current, page.pageSize)
}
// 刷新
const handleRefresh = () => {
  listDailyTrainPage(1, pagination.value.pageSize)
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
  axios.delete(`/business/admin/daily-train/deleteById/${record.id}`).then(res => {
    if (res.data.code === 200) {
      message.success('删除成功');
      listDailyTrainPage(pagination.value.current, pagination.value.pageSize)
    }
  }).catch(err => {
    message.error(`删除数据出现错误: ${err.message || err}`);
  })
}
// 车次编码改变触发的事件
const trainCodeChange = (train) => {
  let t = deepCopy(train)
  if (t.id) delete t.id // 避免train.id覆盖formData.value.id
  let type = null;
  for (const e of typeMap) {
    if (e.value === train.type) {
      type = e.key
      break
    }
  }
  t.type = type
  formData.value = Object.assign(formData.value, t)
}

const handleGenDailyTrain = () => {
  genDailyTrainVisible.value = true;
}

const handleGenOk = () => {
  genConfirmLoading.value = true;
  let date = genFormData.value.date
  axios.get(`/business/admin/daily-train/generateDailyTrain/${date}`).then(res => {
    if (res.data.code === 200) {
      genDailyTrainVisible.value = false;
      message.success('生成成功');
      listDailyTrainPage(pagination.value.current, pagination.value.pageSize)
    } else {
      message.error(`生成失败: ${res.data.msg}`);
    }
  }).catch(err => {
    message.error(`生成车次信息出现错误: ${err.message || err}`);
  }).finally(() => {
    genConfirmLoading.value = false;
  })
}

const handleGenCancel = () => {
  genDailyTrainVisible.value = false;
}

onMounted(() => {
  listDailyTrainPage(pagination.value.current, pagination.value.pageSize)
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