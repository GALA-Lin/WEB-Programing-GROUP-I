import { createRouter, createWebHistory } from 'vue-router';
import HomeView from '@/views/HomeView.vue';
import ActivitiesView from '@/views/ActivitiesView.vue';


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/activities',
      name: 'activities',
      component: ActivitiesView
    }
  ]
});

export default router;
    