import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import Antd from 'ant-design-vue';
import axios from "axios";
import * as Icons from "@ant-design/icons-vue";
import '@/assets/js/enums'

const app = createApp(App)
app.config.globalProperties.$icons= Icons
Object.keys(Icons).forEach((key) => {
    app.component(key, Icons[key])
})
app.use(store)
    .use(router)
    .use(Antd)
    .mount('#app')

axios.defaults.baseURL = process.env.VUE_APP_SERVER;
axios.interceptors.request.use(config => {
    console.log('请求参数：', config.data ? config.data : '无');
    return config;
}, error => {
    console.log(123)
    return Promise.reject(error);
});
axios.interceptors.response.use(res => {
    console.log('返回结果：', res.data);
    return res;
}, error => {
    console.log(123)
    console.log(error)
    return Promise.reject(error);
});

console.log("当前环境：", process.env.NODE_ENV);
console.log("服务端地址：", process.env.VUE_APP_SERVER);
