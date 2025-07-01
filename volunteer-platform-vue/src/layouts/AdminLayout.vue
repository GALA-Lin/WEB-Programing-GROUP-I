<template>
  <div class="admin-layout">
    <aside class="admin-sidebar">
      <h3>后台管理</h3>
      <ul>
        <li>
          <router-link to="/admin/activities">活动管理</router-link>
        </li>
        <li>
          <router-link to="/admin/users">用户管理</router-link> </li>
      </ul>
      <button @click="logout">退出登录</button>
    </aside>

    <main class="admin-main-content">
      <router-view />
    </main>
  </div>
</template>

<script setup>
import { useUserStore } from '@/stores/userStore';
import { useRouter } from 'vue-router';

const userStore = useUserStore();
const router = useRouter();

const logout = () => {
  userStore.logout(); // 假设 store 中有 logout 方法
  router.push({ name: 'AdminLogin' });
};
</script>

<style scoped>
.admin-layout {
  display: flex;
  min-height: 100vh;
}
.admin-sidebar {
  width: 240px;
  background-color: #2c3e50;
  color: white;
  padding: 20px;
}
.admin-sidebar h3 {
  text-align: center;
}
.admin-sidebar ul {
  list-style: none;
  padding: 0;
}
.admin-sidebar li a {
  display: block;
  padding: 10px 15px;
  color: white;
  text-decoration: none;
  border-radius: 4px;
}
.admin-sidebar li a:hover,
.admin-sidebar li a.router-link-exact-active {
  background-color: #34495e;
}
.admin-main-content {
  flex: 1;
  padding: 20px;
  background-color: #f0f2f5;
}
</style>