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
              <label for="login-email">邮箱</label>
              <input
                  type="email"
                  id="login-email"
                  placeholder="请输入您的邮箱"
                  v-model="loginForm.email"
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
              <label for="register-name">姓名</label>
              <input
                  type="text"
                  id="register-name"
                  placeholder="请输入您的姓名"
                  v-model="registerForm.name"
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

    <!-- 页脚 -->
    <footer class="auth-footer">
      <div class="container">
        <p>© 2023 志愿平台. 保留所有权利.</p>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { ref } from 'vue';

// 当前活动的选项卡
const currentTab = ref('login');

// 登录表单数据
const loginForm = ref({
  email: '',
  password: '',
  rememberMe: false
});

// 注册表单数据
const registerForm = ref({
  name: '',
  email: '',
  password: '',
  confirmPassword: '',
  terms: false
});

// 登录处理函数
const handleLogin = () => {
  console.log('登录表单数据:', loginForm.value);
  // 这里可以添加实际的登录逻辑
};

// 注册处理函数
const handleRegister = () => {
  console.log('注册表单数据:', registerForm.value);
  // 这里可以添加实际的注册逻辑
};
</script>

<style scoped>
/* 基础样式 */
.auth-page {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #f9fafb;
}



/* 主内容区样式 */
.auth-content {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 60px 0; /* 增加上下间距 */
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 16px;
}

.auth-card {
  background-color: white;
  border-radius: 12px; /* 增大圆角 */
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1); /* 增强阴影效果 */
  max-width: 480px; /* 增大卡片宽度 */
  width: 100%;
  margin: 0 auto;
  overflow: hidden;
  transition: all 0.3s ease; /* 添加过渡效果 */
}

.auth-card:hover {
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.15); /* 悬停时增强阴影 */
}

/* 选项卡样式 */
.auth-tabs {
  display: flex;
  border-bottom: 1px solid #e5e7eb;
}

.auth-tabs button {
  flex: 1;
  padding: 18px; /* 增加选项卡高度 */
  font-size: 18px; /* 增大字体 */
  font-weight: 500;
  background-color: transparent;
  border: none;
  cursor: pointer;
  transition: all 0.2s;
}

.auth-tabs button.active {
  color: #2563eb;
  border-bottom: 3px solid #2563eb; /* 增加选中状态下划线宽度 */
}

/* 表单样式 */
.auth-form {
  padding: 40px; /* 增加表单内边距 */
}

.form-title {
  font-size: 28px; /* 增大标题字体 */
  font-weight: 600;
  color: #333;
  margin-bottom: 10px; /* 增加间距 */
}

.form-subtitle {
  font-size: 16px; /* 增大副标题字体 */
  color: #666;
  margin-bottom: 32px; /* 增加间距 */
}

.form-group {
  margin-bottom: 24px; /* 增加表单组间距 */
}

.form-group label {
  display: block;
  font-size: 16px; /* 增大标签字体 */
  font-weight: 500;
  color: #333;
  margin-bottom: 12px; /* 增加标签与输入框间距 */
}

.form-group input {
  width: 100%;
  padding: 14px 16px; /* 增加输入框内边距 */
  font-size: 16px; /* 增大输入框字体 */
  border: 1px solid #d1d5db;
  border-radius: 6px; /* 增大输入框圆角 */
  transition: border-color 0.2s, box-shadow 0.2s; /* 添加阴影过渡 */
}

.form-group input:focus {
  outline: none;
  border-color: #2563eb;
  box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.15); /* 添加焦点状态阴影 */
}

.remember-me {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 20px; /* 增加间距 */
}

.remember-me input {
  margin-right: 10px; /* 增加复选框与标签间距 */
  width: 18px; /* 增大复选框尺寸 */
  height: 18px; /* 增大复选框尺寸 */
}

.remember-me label {
  font-size: 15px; /* 调整标签字体 */
  color: #666;
}

.forgot-password {
  font-size: 15px; /* 调整链接字体 */
  color: #2563eb;
  text-decoration: none;
  transition: color 0.2s; /* 添加颜色过渡 */
}

.forgot-password:hover {
  color: #1d4ed8; /* 悬停时加深颜色 */
  text-decoration: underline;
}

.terms {
  display: flex;
  align-items: center;
  margin-top: 20px; /* 增加间距 */
}

