import { createRouter, createWebHistory } from 'vue-router';
import { useUserStore } from '@/stores/userStore.js';
import { useTagsViewStore } from '@/stores/tagsView.js'; // 导入 tagsView store
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
      { path: '', name: 'Home', component: HomeView, meta: { title: '首页' } },
      { path: 'activities', name: 'Activities', component: ActivitiesView, meta: { title: '活动列表' } },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('@/views/main/ProfileView.vue'),
        meta: { requiresAuth: true, title: '个人中心' }
      },
      { path: 'news', name: 'NewsList', component: NewsView, meta: { title: '新闻资讯' } },
      { path: 'news/:id', name: 'NewsDetail', component: NewsDetailView, meta: { title: '新闻详情' } },
      {
        path: 'organizations',
        name: 'OrganizationList',
        component: () => import('@/views/main/OrganizationsView.vue'),
        meta: { requiresAuth: true, title: '组织列表' }
      },
      {
        path: 'organizations/:id',
        name: 'OrganizationDetail',
        component: () => import('@/views/main/OrganizationDetailView.vue'),
        meta: { requiresAuth: true, title: '组织详情' }
      }
    ]
  },
  {
    path: '/login',
    name: 'Login',
    component: AuthView,
    props: { mode: 'user' },
    meta: { title: '登录' }
  },

  // --- 后台管理路由 ---
  {
    path: '/admin',
    component: AdminLayout,
    meta: { requiresAuth: true, requiresAdmin: true },
    children: [
      { path: '', redirect: '/admin/dashboard' },
      {
        path: 'dashboard',
        name: 'AdminDashboard',
        component: () => import('@/views/admin/DashboardView.vue'),
        meta: { title: '数据看板', affix: true } // affix: true 表示标签不可关闭
      },
      {
        path: 'activities',
        name: 'AdminActivityManagement',
        component: ActivityManagement,
        meta: { title: '活动管理' }
      },
      {
        path: 'activities/:id/records', // 使用嵌套路由，清晰地表示它属于某个活动
        name: 'AdminServiceRecordManagement',
        component: () => import('@/views/admin/ServiceRecordManagement.vue'),
        meta: { title: '活动时长管理' } // title 用于面包屑导航
      },
      {
        path: 'users',
        name: 'AdminUserManagement',
        component: () => import('@/views/admin/UserManagement.vue'),
        meta: { title: '用户管理' }
      },
      {
        path: 'news',
        name: 'AdminNewsManagement',
        component: () => import('@/views/admin/NewsManagement.vue'),
        meta: { title: '新闻管理' }
      },
      {
        path: 'organizations',
        name: 'AdminOrganizationManagement',
        component: () => import('@/views/admin/OrganizationManagement.vue'),
        meta: { title: '组织管理' }
      }
    ]
  },
  {
    path: '/admin/login',
    name: 'AdminLogin',
    component: AuthView,
    props: { mode: 'admin' },
    meta: { title: '后台登录' }
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// 在 beforeEach 之外定义，确保它只被初始化一次
let tagsViewStoreInstance = null;
let userStoreInstance = null;

router.beforeEach((to, from, next) => {
  // 仅在第一次导航时初始化 stores
  if (!userStoreInstance) {
    userStoreInstance = useUserStore();
  }
  if (!tagsViewStoreInstance) {
    tagsViewStoreInstance = useTagsViewStore();
  }

  // 登录和权限检查逻辑
  if (to.meta.requiresAuth && !userStoreInstance.isLoggedIn) {
    alert('此页面需要登录后才能访问！');
    if (to.path.startsWith('/admin')) {
      return next({ name: 'AdminLogin' });
    }
    return next({ name: 'Login' });
  }

  if (to.meta.requiresAdmin && !userStoreInstance.isAdmin) {
    alert('权限不足！您不是管理员，无法访问此页面。');
    return next(from.name ? from : '/');
  }

  next();
});

router.afterEach((to) => {
  // 【修改点】在这里添加了对路由名称的判断
  if (
      tagsViewStoreInstance &&
      to.meta.title &&
      to.path.startsWith('/admin') &&
      to.name !== 'AdminLogin' // 排除名为 'AdminLogin' 的路由
  ) {
    tagsViewStoreInstance.addView(to);
  }
})

export default router;