<template>
  <div class="wrapper">
    <div class="header">
      <h2>车厢管理</h2>
      <div class="button-group">
        <train-select v-model="queryParams.trainCode" width="400px"></train-select>
        <a-button @click="handleRefresh" class="refresh-button">
          <reload-outlined /> 刷新
        </a-button>
        <a-button type="primary" @click="handleAdd" class="add-button">
          <plus-outlined /> 新增车厢
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
        title="车厢"
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
          <train-select v-model="formData.trainCode" width="300px"></train-select>
        </a-form-item>
        <a-form-item name="indexOrder" label="厢号">
          <a-input v-model:value="formData.indexOrder" placeholder="请输入厢号" />
        </a-form-item>
        <a-form-item name="seatType" label="车座类型">
          <a-select v-model:value="formData.seatType" placeholder="请选择车座类型">
            <a-select-option v-for="item in typeMap" :key="item.key" :value="item.key">{{item.value}}</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item name="rowCount" label="座位排数">
          <a-input v-model:value="formData.rowCount" placeholder="请输入座位排数" />
        </a-form-item>
        <a-form-item name="colCount" label="座位列数">
          <a-input v-model:value="formData.colCount" placeholder="座位列数" disabled/>
        </a-form-item>
        <a-form-item name="seatCount" label="总座位数">
          <a-input v-model:value="formData.seatCount" placeholder="总座位数" disabled/>
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
import TrainSelect from "@/components/TrainSelect.vue";

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
    title: '厢号',
    dataIndex: 'indexOrder',
    key: 'indexOrder',
  },
  {
    title: '车座类型',
    dataIndex: 'seatType',
    key: 'seatType',
  },
  {
    title: '座位数',
    dataIndex: 'seatCount',
    key: 'seatCount',
  },
  {
    title: '座位排数',
    dataIndex: 'rowCount',
    key: 'rowCount',
  },
  {
    title: '座位列数',
    dataIndex: 'colCount',
    key: 'colCount',
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
  seatType: [
    {required: true, message: '请输入车座类型', trigger: 'change'}
  ],
  seatCount: [
    {required: true, message: '请输入座位数', trigger: 'change'}
  ],
  rowCount: [
    {required: true, message: '请输入座位排数', trigger: 'change'}
  ],
  colCount: [
    {required: true, message: '请输入座位列数', trigger: 'change'}
  ],
};
const pagination = reactive({
  total: 0, // 数据总条数
  current: 1, // 当前页码
  pageSize: 2, // 每页条数
})
const typeMap = window.TRAIN_SEAT_TYPE_ARRAY
const loading = ref(false)
const queryParams = ref({})
const seatColMap = window.TRAIN_SEAT_COL_ARRAY

const handleAdd = () => {
  formData.value = {}
  visible.value = true;
}

const handleOk = () => {
  formRef.value.validate().then(() => {
    confirmLoading.value = true;
    axios.post('/business/admin/train-carriage/saveTrainCarriage', formData.value)
        .then(res => {
          if (res.data.code === 200) {
            message.success("保存成功");
            resetForm();
            formData.value = {}
            visible.value = false;
            listTrainCarriages(pagination.current, pagination.pageSize)
          } else {
            message.error(res.data.msg);
          }
        })
        .catch(err => {
          message.error(err);
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
const listTrainCarriages = (pageNum, pageSize) => {
  loading.value = true;
  axios.get('/business/admin/train-carriage/listTrainCarriages', {
    params: { pageNum: pageNum, pageSize: pageSize, trainCode: queryParams.value.trainCode }
  }).then(res => {
    loading.value = false;
    dataSource.value = res.data.data.rows
    pagination.current = pageNum
    pagination.pageSize = pageSize
    pagination.total = res.data.data.totalRecords
  }).catch(err => {
    loading.value = false;
    message.error('加载车厢列表错误:', err);
  })
}
// 页码变化
const handleTableChange = (page) => {
  listTrainCarriages(page.current, page.pageSize)
}
// 刷新
const handleRefresh = () => {
  listTrainCarriages(1, pagination.pageSize)
}
// 编辑
const handleEdit = (record) => {
  let seatType = null;
  for (const e of typeMap) {
    if (e.value === record.seatType) {
      seatType = e.key
      break
    }
  }
  formData.value = {...record, seatType: seatType};
  visible.value = true;
}
// 删除
const handleDelete = (record) => {
  axios.delete(`/business/admin/train-carriage/deleteById/${record.id}`).then(res => {
    if (res.data.code === 200) {
      message.success('删除成功');
      listTrainCarriages(pagination.current, pagination.pageSize)
    }
  }).catch(err => {
    message.error('删除车厢出现错误:', err);
  })
}

onMounted(() => {
  listTrainCarriages(pagination.current, pagination.pageSize)
})
watch(() => formData.value.seatType, () => {
  if (formData.value.seatType) {
    if (formData.value.rowCount) {
      const count = seatColMap.filter(seatCol => seatCol.type === formData.value.seatType).length
      formData.value.colCount = count
      formData.value.seatCount = formData.value.rowCount * count
    }
  } else {
    formData.value.colCount = 0
    formData.value.seatCount = 0
  }
})
watch(() => formData.value.rowCount, () => {
  if (formData.value.rowCount) {
    if (formData.value.seatType) {
      const count = seatColMap.filter(seatCol => seatCol.type === formData.value.seatType).length
      formData.value.colCount = count
      formData.value.seatCount = formData.value.rowCount * count
    }
  } else {
    formData.value.colCount = 0
    formData.value.seatCount = 0
  }
})
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