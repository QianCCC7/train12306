<template>
  <div class="wrapper">
    <div class="header">
      <h2>任务管理</h2>
      <div class="button-group">
        <a-button @click="handleRefresh" class="refresh-button">
          <reload-outlined /> 刷新
        </a-button>
        <a-button type="primary" @click="handleAdd" class="add-button">
          <plus-outlined /> 新增任务
        </a-button>
      </div>
    </div>
    <div>
      <a-table :dataSource="dataSource" :columns="columns" :loading="loading">
        <template #bodyCell="{ column, record }">
          <template v-if="column.dataIndex === 'operation'">
            <a-space>
              <a-popconfirm
                  title="手动执行会立即执行一次，确定执行？"
                  ok-text="是"
                  cancel-text="否"
                  @confirm="handleRun(record)"
              >
                <a style="color: green">手动执行一次</a>
              </a-popconfirm>
              <a-popconfirm
                  title="确定重启？"
                  ok-text="是"
                  cancel-text="否"
                  @confirm="handleResume(record)"
              >
                <a style="color: hotpink" v-show="record.state === 'PAUSED' || record.state === 'ERROR'">重启</a>
              </a-popconfirm>
              <a-popconfirm
                  title="确定暂停？"
                  ok-text="是"
                  cancel-text="否"
                  @confirm="handlePause(record)"
              >
                <a-button v-show="record.state === 'NORMAL' || record.state === 'BLOCKED'" type="primary" size="small">
                  暂停
                </a-button>
              </a-popconfirm>
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
        title="job任务"
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
        <a-form-item name="name" label="类名">
          <a-input v-model:value="formData.name"></a-input>
        </a-form-item>
        <a-form-item name="group" label="组名">
          <a-input v-model:value="formData.group"></a-input>
        </a-form-item>
        <a-form-item name="description" label="任务描述">
          <a-input v-model:value="formData.description" :disabled="!!formData.state"></a-input>
        </a-form-item>
        <a-form-item name="cronExpression" label="cron表达式">
          <a-input v-model:value="formData.cronExpression"></a-input>
          <a-alert message="每5秒执行一次：0/5 * * * * ?" type="success" />
          <a-alert message="每5分钟执行一次：* 0/5 * * * ?" type="success" />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup>
import {ref, onMounted} from 'vue';
import {PlusOutlined} from '@ant-design/icons-vue';
import axios from "axios";
import {message} from "ant-design-vue";

const visible = ref(false);
const confirmLoading = ref(false);
const formRef = ref(null);
const dataSource = ref([]);
const columns = [
  {
    title: '任务名',
    dataIndex: 'name',
    key: 'name',
  },
  {
    title: '组名',
    dataIndex: 'group',
    key: 'group',
  },
  {
    title: '任务描述',
    dataIndex: 'description',
    key: 'description',
  },
  {
    title: 'cron表达式',
    dataIndex: 'cronExpression',
    key: 'cronExpression',
  },
  {
    title: '状态',
    dataIndex: 'state',
    key: 'state',
  },
  {
    title: '上次执行时间',
    dataIndex: 'preFireTime',
    key: 'preFireTime',
  },
  {
    title: '下次执行时间',
    dataIndex: 'nextFireTime',
    key: 'nextFireTime',
  },
  {
    title: '操作',
    dataIndex: 'operation',
    key: 'operation',
  }
]
const formData = ref({})
const rules = {
  name: [
    {required: true, message: '请输入Java全类名', trigger: 'blur'}
  ],
  group: [
    {required: true, message: '请输入任务所在组', trigger: 'blur'},
  ],
  description: [
    {required: false, message: '请输入任务描述', trigger: 'change'}
  ],
  cronExpression: [
    {required: true, message: '请输入cron表达式', trigger: 'change'}
  ]
};
const loading = ref(false)

const handleAdd = () => {
  formData.value = {}
  visible.value = true;
}

const handleRun = (record) => {
  confirmLoading.value = true;
  axios.post('/batch/admin/job/run', record).then((res) => {
    if (res.data.code === 200) {
      message.success("手动执行成功！");
      listData();
    } else {
      message.error('手动执行任务失败,', res.data.msg);
    }
  }).catch(err => {
    message.error(`手动执行任务出现错误: ${err.message || err}`);
  }).finally(() => {
    confirmLoading.value = false;
  })
}

const handleResume = (record) => {
  confirmLoading.value = true;
  axios.post('/batch/admin/job/reschedule', record).then((res) => {
    if (res.data.code === 200) {
      visible.value = false;
      message.success("重启成功！");
      listData();
    } else {
      message.error('重启失败', res.data.msg);
    }
  }).catch(err => {
    message.error(`重启任务出现错误: ${err.message || err}`);
  }).finally(() => {
    confirmLoading.value = false;
  })
}

const handlePause = (record) => {
  confirmLoading.value = true
  axios.post('/batch/admin/job/pause', {
    name: record.name,
    group: record.group
  }).then((res) => {
    if (res.data.code === 200) {
      message.success( "暂停成功！");
      listData()
    } else {
      message.error('暂停任务失败:', res.data.msg);
    }
  }).catch(err => {
    message.error(`暂停任务出现错误: ${err.message || err}`);
  }).finally(() => {
    confirmLoading.value = false;
  })
}

const handleOk = () => {
  formRef.value.validate().then(() => {
    confirmLoading.value = true;
    const url = formData.value.state ? "reschedule" : "add"
    axios.post('/batch/admin/job/' + url, formData.value)
        .then(res => {
          if (res.data.code === 200) {
            message.success("保存成功");
            resetForm();
            formData.value = {}
            visible.value = false;
            listData()
          } else {
            message.error(res.data.msg);
          }
        })
        .catch(err => {
          message.error(`保存失败:${err.message || err}`);
        })
  }).catch(error => {
    message.error('验证失败:', error);
  }).finally(() => {
    confirmLoading.value = false;
  })
};

const handleCancel = () => {
  resetForm()
};

const resetForm = () => {
  formRef.value?.resetFields();
};
// 查询
const listData = () => {
  loading.value = true;
  axios.get('/batch/admin/job/query').then(res => {
    dataSource.value = res.data.data
  }).catch(err => {
    message.error(`加载列表出现错误: ${err.message || err}`);
  }).finally(() => {
    loading.value = false;
  })
}
// 刷新
const handleRefresh = () => {
  listData()
}
// 编辑
const handleEdit = (record) => {
  formData.value = {...record};
  visible.value = true;
}
// 删除
const handleDelete = (record) => {
  axios.post(`/batch/admin/job/delete`, {
    name: record.name,
    group: record.group
  }).then(res => {
    if (res.data.code === 200) {
      message.success('删除成功');
      listData()
    } else {
      message.error('删除任务失败:', res.data.msg);
    }
  }).catch(err => {
    message.error(`删除任务出现错误: ${err.message || err}`);
  })
}

onMounted(() => {
  listData()
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