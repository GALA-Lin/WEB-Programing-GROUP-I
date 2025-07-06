<template>
  <div class="home-page">
    <div class="background-shapes">
      <div class="shape shape-1"></div>
      <div class="shape shape-2"></div>
    </div>

    <header class="hero-section">
      <video autoplay muted loop playsinline class="hero-video-background">
        <source src="/1803239b1f9720a4cd36336320f4f6d6_raw.mp4" type="video/mp4">
        Your browser does not support the video tag.
      </video>
      <div class="hero-video-overlay"></div>

      <div class="container hero-content">
        <h1 class="hero-title">让志愿行动，改变世界</h1>
        <p class="hero-subtitle">发现、参与并记录您的每一次善举。油炬智愿，点亮您与社区的光。</p>
        <div class="hero-actions">
          <router-link to="/activities" class="btn btn-primary btn-lg">探索活动</router-link>
          <router-link to="/login" class="btn btn-secondary btn-lg" v-if="!isLoggedIn">成为志愿者</router-link>
        </div>
      </div>
    </header>

    <section class="impact-section">
      <div class="container">
        <div class="impact-grid">
          <div class="impact-item">
            <div class="impact-icon"><User /></div>
            <div class="impact-text">
              <span class="impact-number">{{ animatedStats.volunteers }}</span>
              <span class="impact-label">注册志愿者</span>
            </div>
          </div>
          <div class="impact-item">
            <div class="impact-icon"><Flag /></div>
            <div class="impact-text">
              <span class="impact-number">{{ animatedStats.activities }}</span>
              <span class="impact-label">已举办活动</span>
            </div>
          </div>
          <div class="impact-item">
            <div class="impact-icon"><Timer /></div>
            <div class="impact-text">
              <span class="impact-number">{{ animatedStats.hours.toFixed(1) }}</span>
              <span class="impact-label">累计服务时长</span>
            </div>
          </div>
          <div class="impact-item">
            <div class="impact-icon"><OfficeBuilding /></div>
            <div class="impact-text">
              <span class="impact-number">{{ animatedStats.organizations }}</span>
              <span class="impact-label">合作组织</span>
            </div>
          </div>
        </div>
      </div>
    </section>

    <section class="featured-activities-section">
      <div class="container">
        <div class="section-header">
          <h2 class="section-title">近期热门活动</h2>
          <router-link to="/activities" class="section-link">查看全部 &rarr;</router-link>
        </div>
      </div>
      <div class="carousel-container">
        <div class="horizontal-scroll-container">
          <div class="activity-card" v-for="(activity, index) in doubledActivities" :key="`${activity.id}-${index}`">
            <router-link :to="`/activities/${activity.id}`" class="card-link">
              <div class="card-image-wrapper">
                <img :src="activity.coverImageUrl || 'data:image/svg+xml,%3Csvg xmlns=%22http://www.w3.org/2000/svg%22 width=%22400%22 height=%22220%22%3E%3Crect width=%22100%25%22 height=%22100%25%22 fill=%22%23e2e8f0%22/%3E%3C/svg%3E'" :alt="activity.title" class="activity-image"/>
                <span class="category-tag">{{ activity.category }}</span>
              </div>
              <div class="card-content">
                <h3 class="card-title">{{ activity.title }}</h3>
                <p class="card-location">📍 {{ activity.location }}</p>
              </div>
            </router-link>
          </div>
        </div>
      </div>
    </section>

    <!-- 【新增】近期新闻板块 -->
    <section class="recent-news-section">
      <div class="container">
        <div class="section-header">
          <h2 class="section-title">近期新闻</h2>
          <router-link to="/news" class="section-link">查看全部 &rarr;</router-link>
        </div>
        <div class="news-grid">
          <div class="news-card" v-for="newsItem in recentNews" :key="newsItem.id">
            <router-link :to="`/news/${newsItem.id}`" class="card-link">
              <div class="news-card-content">
                <h3 class="card-title">{{ newsItem.title }}</h3>
                <p class="card-summary">{{ newsItem.summary }}</p>
                <span class="card-meta">{{ newsItem.publishedAt }}</span>
              </div>
            </router-link>
          </div>
        </div>
      </div>
    </section>

    <!-- 【新增】AI聊天框组件 -->
    <AIChatBox />
  </div>
</template>

<script setup>
// 【变更】合并了所有 imports
import { ref, reactive, onMounted, computed } from 'vue';
import { useUserStore } from '@/stores/userStore.js';
import { getPublicActivities } from '@/services/publicActivityApi.js';
import { getPublicDashboardStats } from '@/services/dashboardApi.js';
import { getNewsList } from '@/services/newsApi.js'; // 新增
import { gsap } from 'gsap';
import { User, Flag, Timer, OfficeBuilding } from '@element-plus/icons-vue';
import AIChatBox from '@/components/AIChatBox.vue'; // 新增

