// src/router/index.js
import { createRouter, createWebHistory } from 'vue-router';
import { useUserStore } from '@/stores/userStore.js';
import MainLayout from '@/layouts/MainLayout.vue';
import AdminLayout from '@/layouts/AdminLayout.vue';
import HomeView from '@/views/main/HomeView.vue';
import ActivitiesView from '@/views/main/ActivitiesView.vue';
import AuthView from '@/views/main/AuthView.vue';
import ActivityManagement from '@/views/admin/ActivityManagement.vue';
import NewsView from '@/views/main/NewsView.vue';
import NewsDetailView from '@/views/main/NewsDetailView.vue';


const routes = [
  // --- 前台页面路由 ---
  {
    path: '/',
    component: MainLayout,
    children: [
      { path: '', name: 'Home', component: HomeView },
      { path: 'activities', name: 'Activities', component: ActivitiesView },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('@/views/main/ProfileView.vue'),
        meta: { requiresAuth: true }
      },
      { path: 'news', name: 'NewsList', component: NewsView },
      { path: 'news/:id', name: 'NewsDetail', component: NewsDetailView },
      {
        path: 'organizations',
        name: 'OrganizationList',
        component: () => import('@/views/main/OrganizationsView.vue'),
        meta: { requiresAuth: true }
      },
      {
        path: 'organizations/:id',
        name: 'OrganizationDetail',
        component: () => import('@/views/main/OrganizationDetailView.vue'),
        meta: { requiresAuth: true }
      }
    ]
  },
  {
    path: '/login',
    name: 'Login',
    component: AuthView,
    props: { mode: 'user' }
  },

  // --- 后台管理路由 ---
  {
    path: '/admin',
    component: AdminLayout,
    meta: { requiresAuth: true, requiresAdmin: true },
    children: [
      { path: '', redirect: '/admin/activities' },
      {
        path: 'activities',
        name: 'AdminActivityManagement',
        component: ActivityManagement
      },
      {
        path: 'users',
        name: 'AdminUserManagement',
        component: () => import('@/views/admin/UserManagement.vue')
      },
      {
        path: 'news',
        name: 'AdminNewsManagement',
        component: () => import('@/views/admin/NewsManagement.vue')
      },
      {
        path: 'organizations',
        name: 'AdminOrganizationManagement',
        component: () => import('@/views/admin/OrganizationManagement.vue')
      }
    ]
  },
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

// --- ↓↓↓ 核心修改点2：升级路由守卫逻辑 ↓↓↓ ---
router.beforeEach((to, from, next) => {
  const userStore = useUserStore();

  // 检查目标路由是否需要登录
  if (to.meta.requiresAuth && !userStore.isLoggedIn) {
    alert('此页面需要登录后才能访问！');
    if (to.path.startsWith('/admin')) {
      return next({ name: 'AdminLogin' });
    }
    return next({ name: 'Login' });
  }

  // 检查目标路由是否需要管理员权限
  if (to.meta.requiresAdmin && !userStore.isAdmin) {
    alert('权限不足！您不是管理员，无法访问此页面。');
    // 从一个受保护的页面跳转，通常返回上一页或主页
    if(from.name) {
      return next(from);
    }
    return next('/');
  }

  // 如果所有检查都通过，则放行
  next();
});

export default router;