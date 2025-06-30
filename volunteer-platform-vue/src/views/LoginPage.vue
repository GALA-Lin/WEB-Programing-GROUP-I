<template>
  <div class="auth-page">

    <!-- 主内容区 -->
    <main class="auth-content">
      <div class="container">
        <div class="auth-card">
          <!-- 登录/注册切换选项卡 -->
          <div class="auth-tabs">
            <button
                :class="{ 'active': currentTab === 'login' }"
                @click="currentTab = 'login'"
            >
              登录
            </button>
            <button
                :class="{ 'active': currentTab === 'register' }"
                @click="currentTab = 'register'"
            >
              注册
            </button>
          </div>

          <!-- 登录表单 -->
          <div v-if="currentTab === 'login'" class="auth-form">
            <h2 class="form-title">欢迎回来</h2>
            <p class="form-subtitle">请登录您的账户</p>

            <div class="form-group">
              <label for="login-username">用户名</label>
              <input
                  type="text"
                  id="login-username"
                  placeholder="请输入您的用户名"
                  v-model="loginForm.username"
              >
            </div>

            <div class="form-group">
              <label for="login-password">密码</label>
              <input
                  type="password"
                  id="login-password"
                  placeholder="请输入您的密码"
                  v-model="loginForm.password"
              >
            </div>

            <div class="form-group remember-me">
              <input type="checkbox" id="remember-me" v-model="loginForm.rememberMe">
              <label for="remember-me">记住我</label>
              <a href="#" class="forgot-password">忘记密码?</a>
            </div>
            <p v-if="errorMessage" style="color: red; text-align: center; margin-bottom: 15px;">{{ errorMessage }}</p>
            <button
                class="submit-button"
                @click="handleLogin"
            >
              登录
            </button>

            <div class="social-login">
              <p class="divider">
                <span>或使用其他方式登录</span>
              </p>
              <div class="social-buttons">
                <button class="social-button wechat">
                  <img src="https://gitee.com/favicon.ico" alt="gitee" height="38px">
                </button>
                <button class="social-button qq">
                  <img src="https://github.githubassets.com/assets/pinned-octocat-093da3e6fa40.svg" alt="github" height="38px">
                </button>
              </div>
            </div>
          </div>

          <!-- 注册表单 -->
          <div v-if="currentTab === 'register'" class="auth-form">
            <h2 class="form-title">创建新账户</h2>
            <p class="form-subtitle">请填写以下信息完成注册</p>
            <div class="form-group">
              <label for="register-name">用户名</label>
              <input
                  type="text"
                  id="register-name"
                  placeholder="请输入您的用户名"
                  v-model="registerForm.username"
              >
            </div>
            <div class="form-group">
              <label for="register-name">真实姓名</label>
              <input
                  type="text"
                  id="register-name"
                  placeholder="请输入您的姓名"
                  v-model="registerForm.realName"
              >
            </div>
            <div class="form-group">
              <label for="register-name">学号</label>
              <input
                  type="text"
                  id="register-name"
                  placeholder="请输入您的学号"
                  v-model="registerForm.studentId"
              >
            </div>
            <div class="form-group">
              <label for="register-email">邮箱</label>
              <input
                  type="email"
                  id="register-email"
                  placeholder="请输入您的邮箱"
                  v-model="registerForm.email"
              >
            </div>

            <div class="form-group">
              <label for="register-password">密码</label>
              <input
                  type="password"
                  id="register-password"
                  placeholder="请设置密码"
                  v-model="registerForm.password"
              >
            </div>

            <div class="form-group">
              <label for="register-confirm-password">确认密码</label>
              <input
                  type="password"
                  id="register-confirm-password"
                  placeholder="请再次输入密码"
                  v-model="registerForm.confirmPassword"
              >
            </div>

            <div class="form-group terms">
              <input type="checkbox" id="terms" v-model="registerForm.terms">
              <label for="terms">我已阅读并同意<a href="#">服务条款</a>和<a href="#">隐私政策</a></label>
            </div>
            <p v-if="errorMessage" style="color: red; text-align: center; margin-bottom: 15px;">{{ errorMessage }}</p>
            <button
                class="submit-button"
                @click="handleRegister"
                :disabled="!registerForm.terms"
            >
              注册
            </button>

            <div class="social-login">
              <p class="divider">
                <span>或使用其他方式注册</span>
              </p>
              <div class="social-buttons">
                <button class="social-button wechat">
                  <img src="https://gitee.com/favicon.ico" alt="gitee" height="38px">
                </button>
                <button class="social-button qq">
                  <img src="https://github.githubassets.com/assets/pinned-octocat-093da3e6fa40.svg" alt="github" height="38px">
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>

  </div>