const userStore = useUserStore();
const { isLoggedIn } = userStore;

const recentActivities = ref([]);
const recentNews = ref([]); // 新增
const animatedStats = reactive({ volunteers: 0, activities: 0, hours: 0, organizations: 0 });

// 【变更】保留了新版的无限滚动效果
const doubledActivities = computed(() => {
  return recentActivities.value.length > 0
      ? [...recentActivities.value, ...recentActivities.value]
      : [];
});

// 【变更】合并了 onMounted 钩子
onMounted(() => {
  fetchRecentActivities();
  fetchRecentNews(); // 新增
  fetchDashboardStatsAndAnimate();
});

const fetchRecentActivities = async () => {
  try {
    const response = await getPublicActivities(1, 8);
    if (response && response.list) {
      recentActivities.value = response.list;
    }
  } catch (err) {
    console.error('获取热门活动失败:', err);
  }
};

// 【新增】获取近期新闻的函数
const fetchRecentNews = async () => {
  try {
    const response = await getNewsList(1, 4);
    if (response && response.list) {
      recentNews.value = response.list;
    }
  } catch (err) {
    console.error('获取近期新闻失败:', err);
  }
};

const fetchDashboardStatsAndAnimate = async () => {
  try {
    const realStats = await getPublicDashboardStats();
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
  }
};
</script>

<style scoped>
/* 【新增】全局样式变量 */
:root {
  --color-primary: #1e88e5;
  --color-primary-soft: #e3f2fd;
  --color-secondary: #6b7280;
  --color-background: #ffffff;
  --color-background-soft: #f9fafb;
  --color-surface: #ffffff;
  --color-border: #e5e7eb;
  --color-text-heading: #1f2937;
  --color-text-body: #374151;
  --color-text-muted: #6b7280;
}

.home-page { position: relative; overflow-x: hidden; background-color: var(--color-background); }
.background-shapes { position: absolute; top: 0; left: 0; width: 100%; height: 100%; overflow: hidden; z-index: 0; }
.shape { position: absolute; border-radius: 50%; opacity: 0.1; filter: blur(80px); }
.shape-1 { width: 400px; height: 400px; background-color: var(--color-primary); top: -100px; left: -100px; }
.shape-2 { width: 500px; height: 500px; background-color: #8b5cf6; bottom: -150px; right: -150px; }
.container { max-width: 1200px; margin: 0 auto; padding: 0 20px; position: relative; z-index: 2; }

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: baseline;
  text-align: left;
  margin-bottom: 2rem;
}
.section-title { font-size: 2.25rem; font-weight: 800; color: var(--color-text-heading); }
.section-link { color: var(--color-primary); font-weight: 500; text-decoration: none; }
.section-link:hover { text-decoration: underline; }

