// src/router/index.js
import { createRouter, createWebHistory } from 'vue-router';
import { useUserStore } from '@/stores/userStore';

// 导入布局组件
import MainLayout from '@/layouts/MainLayout.vue';
import AdminLayout from '@/layouts/AdminLayout.vue';

// 导入页面组件
import HomeView from '@/views/main/HomeView.vue';
import ActivitiesView from '@/views/main/ActivitiesView.vue';
import LoginPage from '@/views/main/LoginPage.vue';
import AdminLoginPage from '@/views/admin/AdminLoginPage.vue';
import ActivityManagement from '@/views/admin/ActivityManagement.vue';


const routes = [
  // --- 前台路由 ---
  // 所有前台页面都作为 MainLayout 的子路由
  {
    path: '/',
    component: MainLayout,
    children: [
      { path: '', name: 'Home', component: HomeView },
      { path: 'activities', name: 'Activities', component: ActivitiesView },
      { path: 'login', name: 'Login', component: LoginPage },
    ]
  },

  // --- 后台路由 ---
  // 单独的管理员登录页，不使用任何布局
  {
    path: '/admin/login',
    name: 'AdminLogin',
    component: AdminLoginPage
  },
  // 所有受保护的后台页面都作为 AdminLayout 的子路由
  {
    path: '/admin',
    component: AdminLayout,
    meta: { requiresAuth: true }, // 这个 meta 字段用于路由守卫
    children: [
      // 默认后台首页，可以重定向到活动管理
      { path: '', redirect: '/admin/activities' },
      {
        path: 'activities',
        name: 'AdminActivityManagement',
        component: ActivityManagement
      },
      // 未来可以添加更多后台页面，例如用户管理
      // { path: 'users', name: 'AdminUserManagement', component: UserManagement },
    ]
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// --- 全局路由守卫 ---
// 在每次路由跳转前执行
router.beforeEach((to, from, next) => {
  // 检查目标路由是否需要认证
  if (to.meta.requiresAuth) {
    const userStore = useUserStore();
    // 检查 Pinia store 中是否有管理员登录凭证
    if (userStore.isAdmin) { // 假设你在 userStore 中有 isAdmin 状态
      next(); // 已登录，放行
    } else {
      // 未登录，跳转到管理员登录页
      next({ name: 'AdminLogin' });
    }
  } else {
    next(); // 不需要认证，直接放行
  }
});

export default router;