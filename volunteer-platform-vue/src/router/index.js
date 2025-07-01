// src/router/index.js
import { createRouter, createWebHistory } from 'vue-router';

// 导入我们创建的布局组件
import MainLayout from '@/layouts/MainLayout.vue';
import AdminLayout from '@/layouts/AdminLayout.vue';

// 导入所有页面级组件
import HomeView from '@/views/main/HomeView.vue';
import ActivitiesView from '@/views/main/ActivitiesView.vue';
import AuthView from '@/views/main/AuthView.vue'; // 统一的登录/注册视图
import ActivityManagement from '@/views/admin/ActivityManagement.vue';

const routes = [
  // --- 规则一：前台页面路由 ---
  // 所有访问网站主体的路径，都使用 MainLayout 布局
  {
    path: '/',
    component: MainLayout,
    children: [
      { path: '', name: 'Home', component: HomeView },
      { path: 'activities', name: 'Activities', component: ActivitiesView },
      {
        path: 'login', // 普通用户的登录路径
        name: 'Login',
        component: AuthView,
        // 【关键】告诉 AuthView 组件，现在是 'user' 模式
        props: { mode: 'user' }
      }
    ]
  },

  // --- 规则二：后台管理路由 ---
  {
    path: '/admin',
    component: AdminLayout,
    // meta: { requiresAuth: true }, // 我们稍后可以启用这个来实现登录保护
    children: [
      // 当访问 /admin 时，自动跳转到活动管理页
      { path: '', redirect: '/admin/activities' },
      {
        path: 'activities',
        name: 'AdminActivityManagement',
        component: ActivityManagement
      }
    ]
  },

  // --- 规则三：独立的管理员登录页 ---
  // 这个页面不使用任何布局，是单独显示的
  {
    path: '/admin/login',
    name: 'AdminLogin',
    component: AuthView,
    // 【关键】告诉 AuthView 组件，现在是 'admin' 模式
    props: { mode: 'admin' }
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// 这里我们可以添加“路由守卫”，用于检查用户是否登录
// router.beforeEach((to, from, next) => { ... });

export default router;