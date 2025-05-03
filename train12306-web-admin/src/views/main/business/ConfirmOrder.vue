
<template>
  <div class="wrapper">
    <div class="header">
      <h2>订单信息</h2>
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
import {ref, reactive, onMounted, h, resolveComponent} from 'vue';
import axios from "axios";
import {message} from "ant-design-vue";
import TrainSelect from "@/components/TrainSelect.vue";
const dataSource = ref([]);
const columns = [
  {
    title: '订单ID',
    dataIndex: 'id',
    key: 'id',
  },
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
    title: '始发站',
    dataIndex: 'start',
    key: 'start',
  },
  {
    title: '终点站',
    dataIndex: 'end',
    key: 'end',
  },
  {
    title: '总票价',
    dataIndex: 'totalPrice',
    key: 'totalPrice',
  },
  {
    title: '订单状态',
    dataIndex: 'status',
    key: 'status',
    customRender: ({ record }) => {
      const statusMap = {
        "初始":    { color: 'default' },
        "处理中":  { color: 'blue' },
        "成功":    { color: 'green' },
        "失败":    { color: 'red' },
        "无票":    { color: 'orange' },
        "取消":    { color: 'volcano' },
      };
      const { color } = statusMap[record.status] || { color: 'gray' };
      const ATag = resolveComponent('a-tag');
      return h(ATag, { color }, () => record.status || '未知');
    },
  },
  {
    title: '生成时间',
    dataIndex: 'createTime',
    key: 'createTime',
  }
]
const pagination = reactive({
  total: 0, // 数据总条数
  current: 1, // 当前页码
  pageSize: 8, // 每页条数
})
const loading = ref(false)
const queryParams = ref({})

// 分页查询
const listOrderPage = (pageNum, pageSize) => {
  loading.value = true;
  axios.get('/business/admin/confirm-order/listOrderPage', {
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
  listOrderPage(page.current, page.pageSize)
}
// 刷新
const handleRefresh = () => {
  listOrderPage(1, pagination.pageSize)
}

onMounted(() => {
  listOrderPage(pagination.current, pagination.pageSize)
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