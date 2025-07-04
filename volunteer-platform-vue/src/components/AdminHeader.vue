<template>
  <div class="navbar">
    <div class="left-menu">
      <el-icon class="sidebar-toggler" @click="toggleSidebar">
        <component :is="isCollapsed ? 'Expand' : 'Fold'" />
      </el-icon>
      <Breadcrumb class="breadcrumb-container" />
    </div>

    <div class="right-menu">
      <el-dropdown class="avatar-container" trigger="click">
        <div class="avatar-wrapper">
          <img :src="userStore.currentUser?.avatarUrl || defaultAvatar" class="user-avatar">
          <span>{{ userStore.currentUser?.username }}</span>
          <el-icon class="el-icon--right"><arrow-down /></el-icon>
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <router-link to="/profile">
              <el-dropdown-item>个人中心</el-dropdown-item>
            </router-link>
            <router-link to="/">
              <el-dropdown-item>返回前台</el-dropdown-item>
            </router-link>
            <el-dropdown-item divided @click="logout">
              <span>退出登录</span>
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script setup>
import { useUserStore } from '@/stores/userStore';
import Breadcrumb from './Breadcrumb.vue';
// 【新增】导入图标
import { ArrowDown, Expand, Fold } from '@element-plus/icons-vue';

// 【新增】定义 props 和 emits
const props = defineProps({
  isCollapsed: {
    type: Boolean,
    default: false,
  },
});
const emit = defineEmits(['toggleSidebar']);

const userStore = useUserStore();
const defaultAvatar = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png';

// 【新增】切换侧边栏的方法
const toggleSidebar = () => {
  emit('toggleSidebar');
};

const logout = async () => {
  await userStore.logout();
}
</script>

<style scoped>
.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 5px 0 15px; /* 调整内边距 */
}

/* 【新增】样式 */
.left-menu {
  display: flex;
  align-items: center;
}
.sidebar-toggler {
  cursor: pointer;
  font-size: 20px;
  margin-right: 15px;
}

.right-menu {
  display: flex;
  align-items: center;
}

.avatar-wrapper {
  margin-top: 5px;
  position: relative;
  display: flex;
  align-items: center;
  cursor: pointer;
  gap: 8px;
}

.user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 10px;
}
</style>