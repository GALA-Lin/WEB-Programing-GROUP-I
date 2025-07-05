<template>
  <div class="admin-layout" :class="{ 'sidebar-collapsed': isSidebarCollapsed }">
    <aside class="admin-sidebar">
      <div class="sidebar-header">
        <img src="https://i.postimg.cc/D006GCf7/logo-oil.png" alt="Logo" class="sidebar-logo"/>
        <h3 v-if="!isSidebarCollapsed">油炬智愿后台</h3>
      </div>
      <el-menu
          :default-active="$route.path"
          class="admin-menu"
          router
          :collapse="isSidebarCollapsed"
          background-color="#304156"
          text-color="#bfcbd9"
          active-text-color="#409EFF"
      >
        <el-menu-item index="/admin/dashboard">
          <el-icon><DataLine /></el-icon>
          <template #title><span>数据看板</span></template>
        </el-menu-item>
        <el-menu-item index="/admin/activities">
          <el-icon><Flag /></el-icon>
          <template #title><span>活动管理</span></template>
        </el-menu-item>
        <el-menu-item index="/admin/users">
          <el-icon><User /></el-icon>
          <template #title><span>用户管理</span></template>
        </el-menu-item>
        <el-menu-item index="/admin/organizations">
          <el-icon><OfficeBuilding /></el-icon>
          <template #title><span>组织管理</span></template>
        </el-menu-item>
        <el-menu-item index="/admin/news">
          <el-icon><Document /></el-icon>
          <template #title><span>新闻管理</span></template>
        </el-menu-item>
        <el-menu-item index="/admin/records">
          <el-icon><Clock /></el-icon>
          <template #title><span>时长管理</span></template>
        </el-menu-item>
      </el-menu>
    </aside>

    <div class="main-container">
      <div class="header-section">
        <Breadcrumb class="breadcrumb-container" />
        <TagsView />
      </div>

      <main class="admin-main-content">
        <router-view v-slot="{ Component }">
          <keep-alive :include="cachedViews">
            <component :is="Component" />
          </keep-alive>
        </router-view>
      </main>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue';
import { DataLine, Flag, User, OfficeBuilding, Document } from '@element-plus/icons-vue';
import AdminHeader from '@/components/AdminHeader.vue';
import TagsView from '@/components/tagIndex.vue';
import Breadcrumb from '@/components/Breadcrumb.vue'; // 引入面包屑
import { useTagsViewStore } from '@/stores/tagsView.js';

const tagsViewStore = useTagsViewStore();
const cachedViews = computed(() => tagsViewStore.cachedViews);
</script>

<style scoped>
.admin-layout-horizontal {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background-color: #f0f2f5;
}

/* 顶部导航栏样式 */
.top-navbar {
  background-color: #fff;
  border-bottom: 1px solid #e6e6e6;
  height: 60px;
  flex-shrink: 0;
  display: flex;
  align-items: center;
  padding: 0 20px;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);
}

.navbar-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.logo-area {
  display: flex;
  align-items: center;
}

.logo {
  width: 32px;
  height: 32px;
  margin-right: 12px;
}

.logo-text {
  font-size: 18px;
  font-weight: 600;
  color: var(--color-text-heading);
}

.top-menu {
  flex-grow: 1;
  border-bottom: none;
  margin-left: 50px; /* 与Logo区域拉开距离 */
}
/* 覆盖Element Plus菜单项的默认下边距 */
.top-menu.el-menu--horizontal > .el-menu-item {
  height: 59px;
}

.right-menu {
  display: flex;
  align-items: center;
}

/* 主内容区域 */
.main-container {
  flex-grow: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.header-section {
  flex-shrink: 0;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);
  /* 调整面包屑和标签页的样式 */
  padding-left: 20px;
}
.breadcrumb-container {
  line-height: 40px; /* 调整面包屑行高 */
}
/* 调整标签页容器的边框 */
:deep(.tags-view-container) {
  border-top: 1px solid #d8dce5;
  border-bottom: none;
  box-shadow: none;
  height: 38px;
}

.admin-main-content {
  flex-grow: 1;
  padding: 20px;
  overflow-y: auto;
}
</style>