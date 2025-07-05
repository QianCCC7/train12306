<template>
  <div class="wrapper">
    <div class="header">
      <h2>令牌余量管理</h2>
      <div class="button-group">
        <a-button @click="handleRefresh" class="refresh-button">
          <reload-outlined /> 刷新
        </a-button>
        <a-button type="primary" @click="handleAdd" class="add-button">
          <plus-outlined /> 新增令牌余量
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
        title="令牌余量"
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
          <train-select v-model:value="formData.trainCode" width="300px"></train-select>
        </a-form-item>
        <a-form-item name="date" label="日期">
          <a-date-picker v-model:value="formData.date" value-format="YYYY-MM-DD" />
        </a-form-item>
        <a-form-item name="count" label="令牌余量">
          <a-input v-model:value="formData.count" placeholder="请输入令牌余量" />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup>
import {PlusOutlined} from "@ant-design/icons-vue";
import TrainSelect from "@/components/TrainSelect.vue";
import {onMounted, reactive, ref} from "vue";
import axios from "axios";
import {message} from "ant-design-vue";

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
    title: '日期',
    dataIndex: 'date',
    key: 'date',
  },
  {
    title: '令牌余量',
    dataIndex: 'count',
    key: 'count'
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
  date: [
    {required: true, message: '请输入日期', trigger: 'blur'}
  ],
  count: [
    {required: true, message: '请输入令牌余量', trigger: 'blur'}
  ]
};
const pagination = reactive({
  total: 0, // 数据总条数
  current: 1, // 当前页码
  pageSize: 8, // 每页条数
})

const handleAdd = () => {
  formData.value = {}
  visible.value = true;
}

const handleOk = () => {
  formRef.value.validate().then(() => {
    confirmLoading.value = true;
    axios.post('/business/admin/sk-token/saveSkToken', formData.value)
        .then(res => {
          if (res.data.code === 200) {
            message.success("保存成功");
            resetForm();
            formData.value = {}
            visible.value = false;
            listSkTokenPage(pagination.current, pagination.pageSize)
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

const listSkTokenPage = (pageNum, pageSize) => {
  confirmLoading.value = true;
  axios.get('/business/admin/sk-token/listSkTokenPage', {
    params: { pageNum: pageNum, pageSize: pageSize }
  }).then(res => {
    confirmLoading.value = false;
    dataSource.value = res.data.data.rows
    pagination.current = pageNum
    pagination.pageSize = pageSize
    pagination.total = res.data.data.totalRecords
  }).catch(err => {
    confirmLoading.value = false;
    message.error(`加载列表出现错误: ${err.message || err}`);
  })
}

const handleTableChange = (page) => {
  listSkTokenPage(page.current, page.pageSize)
}

const handleRefresh = () => {
  listSkTokenPage(1, pagination.pageSize)
}

const handleEdit = (record) => {
  formData.value = record
  visible.value = true;
}

const handleDelete = (record) => {
  axios.delete(`/business/admin/sk-token/deleteById/${record.id}`).then(res => {
    if (res.data.code === 200) {
      message.success('删除成功');
      listSkTokenPage(pagination.current, pagination.pageSize)
    }
  }).catch(err => {
    message.error(`删除数据出现错误: ${err.message || err}`);
  })
}

onMounted(() => {
  listSkTokenPage(pagination.current, pagination.pageSize)
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