<template>
  <a-layout-header class="header">
    <div class="logo">
      <router-link to="/welcome" style="color: white">
        12306控台
      </router-link>
    </div>
    <a-menu
        v-model:selectedKeys="selectedKeys"
        theme="dark"
        mode="horizontal"
        :style="{ lineHeight: '64px' }"
    >
      <a-menu-item key="/welcome">
        <router-link to="/welcome">
          <coffee-outlined />&nbsp; 欢迎
        </router-link>
      </a-menu-item>
      <a-menu-item key="/about">
        <router-link to="/about">
          <user-outlined />&nbsp; 关于
        </router-link>
      </a-menu-item>
      <a-menu-item key="/base/station">
        <router-link to="/base/station">
          <CarOutlined />&nbsp; 车站管理
        </router-link>
      </a-menu-item>
      <a-menu-item key="/base/train">
        <router-link to="/base/train">
          <CarOutlined />&nbsp; 车次管理
        </router-link>
      </a-menu-item>
      <a-menu-item key="/base/train-station">
        <router-link to="/base/train-station">
          <CarOutlined />&nbsp; 历经车站管理
        </router-link>
      </a-menu-item>
      <a-menu-item key="/base/train-carriage">
        <router-link to="/base/train-carriage">
          <CarOutlined />&nbsp; 车厢管理
        </router-link>
      </a-menu-item>
      <a-menu-item key="/base/train-seat">
        <router-link to="/base/train-seat">
          <CarOutlined />&nbsp; 车座管理
        </router-link>
      </a-menu-item>
      <a-menu-item key="/batch/batch-job">
        <router-link to="/batch/batch-job">
          <user-outlined />&nbsp; 任务管理
        </router-link>
      </a-menu-item>
    </a-menu>
  </a-layout-header>
</template>

<script setup>
import {ref, watch} from 'vue';
import {useRoute} from 'vue-router'
const route = useRoute()

const selectedKeys = ref([]);
watch(() => route.path, (newValue) => {
  selectedKeys.value = []
  selectedKeys.value.push(newValue)
}, {immediate: true})
</script>

<style scoped>
.logo {
  float: left;
  height: 31px;
  width: 150px;
  color: white;
  font-size: 20px;
}
</style>