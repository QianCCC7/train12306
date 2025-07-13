<template>
  <div>
    <p>
      日期：{{stationInfo.date}}，车次：{{stationInfo.trainCode}}，出发站：{{stationInfo.start}}，到达站：{{stationInfo.end}}
    </p>
    <table>
      <tr>
        <td style="width: 25px; background: #FF9900;"></td>
        <td>：已被购买</td>
        <td style="width: 20px;"></td>
        <td style="width: 25px; background: #999999;"></td>
        <td>：未被购买</td>
      </tr>
    </table>
    <br>
    <!-- 遍历车厢对象 -->
    <div v-for="(seatInfo, carriage) in seatSellList" :key="carriage" style="border: 3px solid #99CCFF;
         margin-bottom: 30px; padding: 5px; border-radius: 4px">
      <!-- 车厢名称 -->
      <div style="display:block; width:50px; height:10px; position:relative; top:-15px; text-align: center; background: white;">
        {{carriage}}
      </div>
      <!-- 车厢售卖情况 -->
      <table>
        <tr>
          <td v-for="(sell, index) in Object.values(seatInfo)[0]" :key="index"
              style="text-align: center">
            {{index + 1}}
          </td>
        </tr>
        <!-- 每列座位 -->
        <tr v-for="(sellList, col) in seatInfo" :key="col">
          <!-- 每排座位 -->
          <td v-for="(sell, index) in sellList" :key="index"
              style="text-align: center; border: 2px solid white; background: grey; padding: 0 4px; color: white;"
              :style="{background: (sell > 0 ? '#FF9900' : '#999999')}">{{col}}</td>
        </tr>
      </table>
    </div>
  </div>
</template>

<script setup>
import {ref, watch} from "vue";
import axios from "axios";
import {message} from "ant-design-vue";

const props = defineProps(['selectedStation'])
const stationInfo = ref({})
const seatSellList = ref({})

const getSellSeatList = () => {
  axios.get('/business/daily-train-seat/getSellSeatList', {
    params: { trainCode: stationInfo.value.trainCode, localDate: stationInfo.value.date }
  }).then(res => {
    const data = res.data;
    if (data.code === 200) {
      formatSellSeatList(data.data)
    } else {
      message.error(`加载售卖车座列表失败: ${data.msg}`);
    }
  }).catch(err => {
    message.error(`加载售卖车座列表出现错误: ${err.message || err}`);
  })
}

const formatSellSeatList = (data) => {
  if (data) {
    let seatSellListTemp = {}
    data.forEach(d => {
      // 截取某站到某站的售卖情况(二进制)
      const sell = d.sell.substring(stationInfo.value.startIndex, stationInfo.value.endIndex)
      // 车厢置为空对象
      if (!seatSellListTemp["车箱" + d.carriageIndex]) {
        seatSellListTemp["车箱" + d.carriageIndex] = {};
      }
      // 车厢某一行置为空数组
      if (!seatSellListTemp["车箱" + d.carriageIndex][d.col]) {
        seatSellListTemp["车箱" + d.carriageIndex][d.col] = [];
      }
      // 将改行每列的数据放入数组
      seatSellListTemp["车箱" + d.carriageIndex][d.col].push(parseInt(sell));
    })
    seatSellList.value = seatSellListTemp
    console.log('se', seatSellList.value)
  }
}

watch(() => props.selectedStation, () => {
  stationInfo.value = props.selectedStation
  getSellSeatList()
}, {immediate: true})
</script>

<style scoped>

</style>