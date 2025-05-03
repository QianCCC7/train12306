<template>
  <a-layout-header class="header">
    <div class="logo">
      <router-link to="/welcome" style="color: white">
        12306平台
      </router-link>
    </div>
    <div style="float: right; color: white">
      您好:{{member.mobile}} &nbsp;&nbsp;
      <router-link to="/login">退出登录</router-link>
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
  </a-layout-header>
</template>

<script setup>
import {ref, watch} from 'vue';
import store from "@/store";
import {useRoute} from 'vue-router'
const route = useRoute()

const selectedKeys = ref([]);
const member = store.state.member
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