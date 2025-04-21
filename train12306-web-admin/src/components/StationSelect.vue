<template>
  <a-select v-model:value="name" show-search :filter-option="filterTrainNameOption" allow-clear @change="onChange" placeholder="请选择车站" :style="{'width': width}">
    <a-select-option v-for="item in stationList" :key="item.id" :value="item.name" :label="item.name + item.namePinyin + item.namePy">
      {{item.name}} | {{item.namePinyin}} | {{item.namePy}}
    </a-select-option>
  </a-select>
</template>

<script setup>
import {onMounted, ref, watch} from "vue";
import axios from "axios";
import {message} from "ant-design-vue";
// 父组件通过v-model传递，子组件通过modelValue接受
const props = defineProps(['modelValue', 'width'])
// update:modelValue是modelValue自带的监听事件，updateModalValue是自定义事件
const emit = defineEmits(['update:modelValue', 'updateModalValue'])
const name = ref()
const stationList = ref([])
const width = ref(props.width)
if (!width.value) {
  width.value = '100%'
}

// 获取所有的车站
const getAllTrains = () => {
  axios.get(`/business/admin/station/getAllStations`).then(res => {
    if (res.data.code === 200) {
      stationList.value = res.data.data;
    } else {
      message.error(res.data.msg)
    }
  }).catch(err => {
    message.error('获取所有车站出现错误:', err);
  })
}
const filterTrainNameOption = (input, option) => {
  // 通过自定义label属性进行数据过滤
  return option.label.toLowerCase().indexOf(input.toLowerCase()) !== -1
}

onMounted(() => {
  getAllTrains();
})

watch(() => props.modelValue, () => {
  name.value = props.modelValue
}, {immediate: true})

const onChange = (value) => {
  emit('update:modelValue', value)
}

</script>

<style scoped>

</style>