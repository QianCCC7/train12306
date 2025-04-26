<template>
  <div class="ticket-container">
    <h3 style="margin-bottom: 10px">列车信息</h3>
    <div class="ticket-info">
      <div class="train-header">
        <div class="train-code">
          <train-outlined />
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
</template>

<script setup>
const trainTicketReserve = SessionStorage.get(TRAIN_TICKET_RESERVE) || {}
const seatInfoList = [];
for (const e of window.TRAIN_SEAT_TYPE_ARRAY) {
  if (trainTicketReserve[e.type] !== -1) {
    seatInfoList.push({
      type: e.type,
      value: e.value,
      count: trainTicketReserve[e.type],
      price: trainTicketReserve[e.type + 'Price']
    })
  }
}
</script>

<style scoped>
.ticket-container {
  background-color: #f0f5ff;
  padding: 16px;
  border-radius: 8px;
  width: 50%;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.ticket-info {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.train-header {
  display: flex;
  align-items: center;
  gap: 16px;
}

.train-code, .train-date {
  display: flex;
  align-items: center;
  gap: 6px;
}

.train-code {
  font-weight: 600;
  color: #1890ff;
}

.journey-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.station-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.station-name {
  font-size: 14px;
  color: #595959;
  text-align: center;
}

.time {
  font-size: 16px;
  font-weight: 600;
  color: #262626;
}

.separator {
  color: #1890ff;
  font-size: 16px;
}

.seat-info {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  margin-top: 4px;
}

.seat-item {
  display: flex;
  align-items: center;
  gap: 8px;
  background-color: white;
  padding: 6px 10px;
  border-radius: 4px;
  font-size: 14px;
}

.seat-type {
  color: #595959;
}

.seat-price {
  font-weight: 600;
  color: #f5222d;
}

.seat-count {
  color: #52c41a;
}

@media (max-width: 480px) {
  .train-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }

  .journey-info {
    flex-direction: column;
    align-items: flex-start;
  }

  .separator {
    transform: rotate(90deg);
    margin: 4px 0;
  }
  .seat-info {
    flex-direction: column;
    gap: 8px;
  }

  .seat-item {
    width: 100%;
    justify-content: space-between;
  }
}
</style>