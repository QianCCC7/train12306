<template>
  <div class="wrapper">
    <div class="header">
      <h2>会员购票信息</h2>
      <div class="button-group">
        <train-select v-model="queryParams.trainCode" width="400px"></train-select>
        <a-date-picker v-model:value="queryParams.date" value-format="YYYY-MM-DD" placeholder="选择日期"/>
        <a-button @click="handleRefresh" class="refresh-button">
          <reload-outlined /> 刷新
        </a-button>
      </div>
    </div>
    <div>
      <a-table :dataSource="dataSource" :columns="columns" :pagination="pagination" @change="handleTableChange" :loading="loading"></a-table>
    </div>
  </div>
</template>

<script setup>
import {ref, reactive, onMounted} from 'vue';
import axios from "axios";
import {message} from "ant-design-vue";
import TrainSelect from "@/components/TrainSelect.vue";
const dataSource = ref([]);
const columns = [
  {
    title: '会员ID',
    dataIndex: 'memberId',
    key: 'memberId',
  },
  {
    title: '乘客姓名',
    dataIndex: 'passengerName',
    key: 'passengerName',
  },
  {
    title: '日期',
    dataIndex: 'trainDate',
    key: 'trainDate',
  },
  {
    title: '车次编号',
    dataIndex: 'trainCode',
    key: 'trainCode',
  },
  {
    title: '车厢',
    dataIndex: 'carriageIndex',
    key: 'carriageIndex',
  },
  {
    title: '排号',
    dataIndex: 'seatRow',
    key: 'seatRow',
  },
  {
    title: '列号',
    dataIndex: 'seatCol',
    key: 'seatCol',
  },
  {
    title: '出发站',
    dataIndex: 'startStation',
    key: 'startStation',
  },
  {
    title: '出发时间',
    dataIndex: 'startTime',
    key: 'startTime',
  },
  {
    title: '到达站',
    dataIndex: 'endStation',
    key: 'endStation',
  },
  {
    title: '到达时间',
    dataIndex: 'entTime',
    key: 'entTime',
  },
  {
    title: '座位类型',
    dataIndex: 'seatType',
    key: 'seatType',
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    key: 'createTime',
  },
]
const pagination = reactive({
  total: 0, // 数据总条数
  current: 1, // 当前页码
  pageSize: 8, // 每页条数
})
const loading = ref(false)
const queryParams = ref({})

// 分页查询
const listMemberTicketPage = (pageNum, pageSize) => {
  loading.value = true;
  axios.get('/member/admin/member-ticket/listMemberTicketPage', {
    params: { pageNum: pageNum, pageSize: pageSize, code: queryParams.value.trainCode, date: queryParams.value.date}
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
  listMemberTicketPage(page.current, page.pageSize)
}
// 刷新
const handleRefresh = () => {
  listMemberTicketPage(1, pagination.pageSize)
}

onMounted(() => {
  listMemberTicketPage(pagination.current, pagination.pageSize)
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