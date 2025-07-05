<template>
  <header class="navbar" :class="{ 'is-scrolled': scrolled }">
    <div class="container">
      <div class="navbar-content">
        <router-link to="/" class="navbar-logo">
          <img
              src="https://i.postimg.cc/D006GCf7/logo-oil.png"
              alt="油炬智愿 Logo"
              class="logo-image"
          />
          <span class="logo-text">油炬智愿</span>
        </router-link>

        <nav class="navbar-nav desktop-nav">
          <router-link to="/" class="nav-link">首页</router-link>
          <router-link to="/news" class="nav-link">新闻资讯</router-link>
          <router-link to="/activities" class="nav-link">活动广场</router-link>
          <router-link to="/organizations" class="nav-link">组织列表</router-link>
        </nav>

        <div class="navbar-actions">
          <div v-if="!isLoggedIn" class="desktop-actions">
            <router-link to="/login" class="btn btn-primary">登录 / 注册</router-link>
          </div>

          <div v-else class="user-dropdown desktop-actions">
            <div class="user-dropdown-trigger">
              <img :src="currentUser?.avatarUrl || defaultAvatar" alt="avatar" class="user-avatar">
              <span class="user-name">{{ currentUser?.username }}</span>
              <svg class="arrow-down" xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="m6 9 6 6 6-6"></path></svg>
            </div>
            <div class="user-dropdown-menu">
              <router-link to="/profile" class="dropdown-item">
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24"><path fill="currentColor" d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10s10-4.48 10-10S17.52 2 12 2m0 4c1.93 0 3.5 1.57 3.5 3.5S13.93 13 12 13s-3.5-1.57-3.5-3.5S10.07 6 12 6m0 14c-2.03 0-4.43-.82-6.14-2.88a9.96 9.96 0 0 1 12.28 0C16.43 19.18 14.03 20 12 20"/></svg>
                个人中心
              </router-link>
              <router-link to="/admin/activities" class="dropdown-item" v-if="isAdmin">
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24"><path fill="currentColor" d="M12 1L3 5v6c0 5.55 3.84 10.74 9 12c5.16-1.26 9-6.45 9-12V5zm0 10.99h7c-.53 4.12-3.28 7.79-7 8.94V12H5V6.3l7-3.11z"/></svg>
                后台管理
              </router-link>
              <div class="dropdown-divider"></div>
              <button @click="logout" class="dropdown-item logout-button">
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24"><path fill="currentColor" d="M5 21q-.825 0-1.412-.587T3 19V5q0-.825.588-1.412T5 3h7v2H5v14h7v2zm11-4l-1.375-1.45l2.55-2.55H9v-2h8.175l-2.55-2.55L16 7l5 5z"/></svg>
                退出登录
              </button>
            </div>
          </div>

          <button class="mobile-menu-button" @click="mobileMenuOpen = !mobileMenuOpen" :class="{ open: mobileMenuOpen }">
            <span class="bar"></span><span class="bar"></span><span class="bar"></span>
          </button>
        </div>
      </div>
    </div>

    <transition name="mobile-nav-fade">
      <nav class="mobile-nav" v-if="mobileMenuOpen" @click.self="closeMenu">
        <div class="mobile-nav-content">
          <router-link to="/" class="mobile-nav-link" @click="closeMenu">首页</router-link>
          <router-link to="/news" class="mobile-nav-link" @click="closeMenu">新闻资讯</router-link>
          <router-link to="/activities" class="mobile-nav-link" @click="closeMenu">活动广场</router-link>
          <router-link to="/organizations" class="mobile-nav-link" @click="closeMenu">组织列表</router-link>
          <div class="mobile-actions-divider"></div>
          <div v-if="!isLoggedIn" class="mobile-action-item">
            <router-link to="/login" class="btn btn-primary" @click="closeMenu">登录 / 注册</router-link>
          </div>
          <div v-else>
            <router-link to="/profile" class="mobile-nav-link" @click="closeMenu">个人中心</router-link>
            <router-link to="/admin/activities" class="mobile-nav-link" v-if="isAdmin" @click="closeMenu">后台管理</router-link>
            <div class="mobile-action-item">
              <button @click="logout" class="btn btn-danger-outline">退出登录</button>
            </div>
          </div>
        </div>
      </nav>
    </transition>
  </header>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue';
import { useUserStore } from '@/stores/userStore';
import { storeToRefs } from 'pinia';
import { useRouter } from 'vue-router';

const userStore = useUserStore();
const { isLoggedIn, currentUser, isAdmin } = storeToRefs(userStore);
const router = useRouter();

const defaultAvatar = 'https://ui-avatars.com/api/?name=User&background=random&color=fff';
const mobileMenuOpen = ref(false);
const scrolled = ref(false);

const closeMenu = () => {
  mobileMenuOpen.value = false;
};

const logout = async () => {
  await userStore.logout();
  closeMenu();
};

const handleScroll = () => {
  scrolled.value = window.scrollY > 10;
};

onMounted(() => {
  window.addEventListener('scroll', handleScroll);
});

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll);
});

</script>

<style scoped>
/* 使用了我们新定义的 CSS 变量 */
.navbar {
  position: sticky;
  top: 0;
  z-index: 1000;
  transition: background-color 0.3s, box-shadow 0.3s;
  background-color: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(8px);
  -webkit-backdrop-filter: blur(8px);
  border-bottom: 1px solid transparent;
}

.navbar.is-scrolled {
  background-color: rgba(255, 255, 255, 0.85);
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  border-bottom: 1px solid var(--color-border);
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
  height: 36px;
  width: auto;
  margin-right: 12px;
}

.logo-text {
  font-size: 22px;
  font-weight: 700;
  color: var(--color-text-heading);
}

