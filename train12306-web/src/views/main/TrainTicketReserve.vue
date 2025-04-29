<template>
  <div class="page-container">
    <div class="ticket-container">
      <h3 class="section-title">列车信息</h3>
      <div class="ticket-info">
        <div class="train-header">
          <div class="train-code">
            <span>{{ trainTicketReserve.trainCode }}</span>
          </div>
          <div class="train-date">
            <calendar-outlined />
            <span>{{ trainTicketReserve.date }}</span>
          </div>
        </div>

        <div class="journey-info">
          <div class="station-info">
            <span class="station-name">{{ trainTicketReserve.start }}</span>
            <span class="time">{{ trainTicketReserve.startTime }}</span>
          </div>

          <div class="separator">→</div>

          <div class="station-info">
            <span class="station-name">{{ trainTicketReserve.end }}</span>
            <span class="time">{{ trainTicketReserve.endTime }}</span>
          </div>
        </div>
        <div class="seat-info">
          <div v-for="item in seatInfoList" :key="item.type" class="seat-item">
            <span class="seat-type">{{item.value}}</span>
            <span class="seat-price">¥{{item.price}}</span>
            <span class="seat-count">剩余{{item.count}}张票</span>
          </div>
        </div>
      </div>
    </div>

    <a-divider style="margin: 24px 0" />

    <div class="passenger-container">
      <h3 class="section-title">
        <user-outlined class="section-icon" />
        勾选要购票的乘客
        <span class="selected-count" v-if="passengerTickets.length > 0">
          已选择 <span class="highlight">{{ passengerTickets.length }}</span> 位乘客
        </span>
      </h3>

      <div class="passenger-wrapper">
        <div class="passenger-info">
          <a-checkbox-group v-model:value="passengerChecked" class="custom-checkbox-group">
            <a-checkbox
                v-for="option in passengerOptions"
                :key="option.value"
                :value="option.value"
                @change="e => handlePassengerChange(e, option)"
            >
              {{ option.label }}
            </a-checkbox>
          </a-checkbox-group>

        </div>

        <div class="order-tickets" v-if="passengerTickets.length > 0">
          <div class="order-tickets-header">
            <div class="header-cell name-cell">乘客姓名</div>
            <div class="header-cell id-cell">身份证</div>
            <div class="header-cell type-cell">票种</div>
            <div class="header-cell seat-cell">座位类型</div>
          </div>

          <div class="order-tickets-row" v-for="ticket in passengerTickets" :key="ticket.passengerId">
            <div class="row-cell name-cell">{{ticket.passengerName}}</div>
            <div class="row-cell id-cell">{{ticket.passengerIdCard}}</div>
            <div class="row-cell type-cell">
              <a-tag :color="getPassengerTypeColor(ticket.passengerType)">
                {{ticket.passengerType}}票
              </a-tag>
            </div>
            <div class="row-cell seat-cell">
              <a-select
                  v-model:value="ticket.seatType"
                  class="seat-select"
                  size="middle"
              >
                <a-select-option
                    v-for="item in seatInfoList"
                    :key="item.type"
                    :value="item.type"
                    :disabled="item.count <= 0"
                >
                  {{item.value}} (¥{{item.price}})
                </a-select-option>
              </a-select>
            </div>
          </div>
        </div>

        <div class="action-footer" v-if="passengerTickets.length > 0">
          <div class="price-summary" v-if="calculateTotalPrice() > 0">
            总价: <span class="total-price">¥{{ calculateTotalPrice() }}</span>
          </div>
          <a-button
              type="primary"
              size="large"
              @click="handleSubmit"
              :disabled="passengerTickets.length === 0"
              class="submit-button"
          >
            <check-circle-outlined />
            提交订单
          </a-button>
        </div>
      </div>
    </div>

    <a-modal
        v-model:visible="visible"
        title="请核对以下乘客信息"
        style="top: 50px; width: 800px"
        ok-text="确认"
        cancel-text="取消"
    >
      <div class="confirmation-header">
        <div class="train-info-summary">
          <div class="train-code-summary">
            {{ trainTicketReserve.trainCode }}
          </div>
          <div class="train-date-summary">
            {{ trainTicketReserve.date }}
          </div>
          <div class="journey-summary">
            {{ trainTicketReserve.start }} ({{ trainTicketReserve.startTime }})
            <arrow-right-outlined />
            {{ trainTicketReserve.end }} ({{ trainTicketReserve.endTime }})
          </div>
        </div>
      </div>

      <div class="order-tickets confirmation-tickets">
        <div class="order-tickets-header">
          <div class="header-cell name-cell">乘客</div>
          <div class="header-cell id-cell">身份证</div>
          <div class="header-cell type-cell">票种</div>
          <div class="header-cell seat-cell">座位类型</div>
          <div class="header-cell price-cell">票价</div>
        </div>

        <div class="order-tickets-row" v-for="ticket in passengerTickets" :key="ticket.passengerId">
          <div class="row-cell name-cell">{{ticket.passengerName}}</div>
          <div class="row-cell id-cell">{{ticket.passengerIdCard}}</div>
          <div class="row-cell type-cell">
            <a-tag :color="getPassengerTypeColor(ticket.passengerType)">
              {{ticket.passengerType}}
            </a-tag>
          </div>
          <div class="row-cell seat-cell">
            <span v-for="item in trainSeatType" :key="item.type">
              <span v-if="item.type === ticket.seatType">
                {{item.value}}
              </span>
            </span>
          </div>
          <div class="row-cell price-cell">
            <span v-for="item in seatInfoList" :key="item.type">
              <span v-if="item.type === ticket.seatType" class="ticket-price">
                ¥{{item.price}}
              </span>
            </span>
          </div>
        </div>
      </div>

      <div class="confirmation-footer">
        <div class="total-summary">
          总计: <span class="total-price">¥{{ calculateTotalPrice() }}</span>
        </div>
      </div>
    </a-modal>
  </div>
