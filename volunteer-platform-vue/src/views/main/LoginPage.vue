<template>
  <div class="auth-page">
    <main class="auth-content">
      <div class="container">
        <div class="auth-card">
          <div class="auth-tabs">
            <button
                :class="{ active: currentTab === 'login' }"
                @click="currentTab = 'login'"
            >
              登录
            </button>
            <button
                :class="{ active: currentTab === 'register' }"
                @click="currentTab = 'register'"
            >
              注册
            </button>
          </div>
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
              />
            </div>

            <div class="form-group">
              <label for="login-password">密码</label>
              <input
                  type="password"
                  id="login-password"
                  placeholder="请输入您的密码"
                  v-model="loginForm.password"
              />
            </div>

            <div class="form-group remember-me">
              <div class="remember-me-group">
                <input
                    type="checkbox"
                    id="remember-me"
                    v-model="loginForm.rememberMe"
                />
                <label for="remember-me">记住我</label>
              </div>
              <a href="#" class="forgot-password">忘记密码?</a>
            </div>

            <p
                v-if="errorMessage"
                style="color: red; text-align: center; margin-bottom: 15px"
            >
              {{ errorMessage }}
            </p>
            <button class="submit-button" @click="handleLogin">
              登录
            </button>

            <div class="social-login">
              <p class="divider">
                <span>或使用其他方式登录</span>
              </p>
              <div class="social-buttons">
                <button class="social-button wechat">
                  <img
                      src="https://gitee.com/favicon.ico"
                      alt="gitee"
                      height="38px"
                  />
                </button>
                <button class="social-button qq">
                  <img
                      src="https://github.githubassets.com/assets/pinned-octocat-093da3e6fa40.svg"
                      alt="github"
                      height="38px"
                  />
                </button>
              </div>
            </div>
          </div>

          <div v-if="currentTab === 'register'" class="auth-form">
            <h2 class="form-title">创建新账户</h2>
            <p class="form-subtitle">请填写以下信息完成注册</p>
            <div class="form-group">
              <label for="register-username">用户名</label>
              <input
                  type="text"
                  id="register-username"
                  placeholder="请输入您的用户名"
                  v-model="registerForm.username"
              />
            </div>
            <div class="form-group">
              <label for="register-realname">真实姓名</label>
              <input
                  type="text"
                  id="register-realname"
                  placeholder="请输入您的姓名"
                  v-model="registerForm.realName"
              />
            </div>
            <div class="form-group">
              <label for="register-studentid">学号</label>
              <input
                  type="text"
                  id="register-studentid"
                  placeholder="请输入您的学号"
                  v-model="registerForm.studentId"
              />
            </div>
            <div class="form-group">
              <label for="register-email">邮箱</label>
              <input
                  type="email"
                  id="register-email"
                  placeholder="请输入您的邮箱"
                  v-model="registerForm.email"
              />
            </div>

            <div class="form-group">
              <label for="register-password">密码</label>
              <input
                  type="password"
                  id="register-password"
                  placeholder="请设置密码"
                  v-model="registerForm.password"
              />
            </div>

            <div class="form-group">
              <label for="register-confirm-password">确认密码</label>
              <input
                  type="password"
                  id="register-confirm-password"
                  placeholder="请再次输入密码"
                  v-model="registerForm.confirmPassword"
              />
            </div>

            <div class="form-group terms">
              <input type="checkbox" id="terms" v-model="registerForm.terms" />
              <label for="terms"
              >我已阅读并同意<a href="#">服务条款</a>和<a href="#"
              >隐私政策</a
              ></label
              >
            </div>
            <p
                v-if="errorMessage"
                style="color: red; text-align: center; margin-bottom: 15px"
            >
              {{ errorMessage }}
            </p>
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
                  <img
                      src="https://gitee.com/favicon.ico"
                      alt="gitee"
                      height="38px"
                  />
                </button>
                <button class="social-button qq">
                  <img
                      src="https://github.githubassets.com/assets/pinned-octocat-093da3e6fa40.svg"
                      alt="github"
                      height="38px"
                  />
                </button>
              </div>
            </div>
          </div>
          <div class="admin-login-entry">
            <router-link to="/admin/login">
              &raquo; 进入后台管理登录
            </router-link>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import { useUserStore } from "@/stores/userStore.js";
import apiClient from "@/api/axios.js";

const router = useRouter();
const userStore = useUserStore();

const currentTab = ref("login");
const errorMessage = ref("");

// --- 登录逻辑 ---
const loginForm = ref({
  username: "",
  password: "",
  rememberMe: false,
});

const handleLogin = async () => {
  errorMessage.value = "";
  try {
    await userStore.login(loginForm.value);
    alert("登录成功！");
    router.push("/");
  } catch (error) {
    errorMessage.value =
        error.response?.data?.message || "登录失败，请检查您的用户名和密码。";
    console.error("登录失败:", error);
  }
};

