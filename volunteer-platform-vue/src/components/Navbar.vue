<template>
  <header class="navbar">
    <div class="container">
      <div class="navbar-content">
        <router-link to="/" class="navbar-logo">
          <img
              src="data:image/png;base64,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"
              alt="志愿平台logo"
              class="logo-image"
          />
          <span class="logo-text">油炬智愿</span>
        </router-link>

        <nav class="navbar-nav desktop-nav">
          <router-link to="/" class="nav-link">首页</router-link>
          <router-link to="/activities" class="nav-link">活动列表</router-link>
          <router-link to="/organizations" class="nav-link">组织列表</router-link>
          <router-link to="/about" class="nav-link">关于我们</router-link>
        </nav>

        <button
            class="mobile-menu-button"
            @click="mobileMenuOpen = !mobileMenuOpen"
            aria-label="菜单"
        >
          <span class="bar"></span>
          <span class="bar"></span>
          <span class="bar"></span>
        </button>

        <div class="navbar-actions desktop-actions">
          <router-link to="/login" v-if="!isLoggedIn" class="btn-login">登录/注册</router-link>

          <div v-else class="user-dropdown">
            <div class="user-dropdown-trigger">
              <span class="user-name">{{ currentUser?.username }}</span>
              <span class="arrow-down"></span>
            </div>

            <div class="user-dropdown-menu">
              <router-link to="/profile" class="dropdown-item">
                个人中心
              </router-link>
              <button @click="logout" class="dropdown-item logout-button">
                退出登录
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div
        class="mobile-nav-container"
        :class="{ 'mobile-nav-open': mobileMenuOpen }"
    >
    </div>
  </header>
</template>

<script setup>
// script 部分保持不变
import { ref } from 'vue';
import { useUserStore } from '@/stores/userStore';
import { storeToRefs } from 'pinia';

const userStore = useUserStore();
const { isLoggedIn, currentUser } = storeToRefs(userStore);
const mobileMenuOpen = ref(false);

const logout = () => {
  userStore.logout();
  mobileMenuOpen.value = false;
};
</script>

<style scoped>
/* 原有样式保持不变 */
.navbar {
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 100;
}
.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 16px;
}
.navbar-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 64px;
}
.navbar-logo {
  display: flex;
  align-items: center;
  text-decoration: none;
}
.logo-image {
  height: 40px;
  margin-right: 8px;
  object-fit: contain;
}
.logo-text {
  font-size: 20px;
  font-weight: bold;
  color: #333;
}
.desktop-nav {
  display: flex;
  gap: 24px;
}
.nav-link {
  color: #666;
  text-decoration: none;
  transition: color 0.2s;
  white-space: nowrap;
}
.nav-link:hover,
.nav-link.router-link-active {
  color: #2563eb;
}
.desktop-actions {
  display: flex;
  align-items: center;
}
.btn-login {
  background-color: #2563eb;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.2s;
  text-decoration: none;
  margin-left: 10px;
  white-space: nowrap;
}
.btn-login:hover {
  background-color: #1d4ed8;
}

/* --- 【以下是新增和修改的样式】 --- */

/* 下拉菜单容器 */
.user-dropdown {
  position: relative; /* 关键：为绝对定位的子元素提供定位上下文 */
  cursor: pointer;
}

/* 下拉菜单触发器（用户名+箭头） */
.user-dropdown-trigger {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px;
}

.user-name {
  color: #333;
  white-space: nowrap;
  font-weight: 500;
}

/* 下拉箭头样式 */
.arrow-down {
  border: solid #666;
  border-width: 0 2px 2px 0;
  display: inline-block;
  padding: 3px;
  transform: rotate(45deg);
  transition: transform 0.2s;
}

/* 鼠标悬停时箭头旋转 */
.user-dropdown:hover .arrow-down {
  transform: rotate(225deg);
}

/* 下拉菜单主体 */
.user-dropdown-menu {
  position: absolute; /* 关键：相对于父元素进行定位 */
  top: 100%; /* 从父元素底部开始显示 */
  right: 0; /* 对齐到父元素右侧 */
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  padding: 8px 0;
  min-width: 120px; /* 菜单最小宽度 */
  z-index: 110;

  /* 默认隐藏，通过悬停显示 */
  display: none;
}

/* 鼠标悬停在容器上时，显示下拉菜单 */
.user-dropdown:hover .user-dropdown-menu {
  display: block;
}

/* 下拉菜单中的项目 */
.dropdown-item {
  display: block;
  width: 100%;
  padding: 10px 16px;
  text-align: left;
  color: #333;
  text-decoration: none;
  background-color: transparent;
  border: none;
  cursor: pointer;
  font-size: 14px;
  white-space: nowrap;
}

.dropdown-item:hover {
  background-color: #f5f5f5;
}

.logout-button {
  color: #ff4d4f; /* 退出按钮使用警示色 */
}

/* 移动端样式保持不变 */
@media (max-width: 768px) {
  /* ... 原有的移动端样式 ... */
}
</style>