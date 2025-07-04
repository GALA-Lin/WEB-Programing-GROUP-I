<template>
  <div class="home-view">
    <section class="hero-section">
      <video autoplay muted loop playsinline class="hero-video-background">
        <source src="https://videos.pexels.com/video-files/3209828/3209828-hd_1920_1080_25fps.mp4" type="video/mp4">
        Your browser does not support the video tag.
      </video>
      <div class="hero-overlay"></div>
      <div class="container hero-content">
        <h1 class="hero-title">让志愿行动，改变世界</h1>
        <p class="hero-subtitle">发现、参与并记录您的每一次善举。油炬智愿，点亮您与社区的光。</p>
        <div class="hero-actions">
          <router-link to="/activities" class="btn btn-primary btn-lg">探索活动</router-link>
          <router-link v-if="!isLoggedIn" to="/login" class="btn btn-outline-light btn-lg">成为志愿者</router-link>
        </div>
      </div>
    </section>

    <section class="impact-section">
      <div class="container">
        <div class="impact-grid">
          <div class="impact-item">
            <h2 class="impact-number">{{ animatedStats.volunteers }}</h2>
            <p class="impact-label">注册志愿者</p>
          </div>
          <div class="impact-item">
            <h2 class="impact-number">{{ animatedStats.activities }}</h2>
            <p class="impact-label">已举办活动</p>
          </div>
          <div class="impact-item">
            <h2 class="impact-number">{{ animatedStats.hours.toFixed(1) }}</h2>
            <p class="impact-label">累计服务时长 (小时)</p>
          </div>
          <div class="impact-item">
            <h2 class="impact-number">{{ animatedStats.organizations }}</h2>
            <p class="impact-label">合作组织</p>
          </div>
        </div>
      </div>
    </section>

    <section class="featured-activities-section">
      <div class="container">
        <div class="section-header">
          <h2 class="section-title">热门活动</h2>
          <router-link to="/activities" class="section-link">查看全部 &rarr;</router-link>
        </div>
        <div class="carousel-wrapper">
          <div class="activities-carousel" :style="{ transform: `translateX(-${currentSlide * 100}%)` }">
            <div class="activity-slide" v-for="activity in recentActivities" :key="activity.id">
              <div class="activity-card">
                <img :src="activity.coverImageUrl || 'data:image/svg+xml,%3Csvg xmlns=%22http://www.w3.org/2000/svg%22 width=%22400%22 height=%22220%22%3E%3Crect width=%22100%25%22 height=%22100%25%22 fill=%22%23e2e8f0%22/%3E%3C/svg%3E'" alt="活动图片" class="activity-image">
                <div class="card-content">
                  <span class="category-tag">{{ getCategoryName(activity.category) }}</span>
                  <h3 class="card-title">{{ activity.title }}</h3>
                  <div class="card-meta">
                    <span>📍 {{ activity.location }}</span>
                  </div>
                  <router-link :to="`/activities/${activity.id}`" class="btn btn-primary-outline">活动详情</router-link>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="carousel-dots">
          <button v-for="(slide, index) in recentActivities" :key="index" @click="currentSlide = index" :class="{ active: currentSlide === index }"></button>
        </div>
      </div>
    </section>

    <section class="spotlight-section">
      <div class="container spotlight-content">
        <div class="spotlight-image">
          <img src="https://images.unsplash.com/photo-1582213782179-e0d53f98f2ca?q=80&w=800" alt="Volunteer Spotlight">
        </div>
        <div class="spotlight-text">
          <h2 class="section-title">志愿者风采</h2>
          <blockquote class="spotlight-quote">
            “通过‘油炬智愿’，我不仅找到了有意义的活动，更结识了一群志同道合的朋友。每一次付出，都让我感受到了社区的温暖和自己的价值。”
          </blockquote>
          <p class="spotlight-author">- 张三, 信息工程学院</p>
          <router-link to="/register" class="btn btn-secondary">加入我们</router-link>
        </div>
      </div>
    </section>

  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted } from 'vue';
