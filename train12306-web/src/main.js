import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import Antd from 'ant-design-vue';
import axios from "axios";

createApp(App).use(store)
    .use(router)
    .use(Antd)
    .mount('#app')

axios.interceptors.request.use(config => {
    console.log('请求参数：', config.data);
    return config;
}, error => {
    return Promise.reject(error);
});
axios.interceptors.response.use(res => {
    console.log('返回结果：', res.data);
    return res;
}, error => {
    console.log('返回错误：', error);
    return Promise.reject(error);
});