.desktop-nav {
  display: none; /* 默认隐藏，仅在桌面端显示 */
}

@media (min-width: 768px) {
  .desktop-nav {
    display: flex;
    gap: 32px;
  }
}

.nav-link {
  color: var(--color-text-body);
  text-decoration: none;
  font-weight: 500;
  transition: color 0.2s;
  position: relative;
  padding: 8px 4px;
}
.nav-link::after {
  content: '';
  position: absolute;
  width: 0;
  height: 2px;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  background-color: var(--color-primary);
  transition: width 0.3s ease;
}
.nav-link:hover, .nav-link.router-link-exact-active {
  color: var(--color-primary);
}
.nav-link:hover::after, .nav-link.router-link-exact-active::after {
  width: 100%;
}

.navbar-actions {
  display: flex;
  align-items: center;
}

.desktop-actions {
  display: none;
}
@media (min-width: 768px) {
  .desktop-actions {
    display: flex;
  }
}

.btn {
  padding: 8px 20px;
  border-radius: 6px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
  border: 1px solid transparent;
}
.btn-primary {
  background-color: var(--color-primary);
  color: white;
}
.btn-primary:hover {
  background-color: var(--color-primary-hover);
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.2);
}

.user-dropdown {
  position: relative;
  /* 新增：增加一个透明的底部内边距，作为鼠标移动的“桥梁” */
  padding-bottom: 12px;
  /* 新增：用负外边距抵消掉padding增加的高度，防止布局跳动 */
  margin-bottom: -12px;
}

.user-dropdown-trigger {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 6px;
  border-radius: 99px;
  cursor: pointer;
  transition: background-color 0.2s;
}
.user-dropdown-trigger:hover {
  background-color: var(--color-background-soft);
}
.user-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid var(--color-surface);
}
.user-name {
  font-weight: 500;
  color: var(--color-text-body);
}
.arrow-down {
  transition: transform 0.2s;
}
.user-dropdown:hover .arrow-down {
  transform: rotate(180deg);
}

.user-dropdown-menu {
  position: absolute;
  /* 修改：将 top 的值从 calc(100% + 8px) 改为 100% */
  top: 100%;
  right: 0;
  background-color: var(--color-surface);
  border-radius: 8px;
  box-shadow: 0 10px 25px -5px rgba(0, 0, 0, 0.1), 0 8px 10px -6px rgba(0, 0, 0, 0.1);
  border: 1px solid var(--color-border);
  padding: 8px;
  min-width: 180px;
  z-index: 1010;
  opacity: 0;
  visibility: hidden;
  transform: translateY(0); /* 修改：初始时不再向下偏移，让过渡更平滑 */
  transition: opacity 0.2s, transform 0.2s;
}

.user-dropdown:hover .user-dropdown-menu {
  opacity: 1;
  visibility: visible;
  /* transform: translateY(0); 这一行在上面已经修改，这里保持不变 */
}

.dropdown-item {
  display: flex;
  align-items: center;
  gap: 12px;
  width: 100%;
  padding: 10px 12px;
  text-align: left;
  color: var(--color-text-body);
  text-decoration: none;
  background-color: transparent;
  border: none;
  cursor: pointer;
  font-size: 14px;
  border-radius: 6px;
}
.dropdown-item:hover {
  background-color: var(--color-background-soft);
  color: var(--color-text-heading);
}
.dropdown-item svg {
  color: var(--color-text-muted);
}
.dropdown-item:hover svg {
  color: var(--color-primary);
}
.logout-button {
  color: #ef4444;
}
.logout-button:hover {
  background-color: #fee2e2;
  color: #991b1b;
}

.dropdown-divider {
  height: 1px;
  background-color: var(--color-border);
  margin: 8px -8px;
}

/* 移动端菜单按钮 */
.mobile-menu-button {
  display: block;
  background: none;
  border: none;
  cursor: pointer;
  padding: 8px;
  z-index: 1100;
}
@media (min-width: 768px) {
  .mobile-menu-button {
    display: none;
  }
}
.bar {
  display: block;
  width: 24px;
  height: 2px;
  background-color: var(--color-text-heading);
  margin: 5px auto;
  transition: all 0.3s ease-in-out;
}
.mobile-menu-button.open .bar:nth-child(1) { transform: translateY(7px) rotate(45deg); }
.mobile-menu-button.open .bar:nth-child(2) { opacity: 0; }
.mobile-menu-button.open .bar:nth-child(3) { transform: translateY(-7px) rotate(-45deg); }


/* 移动端导航 */
.mobile-nav {
  position: fixed;
  top: 65px; /* 在导航栏下方 */
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.2);
  backdrop-filter: blur(5px);
  -webkit-backdrop-filter: blur(5px);
  z-index: 1050;
}
.mobile-nav-content {
  background-color: var(--color-surface);
  padding: 16px;
  display: flex;
  flex-direction: column;
}

.mobile-nav-link {
  color: var(--color-text-body);
  text-decoration: none;
  padding: 16px;
  border-bottom: 1px solid var(--color-border);
  font-size: 16px;
  font-weight: 500;
  text-align: center;
}
.mobile-nav-link.router-link-exact-active {
  color: var(--color-primary);
  background-color: var(--color-primary-soft);
  border-radius: 6px;
}
.mobile-actions-divider {
  margin-top: 24px;
}
.mobile-action-item {
  margin-top: 16px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}
.mobile-action-item .btn {
  width: 100%;
  text-align: center;
}
.btn-danger-outline {
  background-color: transparent;
  color: #ef4444;
  border: 1px solid #ef4444;
}

.mobile-nav-fade-enter-active, .mobile-nav-fade-leave-active {
  transition: opacity 0.3s;
}
.mobile-nav-fade-enter-from, .mobile-nav-fade-leave-to {
  opacity: 0;
}
</style>