</template>

<script setup>
import {onMounted, ref} from "vue";
import axios from "axios";
import {message} from "ant-design-vue";
import {deepCopy} from "@/utils/copyUtils";

const trainTicketReserve = SessionStorage.get(TRAIN_TICKET_RESERVE) || {}
const trainSeatType = window.TRAIN_SEAT_TYPE_ARRAY || []
const seatInfoList = []; // 车座信息
const passengerList = ref([]) // 所有乘客
const passengerOptions = ref([]) // passengerList的另一种形式展示在复选框
const passengerChecked = ref([]) // 选中的乘客
const passengerTickets = ref([]) // 乘客的购票信息
const visible = ref(false)

// 车座信息处理
for (const e of trainSeatType) {
  if (trainTicketReserve[e.type] !== -1) {
    seatInfoList.push({
      type: e.type,
      value: e.value,
      count: trainTicketReserve[e.type],
      price: trainTicketReserve[e.type + 'Price']
    })
  }
}

// 乘客信息
const getAllPassengers = () => {
  axios.get('/member/passenger/getAllPassengers').then(res => {
    if (res.data.code === 200) {
      passengerList.value = res.data.data
      passengerList.value.forEach(item => {
        passengerOptions.value.push({label: item.name, value: item})
      })
    } else {
      message.error(`加载乘客列表错误: ${res.data.msg}`);
    }
  }).catch(err => {
    message.error(`加载乘客列表错误: ${err.message || err}`);
  })
}

const handleSubmit = () => {
  if (passengerTickets.value.length > 5) {
    message.error('最多只能购买5张车票');
    return;
  }
  const tempSeatInfoList = deepCopy(seatInfoList)
  for (let passengerTicket of passengerTickets.value) {
    for (let ticket of tempSeatInfoList) {
      if (ticket.type === passengerTicket.seatType) {
        ticket.count -= 1;
        if (ticket.count < 0) {
          message.error(`${ticket.value}数量不足`);
          return;
        }
      }
    }
  }

  visible.value = true;
}

const getPassengerTypeColor = (type) => {
  const colors = {
    '成人': 'blue',
    '儿童': 'green',
    '学生': 'purple'
  };
  return colors[type] || 'blue';
};

const calculateTotalPrice = () => {
  let total = 0;
  passengerTickets.value.forEach(ticket => {
    const seatInfo = seatInfoList.find(item => item.type === ticket.seatType);
    if (seatInfo) {
      total += parseFloat(seatInfo.price);
    }
  });
  return total.toFixed(2);
};

// 乘车人复选框变化监听
const handlePassengerChange = (e, option) => {
  const checked = e.target.checked;
  if (checked) {
    const exists = passengerTickets.value.some(item => item.passengerId === option.value.id);
    if (!exists) {
      // 添加
      const original = passengerList.value.find(p => p.id === option.value.id);
      passengerTickets.value.push({
        passengerId: original.id,
        passengerType: window.PASSENGER_TYPE_ARRAY.filter(t => t.value === original.type)[0].value,
        passengerName: original.name,
        passengerIdCard: original.idCard,
        seatType: seatInfoList.length > 0 ? seatInfoList[0].type : 'edz',
      });
    }
  } else {
    passengerTickets.value = passengerTickets.value.filter(t => t.passengerId !== option.value.id);
  }
};


onMounted(() => {
  getAllPassengers()
})
</script>

<style scoped>
.page-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  height: 78vh;
  overflow-y: auto;
}

.section-title {
  margin: 0 0 16px 0;
  font-size: 18px;
  color: #262626;
  display: flex;
  align-items: center;
}

.section-icon {
  margin-right: 8px;
  color: #1890ff;
}

.selected-count {
  margin-left: auto;
  font-size: 14px;
  font-weight: normal;
}

.highlight {
  color: #1890ff;
  font-weight: 600;
}

