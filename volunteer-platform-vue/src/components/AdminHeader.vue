<template>
  <div class="admin-header-right">
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
</template>

<script setup>
import { useUserStore } from '@/stores/userStore';
import { ArrowDown } from '@element-plus/icons-vue';

const userStore = useUserStore();
const defaultAvatar = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png';

const logout = async () => {
  await userStore.logout();
}
</script>

<style scoped>
.admin-header-right {
  display: flex;
  align-items: center;
}

.avatar-wrapper {
  position: relative;
  display: flex;
  align-items: center;
  cursor: pointer;
  gap: 8px;
}

.user-avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
}
</style>