</template>

<script setup>
import { ref } from 'vue';
import apiClient from '@/api/axios'; // 导入我们创建的axios实例
// import { useRouter } from 'vue-router'; // 如果需要跳转，导入useRouter

// const router = useRouter(); // 获取router实例

const currentTab = ref('login');

// 登录表单数据
const loginForm = ref({
  username: '',
  password: ''
});

// 注册表单数据 (使用我们最终对齐的版本)
const registerForm = ref({
  username: '',
  realName: '',
  studentId: '',
  email: '',
  password: '',
  confirmPassword: '',
  terms: false
});

// 用于显示错误信息的变量
const errorMessage = ref('');

// 注册处理函数
const handleRegister = async () => {
  errorMessage.value = ''; // 清空之前的错误信息

  // --- 新增的前置校验逻辑 ---
  const { username, realName, studentId, email, password, confirmPassword } = registerForm.value;

  if (!username || !realName || !studentId || !email || !password) {
    errorMessage.value = '所有项目均为必填项，请填写完整！';
    return; // 阻止后续代码执行
  }

  if (password !== confirmPassword) {
    errorMessage.value = '两次输入的密码不一致！';
    return;
  }
  // --- 校验结束 ---

  try {
    // 只有在所有前端校验都通过后，才执行API请求
    const response = await apiClient.post('/api/auth/register', {
      realName: realName,
      studentId: studentId,
      username: username,
      email: email,
      password: password
    });

    if (response.data.code === 201) {
      alert('注册成功！请前往登录。');
      currentTab.value = 'login'; // 注册成功后自动切换到登录选项卡
    }
  } catch (error) {
    if (error.response && error.response.data) {
      errorMessage.value = error.response.data.message;
    } else {
      errorMessage.value = '注册失败，请稍后重试。';
    }
    console.error('注册失败:', error);
  }
};

// 登录处理函数
const handleLogin = async () => {
  errorMessage.value = ''; // 清空之前的错误信息
  try {
    const response = await apiClient.post('/api/auth/login', {
      username: loginForm.value.username,
      password: loginForm.value.password
    });

    if (response.data.code === 200) {
      const token = response.data.data.token;
      // 登录成功，将token存储到localStorage
      localStorage.setItem('jwt_token', token);

      alert('登录成功！即将跳转到首页。');
      // 这里可以取消注释，跳转到首页
      // router.push('/');
    }
  } catch (error) {
    if (error.response && error.response.data) {
      // 显示后端返回的错误信息
      errorMessage.value = error.response.data.message;
    } else {
      errorMessage.value = '登录失败，请稍后重试。';
    }
    console.error('登录失败:', error);
  }
};
</script>

<style scoped>
.auth-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f4f6f8; /* 更柔和的背景 */
  padding: 40px; /* 页面整体内边距 */
}

.container {
  max-width: 600px; /* 修改点：增大容器最大宽度 */
  width: 100%;
  margin: 0 auto;
}

