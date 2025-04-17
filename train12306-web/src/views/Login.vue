<template>
  <div class="login-container">
    <div class="login-card">
      <h1 class="title">12306售票系统</h1>
      <a-form :model="userinfo" class="login-form">
        <a-form-item
            name="mobile"
            :rules="[{ required: true, message: '请输入手机号码!' },
            { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码!' }]"
        >
          <a-input v-model:value="userinfo.mobile" size="large" placeholder="请输入手机号码">
            <template #prefix>
              <phone-outlined />
            </template>
          </a-input>
        </a-form-item>

        <a-form-item
            name="code"
            :rules="[{ required: true, message: '请输入验证码!' }]"
        >
          <div class="verification-code-container">
            <a-input
                v-model:value="userinfo.code"
                size="large"
                placeholder="请输入验证码"
                class="verification-input"
            >
              <template #prefix>
                <safety-outlined />
              </template>
            </a-input>
            <a-button
                :disabled="countdown > 0"
                @click="getVerificationCode"
                class="verification-button"
                size="large"
                type="primary"
            >
              {{ countdown > 0 ? `${countdown}秒后重试` : '获取验证码' }}
            </a-button>
          </div>
        </a-form-item>

        <a-form-item>
          <div class="button-group">
            <a-button type="primary" html-type="submit" size="large" block @click="login">
              登录
            </a-button>
          </div>
        </a-form-item>
      </a-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue';
import { PhoneOutlined, SafetyOutlined } from '@ant-design/icons-vue';
import axios from "axios";
import {message} from "ant-design-vue";
import {useRouter} from "vue-router";
import store from "@/store";

const router = useRouter()
const userinfo = reactive({
  mobile: '',
  code: '',
});
const countdown = ref(0);

// 获取验证码
const getVerificationCode = () => {
  if (!/^1[3-9]\d{9}$/.test(userinfo.mobile)) {
    message.error('请输入正确的手机号码');
    return;
  }
  axios.post("/member/member/sendCode", {
    mobile: userinfo.mobile,
  }).then(() => {
    message.success("验证码已发送");
  }).catch(err => {
    message.error(err);
  })
  countdown.value = 60;
  const timer = setInterval(() => {
    countdown.value--;
    if (countdown.value <= 0) {
      clearInterval(timer);
    }
  }, 1000);
};
// 登录
const login = () => {
  if (!/^1[3-9]\d{9}$/.test(userinfo.mobile)) {
    message.error('请输入正确的手机号码');
    return;
  }
  axios.post("/member/member/login", {
    mobile: userinfo.mobile,
    code: userinfo.code
  }).then(res => {
    const data = res.data
    if (data.code === 200) {
      message.success("登录成功");
      store.commit('setMember', data.data)
      router.push("/welcome")
    } else {
      message.error(data.msg);
    }
  }).catch(err => {
    message.error(err);
  })
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  min-width: 100vw;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
}

.login-card {
  width: 100%;
  max-width: 420px;
  padding: 40px 30px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
}

.title {
  text-align: center;
  color: #1890ff;
  font-size: 28px;
  margin-bottom: 30px;
  font-weight: bold;
}

.verification-code-container {
  display: flex;
  gap: 10px;
}

.verification-input {
  flex: 1;
}

.verification-button {
  white-space: nowrap;
}

.button-group {
  display: flex;
  flex-direction: column;
  gap: 15px;
  margin-top: 10px;
}

@media (max-width: 480px) {
  .login-card {
    padding: 30px 20px;
  }

  .verification-code-container {
    flex-direction: column;
  }

  .verification-button {
    margin-top: 10px;
    width: 100%;
  }
}
</style>