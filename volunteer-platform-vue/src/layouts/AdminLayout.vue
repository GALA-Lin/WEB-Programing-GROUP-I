<template>
  <div class="admin-layout-horizontal">
    <header class="top-navbar">
      <div class="navbar-content">
        <div class="logo-area" @click="$router.push('/admin')">
          <img src="@/assets/new-logo.ico" class="h-8" alt="油炬智愿 Logo" />
          <span class="logo-text">油炬智愿 · 后台</span>
        </div>

        <el-menu
            :default-active="$route.path"
            class="top-menu"
            mode="horizontal"
            router
            :ellipsis="false"
        >
          <el-menu-item index="/admin/dashboard">
            <el-icon><DataLine /></el-icon>
            <span>数据看板</span>
          </el-menu-item>
          <el-menu-item index="/admin/activities">
            <el-icon><Flag /></el-icon>
            <span>活动管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/users">
            <el-icon><User /></el-icon>
            <span>用户管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/organizations">
            <el-icon><OfficeBuilding /></el-icon>
            <span>组织管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/news">
            <el-icon><Document /></el-icon>
            <span>新闻管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/records">
            <el-icon><Timer /></el-icon>
            <span>时长管理</span>
          </el-menu-item>

        </el-menu>

        <div class="right-menu">
          <AdminHeader />
        </div>
      </div>
    </header>

    <main class="main-container">
      <div class="page-header-container">
        <Breadcrumb />
        <TagsView />
      </div>
      <div class="app-main-content">
        <router-view v-slot="{ Component }">
          <keep-alive :include="cachedViews">
            <component :is="Component" class="app-view-card" />
          </keep-alive>
        </router-view>
      </div>
    </main>
  </div>
</template>

<script setup>
import { computed } from 'vue';
import { DataLine, Flag, User, OfficeBuilding, Document } from '@element-plus/icons-vue';
import AdminHeader from '@/components/AdminHeader.vue';
import TagsView from '@/components/tagIndex.vue';
import Breadcrumb from '@/components/Breadcrumb.vue';
import { useTagsViewStore } from '@/stores/tagsView.js';

const tagsViewStore = useTagsViewStore();
const cachedViews = computed(() => tagsViewStore.cachedViews);
</script>

<style scoped>
.admin-layout-horizontal {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background-color: #f7f8fa;
}

/* 顶部导航栏样式 */
.top-navbar {
  background-color: #ffffff;
  height: 60px;
  flex-shrink: 0;
  display: flex;
  align-items: center;
  padding: 0 24px;
  box-shadow: 0 1px 4px rgba(0, 21, 41, .08);
  z-index: 10;
}

.navbar-content {
  display: flex;
  align-items: center;
  width: 100%;
}

.logo-area {
  display: flex;
  align-items: center;
  cursor: pointer;
}
.logo { width: 32px; height: 32px; margin-right: 12px; }
.logo-text { font-size: 1.1rem; font-weight: 600; color: var(--color-text-heading); }

.top-menu {
  flex-grow: 1;
  border-bottom: none;
  margin-left: 40px;
  background-color: transparent;
}
.top-menu.el-menu--horizontal > .el-menu-item {
  height: 59px;
  border-bottom: 2px solid transparent;
  transition: all 0.2s ease;
}
.top-menu.el-menu--horizontal > .el-menu-item:not(.is-disabled):hover {
  background-color: transparent;
  color: var(--color-primary-hover);
}
.top-menu.el-menu--horizontal > .el-menu-item.is-active {
  border-bottom-color: var(--color-primary);
  color: var(--color-primary);
  font-weight: 600;
}

.right-menu {
  margin-left: auto;
}

/* 主内容区 */
.main-container {
  flex-grow: 1;
  overflow-y: auto;
}

.page-header-container {
  background-color: #ffffff;
  padding: 0 24px;
  box-shadow: 0 1px 2px 0 rgba(0, 0, 0, .05);
}
:deep(.breadcrumb-container .el-breadcrumb) {
  line-height: 44px;
}
:deep(.tags-view-container) {
  padding-left: 0;
  border-top: 1px solid #f0f2f5;
  box-shadow: none;
  border-bottom: none;
  height: 38px;
}

.app-main-content {
  padding: 24px;
}

/* 为每个路由页面添加卡片样式 */
:deep(.app-view-card) {
  background-color: #ffffff;
  padding: 24px;
  border-radius: 8px;
  min-height: calc(100vh - 180px); /* 减去顶部栏、头部、外边距等高度 */
}
</style>