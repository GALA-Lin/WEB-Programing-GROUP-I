// src/router/index.js
import { createRouter, createWebHistory } from 'vue-router';
// 【修改】导入 userStore 以便在路由守卫中使用
import { useUserStore } from '@/stores/userStore.js';
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
        props: { mode: 'user' }
      },
      // 【新增】用户个人中心路由
      {
        path: '/profile', // 注意：路径以'/'开头会成为根路径，这里我们希望是 /profile
        name: 'Profile',
        // 懒加载我们之前创建的 ProfileView 组件
        component: () => import('@/views/main/ProfileView.vue'),
        // 添加元信息，标记此路由需要登录
        meta: { requiresAuth: true }
      }
    ]
  },

  // --- 规则二：后台管理路由 ---
  {
    path: '/admin',
    component: AdminLayout,
    // 【修改】启用登录保护
    meta: { requiresAuth: true },
    children: [
      // 当访问 /admin 时，自动跳转到活动管理页
      { path: '', redirect: '/admin/activities' },
      {
        path: 'activities',
        name: 'AdminActivityManagement',
        component: ActivityManagement
      }
      // 未来可以添加更多后台管理页面...
    ]
  },

  // --- 规则三：独立的管理员登录页 ---
  // 这个页面不使用任何布局，是单独显示的
  {
    path: '/admin/login',
    name: 'AdminLogin',
    component: AuthView,
    props: { mode: 'admin' }
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// 【修改】添加并启用“路由守卫”，用于检查用户是否登录
router.beforeEach((to, from, next) => {
  // 在守卫中可以安全地使用 Pinia store
  const userStore = useUserStore();

  const isTryingToAccessProtectedRoute = to.meta.requiresAuth;
  const isLoggedIn = userStore.isLoggedIn;

  // 如果目标路由需要认证，但用户未登录
  if (isTryingToAccessProtectedRoute && !isLoggedIn) {
    alert('此页面需要登录后才能访问！');

    // 判断应该跳转到哪个登录页
    if (to.path.startsWith('/admin')) {
      // 如果是未登录访问后台，则跳转到后台登录页
      next({ name: 'AdminLogin' });
    } else {
      // 否则，跳转到前台普通用户登录页
      next({ name: 'Login' });
    }
  } else {
    // 如果不需要认证，或用户已登录，则直接放行
    next();
  }
});

export default router;