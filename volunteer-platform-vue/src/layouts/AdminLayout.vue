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
        <AdminHeader
            :is-collapsed="isSidebarCollapsed"
            @toggle-sidebar="toggleSidebar"
        />
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
import { ref, computed } from 'vue';
import { DataLine, Flag, User, OfficeBuilding, Document } from '@element-plus/icons-vue';
import AdminHeader from '@/components/AdminHeader.vue';
import TagsView from '@/components/tagIndex.vue';
import { useTagsViewStore } from '@/stores/tagsView.js';

const tagsViewStore = useTagsViewStore();
const cachedViews = computed(() => tagsViewStore.cachedViews);

const isSidebarCollapsed = ref(false);
const toggleSidebar = () => {
  isSidebarCollapsed.value = !isSidebarCollapsed.value;
};
</script>

<style scoped>
.admin-layout {
  display: flex;
  height: 100vh;
  overflow: hidden;
}
.admin-sidebar {
  width: 220px;
  background-color: #304156;
  transition: width 0.28s;
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
}
.sidebar-header {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 15px 10px;
  gap: 10px;
  color: white;
  flex-shrink: 0;
  transition: padding 0.28s;
}
.sidebar-logo {
  width: 32px;
  height: 32px;
}
.sidebar-header h3 {
  margin: 0;
  font-size: 18px;
  white-space: nowrap;
}
.admin-menu {
  flex-grow: 1;
  border-right: none;
  overflow-y: auto;
}
.admin-menu:not(.el-menu--collapse) {
  width: 220px;
}
.main-container {
  flex-grow: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  transition: margin-left 0.28s;
}
.header-section {
  flex-shrink: 0;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);
}
.admin-main-content {
  flex-grow: 1;
  padding: 20px;
  background-color: #f0f2f5;
  overflow-y: auto;
}
.sidebar-collapsed .admin-sidebar {
  width: 64px !important;
}
.sidebar-collapsed .sidebar-header {
  padding: 15px 0;
}
</style>