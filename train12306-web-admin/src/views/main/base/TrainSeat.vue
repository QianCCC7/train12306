
<template>
  <div class="wrapper">
    <div class="header">
      <h2>车座管理</h2>
      <div class="button-group">
        <train-select v-model="queryParams.trainCode" width="400px"></train-select>
        <a-button @click="handleRefresh" class="refresh-button">
          <reload-outlined /> 刷新
        </a-button>
<!--        <a-button type="primary" @click="handleAdd" class="add-button">-->
<!--          <plus-outlined /> 新增车座-->
<!--        </a-button>-->
      </div>
    </div>
    <div>
      <a-table :dataSource="dataSource" :columns="columns" :pagination="pagination" @change="handleTableChange" :loading="loading">
<!--        <template #bodyCell="{ column,  record }">-->
<!--          <template v-if="column.dataIndex === 'operation'">-->
<!--            <a-space>-->
<!--              <a @click="handleEdit(record)">编辑</a>-->
<!--              <a-popconfirm title="删除后不可恢复，确认删除?" @confirm="handleDelete(record)" ok-text="确认" cancel-text="取消">-->
<!--                <a style="color: red">删除</a>-->
<!--              </a-popconfirm>-->
<!--            </a-space>-->
<!--          </template>-->
<!--        </template>-->
      </a-table>
    </div>

    <a-modal
        v-model:open="visible"
        title="车座"
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
        <a-form-item name="carriageIndex" label="厢号">
          <a-input v-model:value="formData.carriageIndex" placeholder="请输入厢号" />
        </a-form-item>
        <a-form-item name="rowOrder" label="排号">
          <a-input v-model:value="formData.rowOrder" placeholder="请输入排号" />
        </a-form-item>
        <a-form-item name="col" label="列号">
          <a-select v-model:value="formData.col" placeholder="请选择列号">
            <a-select-option v-for="item in colMap" :key="item.key" :value="item.key">{{item.value}}</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item name="seatType" label="车座类型">
          <a-select v-model:value="formData.seatType" placeholder="请选择车座类型">
            <a-select-option v-for="item in typeMap" :key="item.key" :value="item.key">{{item.value}}</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item name="carriageSeatIndex" label="同车厢坐序">
          <a-input v-model:value="formData.carriageSeatIndex" placeholder="请输入同车厢坐序" />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup>
import {ref, reactive, onMounted} from 'vue';
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
    dataIndex: 'carriageIndex',
    key: 'carriageIndex',
  },
  {
    title: '排号',
    dataIndex: 'rowOrder',
    key: 'rowOrder',
  },
  {
    title: '列号',
    dataIndex: 'col',
    key: 'col',
  },
  {
    title: '车座类型',
    dataIndex: 'seatType',
    key: 'seatType',
  },
  {
    title: '同车厢坐序',
    dataIndex: 'carriageSeatIndex',
    key: 'carriageSeatIndex',
  },
  // {
  //   title: '操作',
  //   dataIndex: 'operation',
  //   key: 'operation',
  // },
]
const formData = ref({})
const rules = {
  trainCode: [
    {required: true, message: '请输入车次编码', trigger: 'blur'}
  ],
  carriageIndex: [
    {required: true, message: '请输入厢号', trigger: 'blur'},
  ],
  rowOrder: [
    {required: true, message: '请输入排号', trigger: 'change'}
  ],
  col: [
    {required: true, message: '请选择列号', trigger: 'change'}
  ],
  seatType: [
    {required: true, message: '请选择车座类型', trigger: 'change'}
  ],
  carriageSeatIndex: [
    {required: true, message: '请输入同车厢坐序', trigger: 'change'}
  ],
};
const pagination = reactive({
  total: 0, // 数据总条数
  current: 1, // 当前页码
  pageSize: 2, // 每页条数
})
const colMap = window.TRAIN_SEAT_COL_ARRAY
const typeMap = window.TRAIN_SEAT_TYPE_ARRAY
const loading = ref(false)
const queryParams = ref({})

// const handleAdd = () => {
//   formData.value = {}
//   visible.value = true;
// }

// const handleOk = () => {
//   formRef.value.validate().then(() => {
//     confirmLoading.value = true;
//     axios.post('/business/admin/train-seat/saveTrainSeat', formData.value)
//         .then(res => {
//           if (res.data.code === 200) {
//             message.success("保存成功");
//             resetForm();
//             formData.value = {}
//             visible.value = false;
//             listTrainSeats(pagination.current, pagination.pageSize)
//           } else {
//             message.error(res.data.msg);
//           }
//         })
//         .catch(err => {
//           message.error(err);
//         })
//     confirmLoading.value = false;
//   }).catch(error => {
//     confirmLoading.value = false;
//     message.error('验证失败:', error);
//   });
// };

const handleCancel = () => {
  resetForm()
};

const resetForm = () => {
  formRef.value?.resetFields();
};
// 分页查询
const listTrainSeats = (pageNum, pageSize) => {
  loading.value = true;
  axios.get('/business/admin/train-seat/listTrainSeats', {
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
  listTrainSeats(page.current, page.pageSize)
}
// 刷新
const handleRefresh = () => {
  listTrainSeats(1, pagination.pageSize)
}
// 编辑
// const handleEdit = (record) => {
//   let seatType = null;
//   let colType = null;
//   for (const e of typeMap) {
//     if (e.value === record.seatType) {
//       seatType = e.key
//       break
//     }
//   }
//   for (const e of colMap) {
//     if (e.value === record.col) {
//       colType = e.key
//       break
//     }
//   }
//   formData.value = {...record, seatType: seatType, col: colType};
//   visible.value = true;
// }
// 删除
// const handleDelete = (record) => {
//   axios.delete(`/business/admin/train-seat/deleteById/${record.id}`).then(res => {
//     if (res.data.code === 200) {
//       message.success('删除成功');
//       listTrainSeats(pagination.current, pagination.pageSize)
//     }
//   }).catch(err => {
//     message.error('删除座位列号出现错误:', err);
//   })
// }

onMounted(() => {
  listTrainSeats(pagination.current, pagination.pageSize)
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