.auth-card {
  background-color: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.auth-tabs {
  display: flex;
  border-bottom: 1px solid #e0e0e0;
}

.auth-tabs button {
  flex: 1;
  padding: 16px 20px; /* 调整选项卡内边距 */
  font-size: 16px;
  font-weight: 500;
  color: #555;
  background-color: transparent;
  border: none;
  cursor: pointer;
  transition: color 0.3s ease;
}

.auth-tabs button.active {
  color: #1890ff; /* 更具活力的选中颜色 */
  border-bottom: 2px solid #1890ff;
}

.auth-form {
  padding: 30px; /* 修改点：减小垂直 padding，保持左右 */
}

.form-title {
  font-size: 24px; /* 略微减小标题 */
  font-weight: 600;
  color: #333;
  margin-bottom: 20px;
  text-align: center; /* 居中标题 */
}

.form-subtitle {
  font-size: 14px;
  color: #777;
  margin-bottom: 25px;
  text-align: center; /* 居中副标题 */
}

.form-group {
  margin-bottom: 20px; /* 修改点：减小表单组间距 */
}

.form-group label {
  display: block;
  font-size: 14px;
  font-weight: 500;
  color: #333;
  margin-bottom: 8px;
}

.form-group input {
  width: 100%;
  padding: 12px 15px;
  font-size: 14px;
  border: 1px solid #d9d9d9;
  border-radius: 6px;
  transition: border-color 0.3s ease;
}

.form-group input:focus {
  outline: none;
  border-color: #1890ff;
  box-shadow: 0 0 0 2px rgba(24, 144, 255, 0.2);
}

.remember-me {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 15px;
  font-size: 13px;
  color: #555;
}

.remember-me label {
  margin-left: 5px;
}

.remember-me input {
  width: 16px;
  height: 16px;
  margin-right: 5px;
}

.forgot-password {
  color: #1890ff;
  text-decoration: none;
}

.forgot-password:hover {
  text-decoration: underline;
}

.terms {
  display: flex;
  align-items: center;
  margin-top: 15px;
  font-size: 13px;
  color: #555;
}

.terms label {
  margin-left: 5px;
}

.terms input {
  width: 16px;
  height: 16px;
  margin-right: 5px;
}

.terms a {
  color: #1890ff;
  text-decoration: none;
}

.terms a:hover {
  text-decoration: underline;
}

.submit-button {
  width: 100%;
  padding: 12px 15px; /* 略微减小按钮 padding */
  background-color: #1890ff;
  color: white;
  font-size: 16px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: background-color 0.3s ease;
  margin-top: 25px; /* 调整按钮上边距 */
}

.submit-button:hover {
  background-color: #0c7cd5;
}

.submit-button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.social-login {
  margin-top: 30px;
  text-align: center;
}

.divider {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 20px;
  color: #999;
  font-size: 12px;
}

.divider::before,
.divider::after {
  content: '';
  flex: 1;
  border-top: 1px solid #e0e0e0;
}

.divider span {
  padding: 0 15px;
}

.social-buttons {
  display: flex;
  justify-content: center;
  gap: 15px;
}

.social-button {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-color: #f0f0f0;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  color: #777;
  border: none;
  cursor: pointer;
  transition: background-color 0.3s ease, color 0.3s ease;
}

.social-button:hover {
  background-color: #e0e0e0;
  color: #333;
}

/* 可以根据具体图标库添加社交平台的颜色 */
/* 例如: */
/* .social-button.wechat { color: #1afa29; background-color: #e6f9e8; } */
/* .social-button.wechat:hover { background-color: #d3f4d6; } */
/* .social-button.qq { color: #1296db; background-color: #e3f3fd; } */
/* .social-button.qq:hover { background-color: #cceeff; } */

.auth-footer {
  text-align: center;
  padding: 20px 0;
  font-size: 12px;
  color: #999;
}

@media (max-width: 576px) {
  .container {
    padding: 0 20px;
  }

  .auth-form {
    padding: 20px;
  }

  .auth-tabs button {
    padding: 14px 15px;
    font-size: 14px;
  }

  .form-title {
    font-size: 22px;
    margin-bottom: 15px;
  }

  .form-subtitle {
    font-size: 13px;
    margin-bottom: 20px;
  }

  .form-group {
    margin-bottom: 18px;
  }

  .form-group label {
    font-size: 13px;
    margin-bottom: 6px;
  }

  .form-group input {
    padding: 10px 12px;
    font-size: 13px;
  }

  .submit-button {
    padding: 10px 12px;
    font-size: 14px;
    margin-top: 20px;
  }

  .social-buttons {
    gap: 10px;
  }

  .social-button {
    width: 36px;
    height: 36px;
    font-size: 16px;
  }
}
</style>
