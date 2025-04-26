
<template>
  <div class="wrapper">
    <div class="header">
      <h2>每日余票</h2>
      <div class="button-group">
        <train-select v-model="queryParams.trainCode" width="400px"></train-select>
        <a-date-picker v-model:value="queryParams.date" value-format="YYYY-MM-DD" placeholder="选择日期"/>
        <station-select v-model="queryParams.start" width="200px"></station-select>
        <station-select v-model="queryParams.end" width="200px"></station-select>
        <a-button @click="handleRefresh" class="refresh-button">
          <reload-outlined /> 刷新
        </a-button>
<!--        <a-button type="primary" @click="handleAdd" class="add-button">-->
<!--          <plus-outlined /> 新增每日余票-->
<!--        </a-button>-->
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
          <template v-else-if="column.dataIndex === 'station'">
            {{record.start}} ~ {{record.end}}
          </template>
          <template v-else-if="column.dataIndex === 'time'">
            {{record.startTime}} ~ {{record.endTime}}
          </template>
          <template v-else-if="column.dataIndex === 'duration'">
            {{calcDuration(record.startTime, record.endTime)}}
            <div v-if="record.startTime.replaceAll(':', '') >= record.endTime.replaceAll(':', '')">次日到达</div>
            <div v-else>当日到达</div>
          </template>
          <template v-else-if="column.dataIndex === 'ydz'">
            <div v-if="record.ydz !== -1">余票:{{record.ydz}}<br>票价:{{record.ydzPrice}}¥</div>
            <div v-else>--</div>
          </template>
          <template v-else-if="column.dataIndex === 'edz'">
            <div v-if="record.edz !== -1">余票:{{record.edz}}<br>票价:{{record.edzPrice}}¥</div>
            <div v-else>--</div>
          </template>
          <template v-else-if="column.dataIndex === 'rw'">
            <div v-if="record.rw !== -1">余票:{{record.rw}}<br>票价:{{record.rwPrice}}¥</div>
            <div v-else>--</div>
          </template>
          <template v-else-if="column.dataIndex === 'yw'">
            <div v-if="record.yw !== -1">余票:{{record.yw}}<br>票价:{{record.ywPrice}}¥</div>
            <div v-else>--</div>
          </template>
        </template>
      </a-table>
    </div>

    <a-modal
        v-model:open="visible"
        title="每日余票"
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
          <a-date-picker v-model:value="formData.date" value-format="YYYY-MM-DD" placeholder="请输入日期"/>
        </a-form-item>
        <a-form-item name="start" label="始发站">
          <a-input v-model:value="formData.start" placeholder="请输入始发站" />
        </a-form-item>
        <a-form-item name="startPinyin" label="始发站拼音">
          <a-input v-model:value="formData.startPinyin" placeholder="请输入始发站拼音" />
        </a-form-item>
        <a-form-item name="startTime" label="出发时间">
          <a-date-picker v-model:value="formData.startTime" value-format="HH:mm:ss" placeholder="请输入出发时间"></a-date-picker>
        </a-form-item>
        <a-form-item name="startIndex" label="出发站序">
          <a-input v-model:value="formData.startIndex" placeholder="请输入出发站序" />
        </a-form-item>
        <a-form-item name="end" label="终点站">
          <a-input v-model:value="formData.end" placeholder="请输入终点站" />
        </a-form-item>
        <a-form-item name="endPinyin" label="终点站拼音">
          <a-input v-model:value="formData.endPinyin" placeholder="请输入终点站拼音" />
        </a-form-item>
        <a-form-item name="endTime" label="到站时间">
          <a-date-picker v-model:value="formData.endTime" value-format="HH:mm:ss" placeholder="请输入到站时间"></a-date-picker>
        </a-form-item>
        <a-form-item name="endIndex" label="终点站序">
          <a-input v-model:value="formData.endIndex" placeholder="请输入终点站序" />
        </a-form-item>
        <a-form-item name="ydz" label="一等座余票">
          <a-input v-model:value="formData.ydz" placeholder="请输入一等座余票" />
        </a-form-item>
        <a-form-item name="ydzPrice" label="一等座票价">
          <a-input v-model:value="formData.ydzPrice" placeholder="请输入一等座票价" />
        </a-form-item>
        <a-form-item name="edz" label="二等座余票">
          <a-input v-model:value="formData.edz" placeholder="请输入二等座余票" />
        </a-form-item>
        <a-form-item name="edzPrice" label="二等座票价">
          <a-input v-model:value="formData.edzPrice" placeholder="请输入二等座票价" />
        </a-form-item>
        <a-form-item name="rw" label="软卧余票">
          <a-input v-model:value="formData.rw" placeholder="请输入软卧余票" />
        </a-form-item>
        <a-form-item name="rwPrice" label="软卧票价">
          <a-input v-model:value="formData.rwPrice" placeholder="请输入软卧票价" />
        </a-form-item>
        <a-form-item name="yw" label="硬卧余票">
          <a-input v-model:value="formData.yw" placeholder="请输入硬卧余票" />
        </a-form-item>
        <a-form-item name="ywPrice" label="硬卧票价">
          <a-input v-model:value="formData.ywPrice" placeholder="请输入硬卧票价" />
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
import StationSelect from "@/components/StationSelect.vue";
import dayjs from "dayjs";

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
    title: '车站',
    dataIndex: 'station',
    key: 'station',
  },
  {
    title: '时间',
    dataIndex: 'time',
    key: 'time',
  },
  {
    title: '历时',
    dataIndex: 'duration',
    key: 'duration'
  },
  {
    title: '一等座',
    dataIndex: 'ydz',
    key: 'ydz',
  },
  {
    title: '二等座',
    dataIndex: 'edz',
    key: 'edz',
  },
  {
    title: '软卧',
    dataIndex: 'rw',
    key: 'rw',
  },
  {
    title: '硬卧',
    dataIndex: 'yw',
    key: 'yw',
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
  date: [
    {required: true, message: '请输入日期', trigger: 'blur'}
  ],
  start: [
    {required: true, message: '请输入始发站', trigger: 'blur'},
  ],
  startPinyin: [
    {required: true, message: '请输入始发站拼音', trigger: 'change'}
  ],
  startTime: [
    {required: true, message: '请选择出发时间', trigger: 'change'}
  ],
  startIndex: [
    {required: true, message: '请输入出发站序', trigger: 'change'}
  ],
  end: [
    {required: true, message: '请输入终点站', trigger: 'blur'},
  ],
  endPinyin: [
    {required: true, message: '请输入终点站拼音', trigger: 'change'}
  ],
  endTime: [
    {required: true, message: '请选择到站时间', trigger: 'change'}
  ],
  endIndex: [
    {required: true, message: '请输入到站站序', trigger: 'change'}
  ],
  ydz: [
    {required: true, message: '请输入一等座余票', trigger: 'change'}
  ],
  ydzPrice: [
    {required: true, message: '请输入一等座票价', trigger: 'change'}
  ],
  erz: [
    {required: true, message: '请输入二等座余票', trigger: 'change'}
  ],
  erzPrice: [
    {required: true, message: '请输入二等座票价', trigger: 'change'}
  ],
  rw: [
    {required: true, message: '请输入软卧余票', trigger: 'change'}
  ],
  rwPrice: [
    {required: true, message: '请输入软卧票价', trigger: 'change'}
  ],
  yw: [
    {required: true, message: '请输入硬卧余票', trigger: 'change'}
  ],
  ywPrice: [
    {required: true, message: '请输入硬卧票价', trigger: 'change'}
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

const handleOk = () => {
  formRef.value.validate().then(() => {
    confirmLoading.value = true;
    axios.post('/business/daily-train-ticket/saveDailyTrainTicket', formData.value)
        .then(res => {
          if (res.data.code === 200) {
            message.success("保存成功");
            resetForm();
            formData.value = {}
            visible.value = false;
            listDailyTrainTicketPage(pagination.current, pagination.pageSize)
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
const listDailyTrainTicketPage = (pageNum, pageSize) => {
  loading.value = true;
  axios.get('/business/daily-train-ticket/listDailyTrainTicketPage', {
    params: { pageNum: pageNum, pageSize: pageSize, code: queryParams.value.trainCode, date: queryParams.value.date,
    start: queryParams.value.start, end: queryParams.value.end}
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
  listDailyTrainTicketPage(page.current, page.pageSize)
}
// 刷新
const handleRefresh = () => {
  listDailyTrainTicketPage(1, pagination.pageSize)
}
// 编辑
const handleEdit = (record) => {
  let seatType = null;
  let colType = null;
  for (const e of typeMap) {
    if (e.value === record.seatType) {
      seatType = e.key
      break
    }
  }
  for (const e of colMap) {
    if (e.value === record.col) {
      colType = e.key
      break
    }
  }
  formData.value = {...record, seatType: seatType, col: colType};
  visible.value = true;
}
// 删除
const handleDelete = (record) => {
  axios.delete(`/business/daily-train-ticket/deleteById/${record.id}`).then(res => {
    if (res.data.code === 200) {
      message.success('删除成功');
      listDailyTrainTicketPage(pagination.current, pagination.pageSize)
    }
  }).catch(err => {
    message.error('删除数据出现错误:', err);
  })
}

const calcDuration = (startTime, endTime) => {
  let diff = dayjs(endTime, 'HH:mm:ss').diff(dayjs(startTime, 'HH:mm:ss'), 'seconds')
  return dayjs('00:00:00', 'HH:mm:ss').second(diff).format('HH:mm:ss')
}

onMounted(() => {
  listDailyTrainTicketPage(pagination.current, pagination.pageSize)
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