.terms input {
  margin-right: 10px; /* 增加复选框与标签间距 */
  width: 18px; /* 增大复选框尺寸 */
  height: 18px; /* 增大复选框尺寸 */
}

.terms label {
  font-size: 15px; /* 调整标签字体 */
  color: #666;
}

.terms a {
  color: #2563eb;
  text-decoration: none;
  transition: color 0.2s; /* 添加颜色过渡 */
}

.terms a:hover {
  color: #1d4ed8; /* 悬停时加深颜色 */
  text-decoration: underline;
}

.submit-button {
  width: 100%;
  padding: 16px 16px; /* 增加按钮高度 */
  background-color: #2563eb;
  color: white;
  font-size: 18px; /* 增大按钮字体 */
  font-weight: 500;
  border: none;
  border-radius: 6px; /* 增大按钮圆角 */
  cursor: pointer;
  transition: all 0.2s; /* 添加所有属性过渡 */
  margin-top: 32px; /* 增加按钮上方间距 */
  box-shadow: 0 4px 12px rgba(37, 99, 235, 0.25); /* 添加按钮阴影 */
}

.submit-button:hover {
  background-color: #1d4ed8;
  transform: translateY(-2px); /* 悬停时轻微上移 */
  box-shadow: 0 6px 16px rgba(37, 99, 235, 0.3); /* 悬停时增强阴影 */
}

.submit-button:disabled {
  background-color: #94a3b8;
  cursor: not-allowed;
  transform: translateY(0); /* 禁用状态取消上移效果 */
  box-shadow: none; /* 禁用状态取消阴影 */
}

/* 社交登录样式 */
.social-login {
  margin-top: 40px; /* 增加社交登录区域上方间距 */
}

.divider {
  display: flex;
  align-items: center;
  margin-bottom: 32px; /* 增加分隔线下方间距 */
}

.divider span {
  font-size: 16px; /* 增大分隔线文字字体 */
  color: #666;
  padding: 0 20px; /* 增加分隔线文字左右间距 */
}

.divider::before,
.divider::after {
  content: '';
  flex: 1;
  border-bottom: 1px solid #e5e7eb;
}

.social-buttons {
  display: flex;
  justify-content: center;
  gap: 24px; /* 增加社交按钮间距 */
}

.social-button {
  width: 52px; /* 增大社交按钮尺寸 */
  height: 52px; /* 增大社交按钮尺寸 */
  border-radius: 50%;
  background-color: #f3f4f6;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.2s; /* 添加所有属性过渡 */
  font-size: 20px; /* 增大图标字体 */
}

.social-button:hover {
  background-color: #e5e7eb;
  transform: translateY(-2px); /* 悬停时轻微上移 */
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); /* 悬停时添加阴影 */
}

.social-button.wechat {
  color: #07c160;
}

.social-button.qq {
  color: #12b7f5;
}


/* 页脚样式 */
.auth-footer {
  padding: 32px 0; /* 增加页脚内边距 */
  text-align: center;
  font-size: 15px; /* 调整页脚字体 */
  color: #666;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .auth-content {
    padding: 40px 0; /* 移动设备上减小内容区上下间距 */
  }

  .auth-card {
    max-width: 100%;
    border-radius: 10px; /* 移动设备上减小卡片圆角 */
  }

  .auth-form {
    padding: 32px; /* 移动设备上减小表单内边距 */
  }



  .auth-tabs button {
    padding: 16px; /* 移动设备上调整选项卡内边距 */
    font-size: 16px; /* 移动设备上调整选项卡文字大小 */
  }

  .form-title {
    font-size: 24px; /* 移动设备上调整标题大小 */
  }

  .form-subtitle {
    font-size: 14px; /* 移动设备上调整副标题大小 */
  }

  .form-group label {
    font-size: 14px; /* 移动设备上调整标签文字大小 */
  }

  .form-group input {
    padding: 12px 14px; /* 移动设备上调整输入框内边距 */
    font-size: 14px; /* 移动设备上调整输入框文字大小 */
  }

  .submit-button {
    padding: 14px 16px; /* 移动设备上调整按钮内边距 */
    font-size: 16px; /* 移动设备上调整按钮文字大小 */
  }

  .social-button {
    width: 48px; /* 移动设备上调整社交按钮大小 */
    height: 48px; /* 移动设备上调整社交按钮大小 */
  }
}
</style>
