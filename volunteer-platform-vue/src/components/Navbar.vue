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
          <router-link to="/news" class="nav-link">新闻资讯</router-link>
          <router-link to="/activities" class="nav-link">活动列表</router-link>
          <router-link to="/organizations" class="nav-link">组织列表</router-link>
          <router-link to="/about" class="nav-link">关于我们</router-link>
        </nav>

        <button
            class="mobile-menu-button"
            @click="mobileMenuOpen = !mobileMenuOpen"
            :class="{ open: mobileMenuOpen }"
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
              <router-link to="/profile" class="dropdown-item">个人中心</router-link>
              <button @click="logout" class="dropdown-item logout-button">退出登录</button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="mobile-nav-container" :class="{ 'mobile-nav-open': mobileMenuOpen }">
      <nav class="mobile-nav">
        <router-link to="/" class="mobile-nav-link" @click="closeMenu">首页</router-link>
        <router-link to="/news" class="mobile-nav-link" @click="closeMenu">新闻资讯</router-link>
        <router-link to="/activities" class="mobile-nav-link" @click="closeMenu">活动列表</router-link>
        <router-link to="/organizations" class="mobile-nav-link" @click="closeMenu">组织列表</router-link>
        <router-link to="/about" class="mobile-nav-link" @click="closeMenu">关于我们</router-link>

        <div class="mobile-actions">
          <div v-if="!isLoggedIn">
            <router-link to="/login" class="mobile-btn" @click="closeMenu">登录</router-link>
          </div>
          <div v-else class="mobile-user-menu">
            <router-link to="/profile" class="mobile-user-name" @click="closeMenu">你好, {{ currentUser?.username }}</router-link>
            <button @click="logout" class="mobile-btn-logout">退出登录</button>
          </div>
        </div>
      </nav>
    </div>
    <div class="overlay" v-if="mobileMenuOpen" @click="closeMenu"></div>
  </header>
</template>

<script setup>
import { ref } from 'vue';
import { useUserStore } from '@/stores/userStore';
import { storeToRefs } from 'pinia';
import { useRouter } from 'vue-router'; // **【核心修复】补上了这一行 import 语句**

const userStore = useUserStore();
const { isLoggedIn, currentUser } = storeToRefs(userStore);
const router = useRouter(); // **【核心修复】获取 router 实例**

const mobileMenuOpen = ref(false);

const closeMenu = () => {
  mobileMenuOpen.value = false;
};

// 使用 async/await 修正 logout 函数，并添加了路由跳转
const logout = async () => {
  if (confirm('您确定要退出登录吗？')) {
    await userStore.logout();
    closeMenu();
    await router.push('/login'); // 跳转到登录页
  }
};
</script>

<style scoped>
/* 样式部分没有变化，您可以保留原有的样式 */
/* ... */
.navbar {
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  position: sticky;
  top: 0;
  z-index: 1000;
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
  font-weight: 700;
  color: #1a202c;
}

/* 桌面端导航 */
.desktop-nav {
  display: flex;
  gap: 32px;
}
.nav-link {
  color: #4a5568;
  text-decoration: none;
  font-weight: 500;
  transition: color 0.2s;
  white-space: nowrap;
  position: relative;
  padding: 4px 0;
}
.nav-link::after {
  content: '';
  position: absolute;
  width: 0;
  height: 2px;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  background-color: #2563eb;
  transition: width 0.3s;
}
.nav-link:hover::after,
.nav-link.router-link-exact-active::after {
  width: 100%;
}
.nav-link:hover,
.nav-link.router-link-exact-active {
  color: #2563eb;
}

/* 桌面端用户操作区 */
.desktop-actions {
  display: flex;
  align-items: center;
}
.btn-login {
  background-color: #2563eb;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 6px;
  cursor: pointer;
  transition: background-color 0.2s, box-shadow 0.2s;
  text-decoration: none;
  margin-left: 10px;
  white-space: nowrap;
  font-weight: 500;
}
.btn-login:hover {
  background-color: #1d4ed8;
  box-shadow: 0 4px 10px rgba(37, 99, 235, 0.3);
}

