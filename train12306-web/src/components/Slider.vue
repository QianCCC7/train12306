<template>
  <a-layout-sider width="200" style="background: #fff">
    <a-menu
        v-model:selectedKeys="selectedKeys"
        mode="inline"
        :style="{ height: '100%', borderRight: 0 }"
    >
      <a-menu-item key="/welcome">
        <router-link to="/welcome">
          <coffee-outlined />&nbsp; 欢迎
        </router-link>
      </a-menu-item>
      <a-menu-item key="/passenger">
        <router-link to="/passenger">
          <user-outlined />&nbsp; 乘客管理
        </router-link>
      </a-menu-item>
      <a-menu-item key="/train-ticket">
        <router-link to="/train-ticket">
          <AuditOutlined />&nbsp; 余票查询
        </router-link>
      </a-menu-item>
      <a-menu-item key="/member-ticket">
        <router-link to="/member-ticket">
          <AuditOutlined />&nbsp; 购票记录
        </router-link>
      </a-menu-item>
    </a-menu>
  </a-layout-sider>
</template>

<script setup>
import {ref, watch} from 'vue';
import { useRoute } from 'vue-router'

const route = useRoute()

const selectedKeys = ref([]);
watch(() => route.path, (newValue) => {
  selectedKeys.value = []
  selectedKeys.value.push(newValue)
}, {immediate: true})
</script>

<style scoped>

</style>