import { useUserStore } from '@/stores/userStore.js';
import { getPublicActivities } from '@/services/publicActivityApi.js';
import { getDashboardStats } from '@/services/dashboardApi.js';
import { gsap } from 'gsap';

const userStore = useUserStore();
const { isLoggedIn } = userStore;

const recentActivities = ref([]);
const loading = ref(true);
const error = ref(null);
const currentSlide = ref(0);

const animatedStats = reactive({ volunteers: 0, activities: 0, hours: 0, organizations: 0 });

let observer;
onMounted(() => {
  fetchRecentActivities();

  const impactSection = document.querySelector('.impact-section');
  observer = new IntersectionObserver((entries) => {
    if (entries[0].isIntersecting) {
      fetchDashboardStatsAndAnimate();
      observer.disconnect();
    }
  }, { threshold: 0.5 });

  if(impactSection) observer.observe(impactSection);

  const interval = setInterval(() => {
    if(recentActivities.value.length > 0) {
      currentSlide.value = (currentSlide.value + 1) % recentActivities.value.length;
    }
  }, 5000);

  onUnmounted(() => {
    clearInterval(interval);
    if(observer) observer.disconnect();
  });
});

const fetchDashboardStatsAndAnimate = async () => {
  try {
    const realStats = await getDashboardStats();
    gsap.to(animatedStats, {
      duration: 2,
      volunteers: realStats.totalUsers || 0,
      activities: realStats.totalActivities || 0,
      hours: realStats.totalServiceHours || 0,
      organizations: realStats.totalOrganizations || 0,
      ease: 'power1.inOut',
      onUpdate: () => {
        animatedStats.volunteers = Math.round(animatedStats.volunteers);
        animatedStats.activities = Math.round(animatedStats.activities);
        animatedStats.organizations = Math.round(animatedStats.organizations);
      }
    });
  } catch (e) {
    console.error("获取统计数据失败:", e);
    animatedStats.volunteers = 100;
    animatedStats.activities = 50;
    animatedStats.hours = 2000;
    animatedStats.organizations = 10;
  }
};

const fetchRecentActivities = async () => {
  try {
    loading.value = true;
    error.value = null;
    const response = await getPublicActivities(1, 6);
    if (response && response.list) {
      recentActivities.value = response.list;
    } else {
      error.value = '无法加载活动数据。';
    }
  } catch (err) {
    console.error('获取热门活动失败:', err);
    error.value = '加载热门活动失败，请稍后再试。';
  } finally {
    loading.value = false;
  }
};

const getCategoryName = (category) => {
  const categories = { '校内服务': '校内服务', '社区服务': '社区服务', '环保': '环保', '教育': '教育' };
  return categories[category] || category || '综合';
};
</script>


