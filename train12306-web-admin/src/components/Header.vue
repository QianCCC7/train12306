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
      <a-menu-item key="/station">
        <router-link to="/station">
          <user-outlined />&nbsp; 车站管理
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