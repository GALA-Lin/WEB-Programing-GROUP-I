<template>
  <header class="navbar">
    <div class="container">
      <div class="navbar-content">
        <router-link to="/" class="navbar-logo">志愿平台</router-link>
        <nav class="navbar-nav">
          <router-link to="/" class="nav-link">首页</router-link>
          <router-link to="/activities" class="nav-link">活动列表</router-link>
          <router-link to="/organizations" class="nav-link">组织列表</router-link>
          <router-link to="/about" class="nav-link">关于我们</router-link>
        </nav>
        <div class="navbar-actions">
          <router-link to="/login" v-if="!isLoggedIn" class="btn-login">登录</router-link>
          <router-link to="/register" v-if="!isLoggedIn" class="btn-register">注册</router-link>
          <div v-else class="user-menu">
            <span class="user-name">用户名</span>
            <button @click="logout" class="btn-logout">退出</button>
          </div>
        </div>
      </div>
    </div>
  </header>
</template>

<script setup>
import { ref } from 'vue';

const isLoggedIn = ref(false);

const login = () => {
  isLoggedIn.value = true;
};

const logout = () => {
  isLoggedIn.value = false;
};
</script>

<style scoped>
.navbar {
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 100;
}

.container {
  max-width: 1200px;
  margin:  auto;
  padding: 0 16px;
}

.navbar-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 64px;
}

.navbar-logo {
  font-size: 20px;
  font-weight: bold;
  color: #333;
  text-decoration: none;
}

.navbar-nav {
  display: flex;
  gap: 24px;
}

.nav-link {
  color: #666;
  text-decoration: none;
  transition: color 0.2s;
}

.nav-link:hover,
.nav-link.router-link-active {
  color: #2563eb;
}

.navbar-actions {
  display: flex;
  align-items: center;
}
.btn-register{
  background-color: #65aeff;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.2s;
  margin-right: 8px;
  margin-left: 8px;
}
.btn-login {
  background-color: #2563eb;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.2s;
  margin-right: 8px;
  margin-left: 8px;
}
.btn-register:hover{
  background-color: #4c9aff;
}
.btn-login:hover {
  background-color: #1d4ed8;
}

.user-menu {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-name {
  color: #333;
}

.btn-logout {
  background-color: transparent;
  color: #666;
  border: none;
  cursor: pointer;
  text-decoration: underline;
}
</style>
    