<style scoped>
/* General */
.home-view { overflow-x: hidden; }
.container { max-width: 1200px; margin: 0 auto; padding: 0 20px; }
.section-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 2.5rem; }
.section-title { font-size: 2.25rem; font-weight: 700; color: var(--color-text-heading); }
.section-link { color: var(--color-primary); font-weight: 500; text-decoration: none; }
.btn { padding: 0.75rem 1.5rem; border-radius: 8px; font-weight: 600; transition: all 0.2s; text-decoration: none; border: 1px solid transparent; cursor: pointer; }
.btn-lg { padding: 1rem 2rem; font-size: 1.1rem; }
.btn-primary { background-color: var(--color-primary); color: white; }
.btn-primary:hover { background-color: var(--color-primary-hover); transform: translateY(-2px); }
.btn-secondary { background-color: #f97316; color: white; }
.btn-secondary:hover { background-color: #ea580c; }
.btn-outline-light { background-color: transparent; color: white; border-color: white; }
.btn-outline-light:hover { background-color: rgba(255,255,255,0.1); }
.btn-primary-outline { color: var(--color-primary); border-color: var(--color-primary); }
.btn-primary-outline:hover { background-color: var(--color-primary-soft); }

/* Hero Section - 调整了高度 */
.hero-section { position: relative; height: 60vh; min-height: 400px; display: flex; align-items: center; text-align: center; color: white; }
.hero-video-background { position: absolute; top: 0; left: 0; width: 100%; height: 100%; object-fit: cover; z-index: 1; }
.hero-overlay { position: absolute; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.5); z-index: 2; }
.hero-content { position: relative; z-index: 3; }
.hero-title { font-size: 3.5rem; font-weight: 800; text-shadow: 0 2px 8px rgba(0,0,0,0.6); }
.hero-subtitle { font-size: 1.25rem; max-width: 600px; margin: 1rem auto 2.5rem; color: #e5e7eb; }
.hero-actions { display: flex; justify-content: center; gap: 1rem; }

/* Impact Section */
.impact-section { padding: 4rem 1rem; background: var(--color-background-soft); }
.impact-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 2rem; }
.impact-item { text-align: center; }
.impact-number { font-size: 3rem; font-weight: 700; color: var(--color-primary); }
.impact-label { font-size: 1rem; color: var(--color-text-muted); }

/* Featured Activities */
.featured-activities-section { padding: 5rem 1rem; }
.carousel-wrapper { overflow: hidden; }
.activities-carousel { display: flex; transition: transform 0.5s ease-in-out; }
.activity-slide { min-width: 100%; }
@media (min-width: 640px) { .activity-slide { min-width: 50%; } }
@media (min-width: 992px) { .activity-slide { min-width: 33.333%; } }
.activity-card { margin: 0 1rem; background: var(--color-surface); border-radius: 12px; overflow: hidden; box-shadow: 0 4px 20px rgba(0,0,0,0.08); display: flex; flex-direction: column; height: 100%;}
.activity-image { width: 100%; height: 220px; object-fit: cover; }
.card-content { padding: 1.5rem; display: flex; flex-direction: column; flex-grow: 1; }
.category-tag { display: inline-block; background-color: var(--color-primary-soft); color: var(--color-primary); padding: 0.25rem 0.75rem; border-radius: 99px; font-size: 0.8rem; font-weight: 500; margin-bottom: 0.75rem; align-self: flex-start; }
.card-title { font-size: 1.25rem; font-weight: 600; margin-bottom: 0.5rem; flex-grow: 1;}
.card-meta { color: var(--color-text-muted); margin-bottom: 1.5rem; }
.carousel-dots { text-align: center; margin-top: 2rem; }
.carousel-dots button { width: 10px; height: 10px; border-radius: 50%; border: none; background: #d1d5db; margin: 0 5px; cursor: pointer; transition: background 0.3s, transform 0.3s; }
.carousel-dots button.active { background: var(--color-primary); transform: scale(1.2); }

/* Spotlight Section */
.spotlight-section { padding: 5rem 1rem; background: var(--color-background-soft); }
.spotlight-content { display: grid; grid-template-columns: 1fr 1fr; align-items: center; gap: 4rem; }
.spotlight-image img { width: 100%; border-radius: 12px; box-shadow: 0 10px 30px rgba(0,0,0,0.1); }
.spotlight-quote { font-size: 1.5rem; font-style: italic; color: var(--color-text-heading); border-left: 4px solid var(--color-primary); padding-left: 1.5rem; margin: 2rem 0; }
.spotlight-author { font-weight: 600; margin-bottom: 2rem; }

@media (max-width: 992px) {
  .spotlight-content { grid-template-columns: 1fr; }
  .spotlight-image { order: 2; margin-top: 2rem; }
  .spotlight-text { order: 1; text-align: center; }
  .spotlight-quote { margin: 2rem auto; }
}
@media (max-width: 768px) {
  .hero-title { font-size: 2.5rem; }
  .hero-subtitle { font-size: 1.1rem; }
  .section-title { font-size: 1.8rem; }
}

</style>