.ticket-container {
  background-color: #f0f5ff;
  padding: 20px;
  border-radius: 8px;
  width: 100%;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.ticket-info {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.train-header {
  display: flex;
  align-items: center;
  gap: 24px;
}

.train-code, .train-date {
  display: flex;
  align-items: center;
  gap: 8px;
}

.train-code {
  font-weight: 600;
  color: #1890ff;
  font-size: 18px;
}

.journey-info {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  padding: 16px 0;
  border-top: 1px dashed #d9d9d9;
  border-bottom: 1px dashed #d9d9d9;
}

.station-info {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 6px;
  flex: 1;
}

.station-name {
  font-size: 14px;
  color: #595959;
}

.time {
  font-size: 20px;
  font-weight: 600;
  color: #262626;
}

.separator {
  color: #1890ff;
  font-size: 20px;
}

.seat-info {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  margin-top: 8px;
}

.seat-item {
  display: flex;
  align-items: center;
  gap: 12px;
  background-color: white;
  padding: 10px 16px;
  border-radius: 6px;
  font-size: 14px;
  flex: 1;
  min-width: 200px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
}

.seat-type {
  color: #595959;
  font-weight: 500;
}

.seat-price {
  font-weight: 600;
  color: #f5222d;
}

.seat-count {
  color: #52c41a;
}

.passenger-container {
  width: 100%;
}

.passenger-wrapper {
  display: flex;
  flex-direction: column;
  background-color: #f6ffed;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  gap: 20px;
}

.passenger-info {
  display: flex;
  gap: 16px;
  flex-wrap: wrap;
}

.custom-checkbox-group {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
}

.custom-checkbox-group :deep(.ant-checkbox-wrapper) {
  margin: 0;
  padding: 8px 12px;
  background-color: white;
  border-radius: 6px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
  transition: all 0.3s;
}

.custom-checkbox-group :deep(.ant-checkbox-wrapper:hover) {
  background-color: #e6f7ff;
}

.custom-checkbox-group :deep(.ant-checkbox-wrapper-checked) {
  background-color: #e6f7ff;
  border-left: 3px solid #1890ff;
}

.order-tickets {
  margin: 16px 0;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.order-tickets-header {
  display: flex;
  background-color: #1890ff;
  color: white;
  font-size: 14px;
  font-weight: 500;
}

.order-tickets-row {
  display: flex;
  background-color: white;
  border-bottom: 1px solid #f0f0f0;
}

.order-tickets-row:last-child {
  border-bottom: none;
}

.header-cell, .row-cell {
  padding: 12px 16px;
  display: flex;
  align-items: center;
}

.name-cell {
  width: 15%;
}

.id-cell {
  width: 35%;
}

.type-cell {
  width: 15%;
}

.seat-cell {
  width: 25%;
}

.price-cell {
  width: 10%;
}

.seat-select {
  width: 100%;
}

.action-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px dashed #d9d9d9;
}

.price-summary {
  font-size: 16px;
}

.total-price {
  font-size: 20px;
  font-weight: 600;
  color: #f5222d;
}

.submit-button {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 0 24px;
  height: 40px;
}

.confirmation-header {
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 1px solid #f0f0f0;
}

.train-info-summary {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  align-items: center;
}

.train-code-summary {
  font-size: 18px;
  font-weight: 600;
  color: #1890ff;
  display: flex;
  align-items: center;
  gap: 8px;
}

.journey-summary {
  display: flex;
  align-items: center;
  gap: 8px;
}

.confirmation-tickets {
  margin-bottom: 20px;
}

.confirmation-footer {
  display: flex;
  justify-content: flex-end;
  padding-top: 16px;
  border-top: 1px solid #f0f0f0;
}

.total-summary {
  font-size: 16px;
  font-weight: 500;
}

.ticket-price {
  color: #f5222d;
  font-weight: 500;
}

@media (max-width: 768px) {
  .train-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }

  .journey-info {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }

  .separator {
    transform: rotate(90deg);
    margin: 8px 0;
  }

  .seat-item {
    min-width: 100%;
  }

  .order-tickets-header, .order-tickets-row {
    font-size: 12px;
  }

  .header-cell, .row-cell {
    padding: 8px;
  }

  .action-footer {
    flex-direction: column;
    gap: 16px;
    align-items: stretch;
  }

  .submit-button {
    width: 100%;
  }
}

@media (max-width: 480px) {
  .custom-checkbox-group {
    flex-direction: column;
    width: 100%;
  }

  .custom-checkbox-group :deep(.ant-checkbox-wrapper) {
    width: 100%;
  }

  .order-tickets-header, .order-tickets-row {
    flex-direction: column;
  }

  .header-cell, .row-cell {
    width: 100%;
    padding: 8px 12px;
  }

  .header-cell:not(:first-child), .row-cell:not(:first-child) {
    border-top: 1px solid #f0f0f0;
  }
}
</style>