<template>
  <header class="navbar">
    <div class="container">
      <div class="navbar-content">
        <!-- Logo区域 -->
        <router-link to="/" class="navbar-logo">
          <!-- 图片logo -->
          <img
              src="data:image/png;base64,
              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"
              alt="志愿平台logo"
              class="logo-image"
          />
          <span class="logo-text">油炬智愿</span>
        </router-link>



        <!-- 桌面端导航 -->
        <nav class="navbar-nav desktop-nav">
          <router-link to="/" class="nav-link">首页</router-link>
          <router-link to="/news" class="nav-link">新闻资讯</router-link>
          <router-link to="/activities" class="nav-link">活动列表</router-link>
          <router-link to="/organizations" class="nav-link">组织列表</router-link>
          <router-link to="/about" class="nav-link">关于我们</router-link>

        </nav>

        <!-- 移动端汉堡菜单按钮 -->
        <button
            class="mobile-menu-button"
            @click="mobileMenuOpen = !mobileMenuOpen"
            aria-label="菜单"
        >
          <span class="bar"></span>
          <span class="bar"></span>
          <span class="bar"></span>
        </button>

        <!-- 桌面端用户操作区 -->
        <div class="navbar-actions desktop-actions">
          <router-link to="/login" v-if="!isLoggedIn" class="btn-login">登录/注册</router-link>
          <div v-else class="user-menu">
            <span class="user-name">{{ currentUser?.username }}</span>
            <button @click="logout" class="btn-logout">退出</button>
          </div>
        </div>
      </div>
    </div>

    <!-- 移动端导航菜单 -->
    <div
        class="mobile-nav-container"
        :class="{ 'mobile-nav-open': mobileMenuOpen }"
    >
      <nav class="mobile-nav">
        <router-link to="/" class="mobile-nav-link" @click="mobileMenuOpen = false">首页</router-link>
        <router-link to="/activities" class="mobile-nav-link" @click="mobileMenuOpen = false">活动列表</router-link>
        <router-link to="/organizations" class="mobile-nav-link" @click="mobileMenuOpen = false">组织列表</router-link>
        <router-link to="/about" class="mobile-nav-link" @click="mobileMenuOpen = false">关于我们</router-link>

        <div class="mobile-actions">
          <router-link to="/login" v-if="!isLoggedIn" class="mobile-btn">登录</router-link>
          <router-link to="/register" v-if="!isLoggedIn" class="mobile-btn">注册</router-link>
          <div v-else class="mobile-user-menu">
            <span class="mobile-user-name">{{ currentUser?.username }}</span>
            <button @click="logout" class="mobile-btn-logout">退出</button>
          </div>
        </div>
      </nav>
    </div>
  </header>
</template>

<script setup>
import { ref } from 'vue';
import { useUserStore } from '@/stores/userStore'; // 1. 导入 userStore
import { storeToRefs } from 'pinia'; // 导入 storeToRefs 来保持响应性

const userStore = useUserStore(); // 2. 获取 userStore 实例

// 3. 使用 storeToRefs 来解构 store 中的状态，确保它们在模板中是响应式的
const { isLoggedIn, currentUser } = storeToRefs(userStore);

const mobileMenuOpen = ref(false);

// 4. 从 store 中获取 logout 方法
const logout = () => {
  userStore.logout();
  mobileMenuOpen.value = false; // 退出时关闭菜单
};
</script>

<style scoped>
/* 基础导航栏样式 */
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

/* Logo样式 */
.navbar-logo {
  display: flex;
  align-items: center;
  text-decoration: none;
}

.logo-image {
  height: 40px; /* 调整logo高度 */
  margin-right: 8px; /* 图片与文本之间的间距 */
  object-fit: contain; /* 保持图片比例 */
}

.logo-text {
  font-size: 20px;
  font-weight: bold;
  color: #333;
}

/* 桌面端导航 */
.desktop-nav {
  display: flex;
  gap: 24px;
}

.nav-link {
  color: #666;
  text-decoration: none;
  transition: color 0.2s;
  white-space: nowrap; /* 防止文字换行 */
}

.nav-link:hover,
.nav-link.router-link-active {
  color: #2563eb;
}

/* 桌面端用户操作区 */
.desktop-actions {
  display: flex;
  align-items: center;
}

.btn-login{
  background-color: #2563eb;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.2s;
  text-decoration: none;
  margin-left: 10px;
  white-space: nowrap; /* 防止文字换行 */
}

.btn-login:hover{
  background-color: #1d4ed8;
}

.user-menu {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-name {
  color: #333;
  white-space: nowrap; /* 防止文字换行 */
}

.btn-logout {
  background-color: transparent;
  color: #666;
  border: none;
  cursor: pointer;
  text-decoration: underline;
  white-space: nowrap; /* 防止文字换行 */
}

/* 移动端汉堡菜单按钮 */
.mobile-menu-button {
  display: none;
  background: none;
  border: none;
  cursor: pointer;
  padding: 8px;
  z-index: 110;
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
  max-width: 300px;
  height: 100vh;
  background-color: white;
  box-shadow: -2px 0 5px rgba(0, 0, 0, 0.1);
  transition: right 0.3s ease-in-out;
  z-index: 105;
  overflow-y: auto;
}

.mobile-nav-open {
  right: 0;
}

/* 移动端导航菜单 */
.mobile-nav {
  display: flex;
  flex-direction: column;
  padding: 80px 20px 20px;
}

.mobile-nav-link {
  color: #333;
  text-decoration: none;
  padding: 15px 0;
  border-bottom: 1px solid #eee;
  font-size: 16px;
}

.mobile-nav-link.router-link-active {
  color: #2563eb;
}

/* 移动端用户操作区 */
.mobile-actions {
  display: flex;
  flex-direction: column;
  margin-top: 20px;
}

.mobile-btn {
  background-color: #2563eb;
  color: white;
  border: none;
  padding: 10px 16px;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.2s;
  text-decoration: none;
  margin-bottom: 10px;
  text-align: center;
}

.mobile-user-menu {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.mobile-user-name {
  color: #333;
  padding: 10px 0;
  font-size: 16px;
}

.mobile-btn-logout {
  background-color: transparent;
  color: #666;
  border: none;
  cursor: pointer;
  text-decoration: underline;
  padding: 10px 0;
  text-align: left;
}

/* 遮罩层 - 当移动端菜单打开时显示 */
.overlay {
  display: none;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 100;
}

.mobile-nav-open + .overlay {
  display: block;
}

/* 媒体查询 - 响应式设计 */
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

  /* 汉堡菜单动画效果 */
  .mobile-menu-button.open .bar:nth-child(1) {
    transform: translateY(8px) rotate(45deg);
  }

  .mobile-menu-button.open .bar:nth-child(2) {
    opacity: 0;
  }

  .mobile-menu-button.open .bar:nth-child(3) {
    transform: translateY(-8px) rotate(-45deg);
  }

  /* 移动端logo调整 */
  .logo-image {
    height: 32px; /* 移动端logo尺寸减小 */
  }

  .logo-text {
    font-size: 18px; /* 移动端文本尺寸减小 */
  }
}
</style>
