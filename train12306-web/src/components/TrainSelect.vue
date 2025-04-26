<template>
  <a-select v-model="trainCode" show-search :filter-option="filterTrainCodeOption" allow-clear @change="onChange" placeholder="请选择车次" :style="{'width': width}">
    <a-select-option v-for="item in trainList" :key="item.code" :value="item.code" :label="item.code + item.start + item.end">
      {{item.code}} | {{item.start}} ~ {{item.end}}
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
const trainCode = ref()
const trainList = ref([])
const width = ref(props.width)
if (!width.value) {
  width.value = '100%'
}

// 获取所有的车次
const getAllTrains = () => {
  axios.get(`/business/train/getAllTrains`).then(res => {
    if (res.data.code === 200) {
      trainList.value = res.data.data;
    } else {
      message.error(res.data.msg)
    }
  }).catch(err => {
    message.error('获取所有车次出现错误:', err);
  })
}
// 车次过滤
const filterTrainCodeOption = (input, option) => {
  // 通过自定义label属性进行数据过滤
  return option.label.toLowerCase().indexOf(input.toLowerCase()) !== -1
}

onMounted(() => {
  getAllTrains();
})

// 监听父组件改变选择器的值变化
watch(() => props.modelValue, () => {
  trainCode.value = props.modelValue
}, {immediate: true})
// 子组件值改变通知父组件
const onChange = (value) => {
  emit('update:modelValue', value) // 先将最新的trainCode传递
  let train = trainList.value.filter(item => item.code === value)[0] || {}
  emit('updateModalValue', train) // 传递对应trainCode的车次完整信息
}

</script>

<style scoped>

</style>