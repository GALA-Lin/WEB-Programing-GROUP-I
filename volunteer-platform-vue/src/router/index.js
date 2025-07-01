// src/router/index.js
import { createRouter, createWebHistory } from 'vue-router';
import { useUserStore } from '@/stores/userStore.js';

// 导入布局组件
import MainLayout from '@/layouts/MainLayout.vue';
import AdminLayout from '@/layouts/AdminLayout.vue';

// 导入页面组件
import HomeView from '@/views/main/HomeView.vue';
import ActivitiesView from '@/views/main/ActivitiesView.vue';
import AuthView from '@/views/main/AuthView.vue'; // 统一的登录/注册视图
import ActivityManagement from '@/views/admin/ActivityManagement.vue';

const routes = [
  // --- 前台页面路由 (使用 MainLayout 布局) ---
  {
    path: '/',
    component: MainLayout,
    children: [
      { path: '', name: 'Home', component: HomeView },
      { path: 'activities', name: 'Activities', component: ActivitiesView },
      {
        path: 'profile', // 个人中心
        name: 'Profile',
        component: () => import('@/views/main/ProfileView.vue'), // 懒加载
        meta: { requiresAuth: true } // 需要登录才能访问
      }
    ]
  },

  // --- 独立的用户登录/注册页 ---
  // 使用 props 区分是普通用户模式
  {
    path: '/login',
    name: 'Login', // 路由守卫中使用了这个名称
    component: AuthView,
    props: { mode: 'user' }
  },

  // --- 后台管理路由 (使用 AdminLayout 布局) ---
  {
    path: '/admin',
    component: AdminLayout,
    meta: { requiresAuth: true }, // 访问后台所有页面都需要登录
    children: [
      { path: '', redirect: '/admin/activities' }, // 默认跳转到活动管理
      {
        path: 'activities',
        name: 'AdminActivityManagement',
        component: ActivityManagement
      }
    ]
  },

  // --- 独立的管理员登录页 ---
  // 使用 props 区分是管理员模式
  {
    path: '/admin/login',
    name: 'AdminLogin',
    component: AuthView, // 复用统一的登录组件
    props: { mode: 'admin' }
  }
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
});

// --- 全局路由守卫 ---
// 在每次路由跳转前执行，用于权限检查
router.beforeEach((to, from, next) => {
  const userStore = useUserStore();
  const isTryingToAccessProtectedRoute = to.meta.requiresAuth;
  const isLoggedIn = userStore.isLoggedIn;

  // 场景: 访问需要登录的页面，但用户未登录
  if (isTryingToAccessProtectedRoute && !isLoggedIn) {
    alert('此页面需要登录后才能访问！');

    // 判断应跳转到哪个登录页
    if (to.path.startsWith('/admin')) {
      // 访问后台 -> 跳转到后台登录页
      next({ name: 'AdminLogin' });
    } else {
      // 访问前台 -> 跳转到前台登录页
      next({ name: 'Login' });
    }
  } else {
    // 其他情况（访问无需登录的页面，或者已登录），直接放行
    next();
  }
});

export default router;