<template>
  <div class="wrapper">
    <div class="header">
      <h2>车站管理</h2>
      <div class="button-group">
        <a-button @click="handleRefresh" class="refresh-button">
          <reload-outlined /> 刷新
        </a-button>
        <a-button type="primary" @click="handleAdd" class="add-button">
          <plus-outlined /> 新增车站
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
        title="车站"
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
        <a-form-item name="name" label="站名">
          <a-input v-model:value="formData.name" placeholder="请输入车站名称" />
        </a-form-item>

        <a-form-item name="namePinyin" label="站名拼音">
          <a-input v-model:value="formData.namePinyin" placeholder="请输入站名拼音" disabled />
        </a-form-item>

        <a-form-item name="namePy" label="站名拼音首字母">
          <a-input v-model:value="formData.namePy" placeholder="请输入站名拼音首字母" disabled />
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

const visible = ref(false);
const confirmLoading = ref(false);
const formRef = ref(null);
const dataSource = ref([]);
const columns = [
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
    title: '站名拼音首字母',
    dataIndex: 'namePy',
    key: 'namePy',
  },
  {
    title: '操作',
    dataIndex: 'operation',
    key: 'operation',
  },
]
const formData = ref({})
const rules = {
  name: [
    {required: true, message: '请输入车站名称', trigger: 'blur'}
  ],
  namePinyin: [
    {required: true, message: '请输入站名拼音', trigger: 'blur'},
  ],
  namePy: [
    {required: true, message: '请输入站名拼音首字母', trigger: 'change'}
  ]
};
const pagination = reactive({
  total: 0, // 数据总条数
  current: 1, // 当前页码
  pageSize: 2, // 每页条数
})
const loading = ref(false)

const handleAdd = () => {
  formData.value = {}
  visible.value = true;
}

const handleOk = () => {
  formRef.value.validate().then(() => {
    confirmLoading.value = true;
    axios.post('/business/admin/station/saveStation', formData.value)
        .then(res => {
          if (res.data.code === 200) {
            message.success("保存成功");
            resetForm();
            formData.value = {}
            visible.value = false;
            listStations(pagination.current, pagination.pageSize) // 刷新列表
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
const listStations = (pageNum, pageSize) => {
  loading.value = true;
  axios.get('/business/admin/station/listStations', {
    params: { pageNum: pageNum, pageSize: pageSize }
  }).then(res => {
    loading.value = false;
    dataSource.value = res.data.data.rows
    pagination.current = pageNum
    pagination.pageSize = pageSize
    pagination.total = res.data.data.totalRecords
  }).catch(err => {
    loading.value = false;
    message.error('加载车站列表错误:', err);
  })
}
// 页码变化
const handleTableChange = (page) => {
  listStations(page.current, page.pageSize)
}
// 刷新
const handleRefresh = () => {
  listStations(1, pagination.pageSize)
}
// 编辑
const handleEdit = (record) => {
  formData.value = {...record};
  visible.value = true;
}
// 删除
const handleDelete = (record) => {
  axios.delete(`/business/admin/station/deleteById/${record.id}`).then(res => {
    if (res.data.code === 200) {
      message.success('删除成功');
      listStations(pagination.current, pagination.pageSize)
    }
  }).catch(err => {
    message.error('删除车站出现错误:', err);
  })
}

onMounted(() => {
  listStations(pagination.current, pagination.pageSize)
})
watch(() => formData.value.name, () => {
  if (formData.value.name) {
    // toneType不需要音调，最后将空格替换为空串
    formData.value.namePinyin = pinyin(formData.value.name, {toneType: "none"}).replaceAll(" ", "")
    formData.value.namePy = pinyin(formData.value.name, {toneType: "none", pattern: "first"}).replaceAll(" ", "")
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