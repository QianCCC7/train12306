<template>
  <div class="passenger-management">
    <div class="header">
      <h2>乘客管理</h2>
      <a-button type="primary" @click="showModal" class="add-button">
        <plus-outlined /> 新增乘客
      </a-button>
    </div>
    <div>
      <a-table :dataSource="passengerList" :columns="columns" :pagination="pagination"/>
    </div>

    <a-modal
        v-model:visible="visible"
        title="新增乘客"
        @ok="handleOk"
        @cancel="handleCancel"
        :confirmLoading="confirmLoading"
        ok-text="确认"
        cancel-text="取消"
    >
      <a-form
          :model="passengerInfo"
          :rules="rules"
          ref="formRef"
          layout="vertical"
      >
        <a-form-item name="name" label="姓名">
          <a-input v-model:value="passengerInfo.name" placeholder="请输入乘客姓名" />
        </a-form-item>

        <a-form-item name="idCard" label="身份证号">
          <a-input v-model:value="passengerInfo.idCard" placeholder="请输入身份证号码" />
        </a-form-item>

        <a-form-item name="type" label="乘客类型">
          <a-select
              v-model:value="passengerInfo.type"
              placeholder="请选择乘客类型"
          >
            <a-select-option value="1">成人</a-select-option>
            <a-select-option value="2">儿童</a-select-option>
            <a-select-option value="3">学生</a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup>
import {ref, reactive, onMounted} from 'vue';
import {PlusOutlined} from '@ant-design/icons-vue';
import axios from "axios";
import {message} from "ant-design-vue";

const visible = ref(false);
const confirmLoading = ref(false);
const formRef = ref(null);
const passengerList = ref([]);
const columns = [
  {
    title: '姓名',
    dataIndex: 'name',
    key: 'name',
  },
  {
    title: '身份证号',
    dataIndex: 'idCard',
    key: 'idCard',
  },
  {
    title: '乘客类型',
    dataIndex: 'type',
    key: 'type',
  },
]
const passengerInfo = reactive({
  name: '',
  idCard: '',
  type: undefined
});
const rules = {
  name: [
    {required: true, message: '请输入乘客姓名', trigger: 'blur'}
  ],
  idCard: [
    {required: true, message: '请输入身份证号码', trigger: 'blur'},
    // {pattern: /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/, message: '请输入正确的身份证号码', trigger: 'blur'}
  ],
  type: [
    {required: true, message: '请选择乘客类型', trigger: 'change'}
  ]
};
const pagination = reactive({
  total: 0, // 数据总条数
  current: 1, // 当前页码
  pageSize: 2, // 每页条数
})

const showModal = () => {
  visible.value = true;
};

const handleOk = () => {
  formRef.value.validate().then(() => {
    confirmLoading.value = true;
    axios.post('/member/passenger/savePassenger', passengerInfo)
        .then(res => {
          if (res.data.code === 200) {
            message.success("保存成功");
            resetForm();
            visible.value = false;
          } else {
            message.error(res.data.msg);
          }
        })
        .catch(err => {
          console.log('error', err)
          message.error(err.data.msg);
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
// 分页查询乘客列表
const listPassengers = () => {
  axios.get('/member/passenger/listPassengers', {
    params: {
      pageNum: pagination.current,
      pageSize: pagination.pageSize
    }
  }).then(res => {
    passengerList.value = res.data.data.rows
    pagination.total = res.data.data.totalRecords
  }).catch(err => {
    message.error('加载乘客列表错误:', err);
  })
}

onMounted(() => {
  listPassengers()
})
</script>

<style scoped>
.passenger-management {
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

.add-button {
  display: flex;
  align-items: center;
}
</style>