.hero-section { position: relative; width: 100%; height: 75vh; min-height: 500px; overflow: hidden; display: flex; align-items: center; justify-content: center; }
.hero-video-background { position: absolute; top: 50%; left: 50%; transform: translateX(-50%) translateY(-50%) scale(1.25); min-width: 100%; min-height: 100%; width: auto; height: auto; z-index: 1; }
.hero-video-overlay { position: absolute; top: 0; left: 0; width: 100%; height: 100%; background-color: rgba(0, 0, 0, 0.4); z-index: 2; }
.hero-content { position: relative; z-index: 3; color: #fff; text-align: center; }
.hero-title { font-size: 3.5rem; font-weight: 800; line-height: 1.2; text-shadow: 0 2px 8px rgba(0,0,0,0.5); }
.hero-subtitle { font-size: 1.25rem; max-width: 600px; margin: 1.5rem auto 2.5rem; color: #e5e7eb; text-shadow: 0 1px 4px rgba(0,0,0,0.5); }
.hero-actions { display: flex; justify-content: center; gap: 1rem; }

/* 【新增】按钮样式 */
.btn { display: inline-flex; align-items: center; padding: 0.75rem 1.5rem; border-radius: 8px; font-weight: 600; text-decoration: none; transition: all 0.2s ease; border: none; cursor: pointer; }
.btn-primary { background-color: var(--color-primary); color: white; }
.btn-primary:hover { background-color: #1565c0; transform: translateY(-2px); }
.btn-secondary { background-color: transparent; color: white; border: 2px solid white; }
.btn-secondary:hover { background-color: white; color: var(--color-primary); }
.btn-lg { padding: 1rem 2rem; font-size: 1.125rem; }

.impact-section { background-color: transparent; padding: 6rem 0; }
.impact-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); gap: 1.5rem; background: rgba(255, 255, 255, 0.5); backdrop-filter: blur(10px); padding: 2rem; border-radius: 16px; border: 1px solid rgba(255, 255, 255, 0.7); }
.impact-item { display: flex; align-items: center; gap: 1rem; }
.impact-icon { flex-shrink: 0; width: 50px; height: 50px; display: flex; align-items: center; justify-content: center; background-color: var(--color-primary-soft); color: var(--color-primary); border-radius: 50%; font-size: 1.5rem; }
.impact-text { display: flex; flex-direction: column; }
.impact-number { font-size: 2rem; font-weight: 700; color: var(--color-text-heading); }
.impact-label { font-size: 0.9rem; color: var(--color-text-muted); }

/* 【变更】保留并应用新版的无限滚动效果 */
.featured-activities-section {
  padding: 4rem 0 6rem;
}

@keyframes infinite-scroll {
  from { transform: translateX(0); }
  to { transform: translateX(-50%); }
}

.carousel-container {
  overflow: hidden;
  -webkit-mask-image: linear-gradient(to right, transparent, black 10%, black 90%, transparent);
  mask-image: linear-gradient(to right, transparent, black 10%, black 90%, transparent);
}

.horizontal-scroll-container {
  display: flex;
  width: fit-content;
  gap: 1.5rem;
  padding: 1rem 0;
  animation: infinite-scroll 40s linear infinite;
}

.carousel-container:hover .horizontal-scroll-container {
  animation-play-state: paused;
}

.activity-card {
  flex: 0 0 320px;
}
.card-link { text-decoration: none; color: inherit; display: block; border-radius: 12px; overflow: hidden; background: var(--color-surface); box-shadow: 0 4px 6px -1px rgba(0,0,0,0.05); transition: transform 0.2s ease, box-shadow 0.2s ease; }
.card-link:hover { transform: translateY(-5px); box-shadow: 0 10px 15px -3px rgba(0,0,0,0.1); }
.card-image-wrapper { position: relative; }
.activity-image { width: 100%; height: 180px; object-fit: cover; }
.category-tag { position: absolute; top: 1rem; left: 1rem; background-color: rgba(255,255,255,0.8); backdrop-filter: blur(4px); color: var(--color-text-heading); padding: 0.25rem 0.75rem; border-radius: 99px; font-size: 0.75rem; font-weight: 600; }
.activity-card .card-content { padding: 1rem; }
.activity-card .card-title { font-size: 1.1rem; font-weight: 600; margin: 0 0 0.25rem; }
.card-location { font-size: 0.9rem; color: var(--color-text-muted); }

/* 【新增】近期新闻板块样式 */
.recent-news-section {
  padding: 4rem 0 6rem;
  background-color: var(--color-background-soft);
}

.news-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
  gap: 1.5rem;
}

.news-card .card-link {
  background: var(--color-surface);
  border: 1px solid var(--color-border);
}

.news-card-content {
  padding: 1.5rem;
  display: flex;
  flex-direction: column;
  height: 100%;
}

.news-card .card-title {
  font-size: 1.1rem;
  font-weight: 600;
  line-height: 1.5;
  margin-bottom: 1rem;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  height: calc(1.1rem * 1.5 * 2);
}

.news-card .card-summary {
  font-size: 0.95rem;
  color: var(--color-text-body);
  line-height: 1.6;
  margin-bottom: 1rem;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
  height: calc(0.95rem * 1.6 * 3);
  flex-grow: 1;
}

.news-card .card-meta {
  margin-top: auto;
  padding-top: 1rem;
  border-top: 1px solid var(--color-border);
  font-size: 0.85rem;
  color: var(--color-text-muted);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .hero-title { font-size: 2.5rem; }
  .hero-subtitle { font-size: 1.1rem; }
  .hero-actions { flex-direction: column; align-items: center; }
  .impact-grid { grid-template-columns: 1fr; gap: 1rem; padding: 1.5rem; }
  .section-header { flex-direction: column; gap: 1rem; text-align: center; }
  .activity-card { flex: 0 0 280px; }
  .news-grid { grid-template-columns: 1fr; }
}

@media (max-width: 480px) {
  .hero-title { font-size: 2rem; }
  .section-title { font-size: 1.875rem; }
  .container { padding: 0 15px; }
  .activity-card { flex: 0 0 260px; }
}
</style>
