<template>
  <div class="wrapper">
    <div class="header">
      <h2>车次历经车站管理</h2>
      <div class="button-group">
        <train-select v-model="queryParams.trainCode" width="400px"></train-select>
        <a-button @click="handleRefresh" class="refresh-button">
          <reload-outlined /> 刷新
        </a-button>
        <a-button type="primary" @click="handleAdd" class="add-button">
          <plus-outlined /> 新增车次历经车站
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
        title="车次历经车站"
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
        <a-form-item name="trainCode" label="车次编码">
          <train-select v-model:value="formData.trainCode" @updateModalValue="trainCodeChange" width="300px"></train-select>
        </a-form-item>
        <a-form-item name="indexOrder" label="站序">
          <a-input v-model:value="formData.indexOrder" placeholder="请输入站序" />
        </a-form-item>
        <a-form-item name="name" label="站名">
          <station-select v-model="formData.name" width="300px"></station-select>
        </a-form-item>
        <a-form-item name="namePinyin" label="站名拼音">
          <a-input v-model:value="formData.namePinyin" placeholder="请输入站名拼音" disabled />
        </a-form-item>
        <a-form-item name="inTime" label="进站时间">
          <a-time-picker v-model:value="formData.inTime" value-format="HH:mm:ss" placeholder="进站时间"/>
        </a-form-item>
        <a-form-item name="outTime" label="出站时间">
          <a-time-picker v-model:value="formData.outTime" value-format="HH:mm:ss" placeholder="出站时间"/>
        </a-form-item>
        <a-form-item name="stopTime" label="停站时长">
          <a-time-picker v-model:value="formData.stopTime" value-format="HH:mm:ss" disabled placeholder="停站时长"/>
        </a-form-item>
        <a-form-item name="km" label="里程">
          <a-input v-model:value="formData.km" placeholder="请输入里程" />
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
import TrainSelect from "@/components/TrainSelect.vue";
import StationSelect from "@/components/StationSelect.vue";
import dayjs from 'dayjs'

const visible = ref(false);
const confirmLoading = ref(false);
const formRef = ref(null);
const dataSource = ref([]);
const columns = [
  {
    title: '车次编码',
    dataIndex: 'trainCode',
    key: 'trainCode',
  },
  {
    title: '站序',
    dataIndex: 'indexOrder',
    key: 'indexOrder',
  },
  {
    title: '站名',
    dataIndex: 'name',
    key: 'name',
  },
  {
    title: '站名拼音',
    dataIndex: 'namePinyin',
    key: 'namePinyin',
  },
  {
    title: '进站时间',
    dataIndex: 'inTime',
    key: 'inTime',
  },
  {
    title: '出站时间',
    dataIndex: 'outTime',
    key: 'outTime',
  },
  {
    title: '停站时长',
    dataIndex: 'stopTime',
    key: 'stopTime',
  },
  {
    title: '里程',
    dataIndex: 'km',
    key: 'km',
  },
  {
    title: '操作',
    dataIndex: 'operation',
    key: 'operation',
  },
]
const formData = ref({})
const rules = {
  trainCode: [
    {required: true, message: '请输入车次编码', trigger: 'blur'}
  ],
  indexOrder: [
    {required: true, message: '请选择站序', trigger: 'blur'},
  ],
  name: [
    {required: true, message: '请输入站名', trigger: 'change'}
  ],
  namePinyin: [
    {required: true, message: '请输入站名拼音', trigger: 'change'}
  ],
  inTime: [
    {required: true, message: '请输入进站时间', trigger: 'change'}
  ],
  outTime: [
    {required: true, message: '请输入出站时间', trigger: 'change'}
  ],
  stopTime: [
    {required: true, message: '请输入停站时长', trigger: 'change'}
  ],
  km: [
    {required: true, message: '请输入里程', trigger: 'change'}
  ],
};
const pagination = reactive({
  total: 0, // 数据总条数
  current: 1, // 当前页码
  pageSize: 8, // 每页条数
})
const typeMap = window.TRAIN_TYPE_ARRAY
const loading = ref(false)
const queryParams = ref({})

const handleAdd = () => {
  formData.value = {}
  visible.value = true;
}

const handleOk = () => {
  formRef.value.validate().then(() => {
    confirmLoading.value = true;
    axios.post('/business/admin/train-station/saveTrainStation', formData.value)
        .then(res => {
          if (res.data.code === 200) {
            message.success("保存成功");
            resetForm();
            formData.value = {}
            visible.value = false;
            listTrainStations(pagination.current, pagination.pageSize)
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
const listTrainStations = (pageNum, pageSize) => {
  loading.value = true;
  axios.get('/business/admin/train-station/listTrainStations', {
    params: { pageNum: pageNum, pageSize: pageSize, trainCode: queryParams.value.trainCode }
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
  listTrainStations(page.current, page.pageSize)
}
// 刷新
const handleRefresh = () => {
  listTrainStations(1, pagination.pageSize)
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
  axios.delete(`/business/admin/train-station/deleteById/${record.id}`).then(res => {
    if (res.data.code === 200) {
      message.success('删除成功');
      listTrainStations(pagination.current, pagination.pageSize)
    }
  }).catch(err => {
    message.error(`删除数据出现错误: ${err.message || err}`);
  })
}
const trainCodeChange = (value) => {
  console.log('当前选中了车次', value) // value是子组件通过自定义事件传递的数据
}

onMounted(() => {
  listTrainStations(pagination.current, pagination.pageSize)
})
watch(() => formData.value.name, () => {
  if (formData.value.name) {
    formData.value.namePinyin = pinyin(formData.value.name, {toneType: "none"}).replaceAll(" ", "")
  } else {
    formData.value.namePinyin = ''
  }
}, {immediate: true})
watch(() => formData.value.inTime, () => {
  if (formData.value.outTime) {
    let diff = dayjs(formData.value.outTime, 'HH:mm:ss').diff(dayjs(formData.value.inTime, 'HH:mm:ss'), 'seconds')
    formData.value.stopTime = dayjs('00:00:00', 'HH:mm:ss').second(diff).format('HH:mm:ss')
  }
}, {immediate: true})
watch(() => formData.value.outTime, () => {
  if (formData.value.inTime) {
    let diff = dayjs(formData.value.outTime, 'HH:mm:ss').diff(dayjs(formData.value.inTime, 'HH:mm:ss'), 'seconds')
    formData.value.stopTime = dayjs('00:00:00', 'HH:mm:ss').second(diff).format('HH:mm:ss')
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