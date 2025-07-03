<template>
  <div class="admin-layout">
    <aside class="admin-sidebar">
      <h3>后台管理系统</h3>
      <ul>
        <li><router-link to="/admin/activities">活动管理</router-link></li>
        <li><router-link to="/admin/users">用户管理</router-link></li>
      </ul>
    </aside>
    <main class="admin-main-content">
      <router-view />
    </main>
  </div>
</template>

<script setup>
// 这里可以添加后台布局的逻辑，比如获取用户信息、退出登录等
</script>

<style scoped>
.admin-layout { display: flex; min-height: 100vh; }
.admin-sidebar { width: 220px; background-color: #304156; color: white; padding: 20px; }
.admin-sidebar h3 { text-align: center; }
.admin-sidebar ul { list-style: none; padding: 0; margin-top: 30px; }
.admin-sidebar li a { display: block; padding: 12px 18px; color: #bfcbd9; text-decoration: none; border-radius: 4px; }
.admin-sidebar li a:hover,
.admin-sidebar li a.router-link-exact-active { background-color: #263445; color: #409EFF; }
.admin-main-content { flex: 1; padding: 20px; background-color: #f0f2f5; }
</style>