/* 下拉菜单 */
.user-dropdown {
  position: relative;
  cursor: pointer;
}
.user-dropdown-trigger {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px;
}
.user-name {
  color: #2d3748;
  white-space: nowrap;
  font-weight: 500;
}
.arrow-down {
  border: solid #666;
  border-width: 0 2px 2px 0;
  display: inline-block;
  padding: 3px;
  transform: rotate(45deg);
  transition: transform 0.2s;
}
.user-dropdown:hover .arrow-down {
  transform: translateY(2px) rotate(225deg);
}
.user-dropdown-menu {
  position: absolute;
  top: 100%;
  right: 0;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  padding: 8px 0;
  min-width: 140px;
  z-index: 1010;
  opacity: 0;
  visibility: hidden;
  transform: translateY(10px);
  transition: opacity 0.2s, transform 0.2s;
}
.user-dropdown:hover .user-dropdown-menu {
  opacity: 1;
  visibility: visible;
  transform: translateY(0);
}
.dropdown-item {
  display: block;
  width: 100%;
  padding: 10px 16px;
  text-align: left;
  color: #2d3748;
  text-decoration: none;
  background-color: transparent;
  border: none;
  cursor: pointer;
  font-size: 14px;
  white-space: nowrap;
}
.dropdown-item:hover {
  background-color: #f7fafc;
  color: #2563eb;
}
.logout-button {
  color: #e53e3e;
}
.logout-button:hover {
  color: #c53030;
  background-color: #fff5f5;
}

/* 移动端菜单按钮 */
.mobile-menu-button {
  display: none;
  background: none;
  border: none;
  cursor: pointer;
  padding: 8px;
  z-index: 1100;
}
.bar {
  display: block;
  width: 25px;
  height: 3px;
  background-color: #333;
  margin: 5px auto;
  transition: all 0.3s ease-in-out;
}

/* 移动端导航容器 */
.mobile-nav-container {
  display: none;
  position: fixed;
  top: 0;
  right: -100%;
  width: 100%;
  max-width: 280px;
  height: 100vh;
  background-color: #ffffff;
  box-shadow: -2px 0 5px rgba(0, 0, 0, 0.1);
  transition: right 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  z-index: 1050;
  overflow-y: auto;
}
.mobile-nav-open {
  right: 0;
}
.mobile-nav {
  display: flex;
  flex-direction: column;
  padding: 80px 24px 24px;
}
.mobile-nav-link {
  color: #2d3748;
  text-decoration: none;
  padding: 16px 0;
  border-bottom: 1px solid #edf2f7;
  font-size: 16px;
  font-weight: 500;
}
.mobile-nav-link.router-link-exact-active {
  color: #2563eb;
}

/* 移动端操作区 */
.mobile-actions {
  margin-top: 32px;
  padding-top: 24px;
  border-top: 1px solid #edf2f7;
}
.mobile-btn {
  width: 100%;
  background-color: #2563eb;
  color: white;
  border: none;
  padding: 12px 16px;
  border-radius: 6px;
  cursor: pointer;
  text-decoration: none;
  text-align: center;
  font-weight: 500;
}
.mobile-user-menu {
  display: flex;
  flex-direction: column;
  gap: 16px;
}
.mobile-user-name {
  color: #2d3748;
  font-size: 16px;
  font-weight: 500;
  text-decoration: none;
  padding: 8px 0;
}
.mobile-btn-logout {
  width: 100%;
  background-color: #edf2f7;
  color: #e53e3e;
  border: none;
  padding: 12px 16px;
  border-radius: 6px;
  cursor: pointer;
  text-align: center;
  font-weight: 500;
}

/* 遮罩层 */
.overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 1040;
}

/* 响应式 */
@media (max-width: 768px) {
  .desktop-nav,
  .desktop-actions {
    display: none;
  }
  .mobile-menu-button {
    display: block;
  }
  .mobile-nav-container {
    display: block;
  }
  /* 汉堡菜单动画 */
  .mobile-menu-button.open .bar:nth-child(1) {
    transform: translateY(8px) rotate(45deg);
  }
  .mobile-menu-button.open .bar:nth-child(2) {
    opacity: 0;
  }
  .mobile-menu-button.open .bar:nth-child(3) {
    transform: translateY(-8px) rotate(-45deg);
  }
}
</style>