// --- 注册逻辑 ---
const registerForm = ref({
  username: "",
  realName: "",
  studentId: "",
  email: "",
  password: "",
  confirmPassword: "",
  terms: false,
});

const handleRegister = async () => {
  errorMessage.value = "";
  const { username, realName, studentId, email, password, confirmPassword } =
      registerForm.value;

  if (!username || !realName || !studentId || !email || !password) {
    errorMessage.value = "所有项目均为必填项，请填写完整！";
    return;
  }

  if (password !== confirmPassword) {
    errorMessage.value = "两次输入的密码不一致！";
    return;
  }

  try {
    const response = await apiClient.post("/api/auth/register", {
      realName: realName,
      studentId: studentId,
      username: username,
      email: email,
      password: password,
    });

    if (response.data.code === 201) {
      alert("注册成功！请前往登录。");
      currentTab.value = "login";
    }
  } catch (error) {
    errorMessage.value =
        error.response?.data?.message || "注册失败，请稍后重试。";
    console.error("注册失败:", error);
  }
};
</script>

<style scoped>
.auth-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f4f6f8;
  padding: 40px;
}

.container {
  max-width: 600px;
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
  padding: 16px 20px;
  font-size: 16px;
  font-weight: 500;
  color: #555;
  background-color: transparent;
  border: none;
  cursor: pointer;
  transition: color 0.3s ease;
}

.auth-tabs button.active {
  color: #1890ff;
  border-bottom: 2px solid #1890ff;
}

.auth-form {
  padding: 30px;
}

.form-title {
  font-size: 24px;
  font-weight: 600;
  color: #333;
  margin-bottom: 20px;
  text-align: center;
}

.form-subtitle {
  font-size: 14px;
  color: #777;
  margin-bottom: 25px;
  text-align: center;
}

.form-group {
  margin-bottom: 20px;
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

/* 文件路径: src/views/LoginPage.vue -> <style scoped> */

/*
  这是整行的样式
  用 flex 和 space-between 把“记住我组合”和“忘记密码”推向两端
*/
.remember-me {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 15px;
  font-size: 13px;
  color: #555;
}

/*
  这是“记住我”组合的样式
  【核心】同样使用 flex 和 align-items: center 来确保内部元素垂直居中
*/
.remember-me-group {
  display: flex;
  align-items: center; /* 确保复选框和文字垂直对齐 */
  gap: 6px; /* 控制复选框和文字之间的间距 */
}

/*
  让文字标签在点击时显示手型光标，提升用户体验
  并确保它没有额外的内外边距影响对齐
*/
.remember-me-group label {
  cursor: pointer;
  margin: 0;
  padding: 0;
}

/*
  确保复选框本身也没有额外的内外边距
*/
.remember-me input {
  width: 16px;
  height: 16px;
  cursor: pointer;
  margin: 0;
  padding: 0;
}

/* “忘记密码”链接的样式 */
.forgot-password {
  color: #1890ff;
  text-decoration: none;
}

.forgot-password:hover {
  text-decoration: underline;
}

.forgot-password {
  color: #1890ff;
  text-decoration: none;
}

.forgot-password:hover {
  text-decoration: underline;
}

/* 文件路径: src/views/LoginPage.vue -> <style scoped> */

/*
  【核心】
  使用 flex 和 align-items: center 来确保复选框和整段文字垂直居中对齐
*/
.terms {
  display: flex;
  align-items: center; /* 垂直居中对齐 */
  gap: 8px;          /* 在复选框和文字之间创建一个8px的间距 */
  margin-top: 15px;
  font-size: 13px;
  color: #555;
}

/*
  清理 label 和 input 的默认边距，防止它们影响对齐
*/
.terms label,
.terms input {
  margin: 0;
  padding: 0;
}

/*
  让<label>元素在被点击时也能触发复选框，并显示手型光标
*/
.terms label {
  cursor: pointer;
}

/* 给复选框也加上手型光标 */
.terms input {
  width: 16px;
  height: 16px;
  cursor: pointer;
  /* 【重要】由于 flex 布局，input会尝试收缩，
     设置 flex-shrink: 0; 可以防止这种情况，保持其原始大小 */
  flex-shrink: 0;
}

/* “服务条款”和“隐私政策”链接的样式 */
.terms a {
  color: #1890ff;
  text-decoration: none;
}

.terms a:hover {
  text-decoration: underline;
}

.submit-button {
  width: 100%;
  padding: 12px 15px;
  background-color: #1890ff;
  color: white;
  font-size: 16px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: background-color 0.3s ease;
  margin-top: 25px;
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
  content: "";
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

.admin-login-entry {
  text-align: center;
  padding: 20px 30px;
  border-top: 1px solid #e0e0e0;
  margin-top: 10px;
}

.admin-login-entry a {
  color: #555;
  text-decoration: none;
  font-weight: 500;
  transition: color 0.3s;
}

.admin-login-entry a:hover {
  color: #1890ff;
  text-decoration: underline;
}
</style>