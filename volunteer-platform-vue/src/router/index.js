// src/router/index.js
import { createRouter, createWebHistory } from 'vue-router';

// 导入布局组件
import MainLayout from '@/layouts/MainLayout.vue';
import AdminLayout from '@/layouts/AdminLayout.vue';

// 导入页面组件
import HomeView from '@/views/main/HomeView.vue';
import ActivitiesView from '@/views/main/ActivitiesView.vue';
import AuthView from '@/views/main/AuthView.vue';
import AdminLoginPage from '@/views/admin/AdminLoginPage.vue'; // 明确导入管理员登录页
import ActivityManagement from '@/views/admin/ActivityManagement.vue';

const routes = [
  // --- 规则一：前台页面路由 (使用MainLayout) ---
  {
    path: '/',
    component: MainLayout,
    children: [
      { path: '', name: 'Home', component: HomeView },
      { path: 'activities', name: 'Activities', component: ActivitiesView },
      // 其它需要导航栏和页脚的页面...
    ]
  },

  // --- 规则二：独立的、全屏的用户登录/注册页 ---
  // 这个页面不使用任何布局，是单独显示的
  {
    path: '/login',
    name: 'Auth',
    component: AuthView,
  },

  // --- 规则三：后台管理路由 (使用AdminLayout) ---
  {
    path: '/admin',
    component: AdminLayout,
    // meta: { requiresAuth: true }, // 可以后续添加登录保护
    children: [
      { path: '', redirect: '/admin/activities' },
      {
        path: 'activities',
        name: 'AdminActivityManagement',
        component: ActivityManagement
      },
      // 其它后台管理页面...
    ]
  },

  // --- 规则四：独立的、全屏的管理员登录页 ---
  {
    path: '/admin/login',
    name: 'AdminLogin',
    component: AdminLoginPage, // 使用专门的管理员登录组件
  }
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
});

export default router;