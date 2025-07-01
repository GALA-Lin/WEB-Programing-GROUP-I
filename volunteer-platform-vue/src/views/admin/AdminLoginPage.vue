<template>
  <div class="admin-auth-page">
    <div class="auth-card">
      <div class="auth-form">
        <h2 class="form-title">后台管理登录</h2>
        <p class="form-subtitle">仅限授权管理员访问</p>

        <div class="form-group">
          <label for="login-username">用户名</label>
          <input
              type="text"
              id="login-username"
              placeholder="请输入管理员用户名"
              v-model="loginForm.username"
          />
        </div>

        <div class="form-group">
          <label for="login-password">密码</label>
          <input
              type="password"
              id="login-password"
              placeholder="请输入密码"
              v-model="loginForm.password"
          />
        </div>

        <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>

        <button class="submit-button" @click="handleAdminLogin">
          登 录
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '@/stores/userStore.js';

const router = useRouter();
const userStore = useUserStore();
const errorMessage = ref('');

const loginForm = ref({
  username: '',
  password: '',
});

// 我们需要在 userStore 中创建一个新的 adminLogin 方法
const handleAdminLogin = async () => {
  errorMessage.value = '';
  if (!loginForm.value.username || !loginForm.value.password) {
    errorMessage.value = '用户名和密码不能为空。';
    return;
  }

  try {
    // 调用 store 中的 adminLogin action
    await userStore.adminLogin(loginForm.value); // 注意：这里是 adminLogin
    alert('管理员登录成功！');
    // 登录成功后跳转到管理员主页
    router.push('/admin/dashboard');
  } catch (error) {
    errorMessage.value = error.response?.data?.message || '登录失败，请检查您的凭据或权限。';
    console.error('管理员登录失败:', error);
  }
};
</script>

<style scoped>
.admin-auth-page {
  min-height: 80vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f0f2f5;
}

.auth-card {
  width: 400px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  padding: 40px;
}

.form-title {
  font-size: 24px;
  font-weight: 600;
  text-align: center;
  margin-bottom: 10px;
}

.form-subtitle {
  font-size: 14px;
  color: #888;
  text-align: center;
  margin-bottom: 30px;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
}

.form-group input {
  width: 100%;
  padding: 12px;
  border-radius: 4px;
  border: 1px solid #ddd;
}

.error-message {
  color: red;
  text-align: center;
  margin-bottom: 15px;
}

.submit-button {
  width: 100%;
  padding: 12px;
  background-color: #1890ff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
  margin-top: 10px;
}

.submit-button:hover {
  background-color: #40a9ff;
}
</style>