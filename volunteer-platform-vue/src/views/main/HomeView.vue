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

  </div>
</template>

<script setup>
// 【新增】导入 computed
import { ref, reactive, onMounted, computed } from 'vue';
import { useUserStore } from '@/stores/userStore.js';
import { getPublicActivities } from '@/services/publicActivityApi.js';
import { getPublicDashboardStats } from '@/services/dashboardApi.js';
import { gsap } from 'gsap';
import { User, Flag, Timer, OfficeBuilding } from '@element-plus/icons-vue';

const userStore = useUserStore();
const { isLoggedIn } = userStore;

const recentActivities = ref([]);
const animatedStats = reactive({ volunteers: 0, activities: 0, hours: 0, organizations: 0 });

// 【新增】创建双倍列表以实现无缝滚动
// 只有当有活动时，才创建双倍列表
const doubledActivities = computed(() => {
  return recentActivities.value.length > 0
      ? [...recentActivities.value, ...recentActivities.value]
      : [];
});

onMounted(() => {
  fetchRecentActivities();
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
/* 省略了大部分不变的样式... */
.home-page { position: relative; overflow-x: hidden; background-color: var(--color-background); }
.background-shapes { position: absolute; top: 0; left: 0; width: 100%; height: 100%; overflow: hidden; z-index: 0; }
.shape { position: absolute; border-radius: 50%; opacity: 0.1; filter: blur(80px); }
.shape-1 { width: 400px; height: 400px; background-color: var(--color-primary); top: -100px; left: -100px; }
.shape-2 { width: 500px; height: 500px; background-color: #8b5cf6; bottom: -150px; right: -150px; }
.container { max-width: 1200px; margin: 0 auto; padding: 0 20px; position: relative; z-index: 2; }
.section-header { display: flex; justify-content: space-between; align-items: baseline; text-align: left; margin-bottom: 2rem; }
.section-title { font-size: 2.25rem; font-weight: 800; color: var(--color-text-heading); }
.section-link { color: var(--color-primary); font-weight: 500; text-decoration: none; }
.hero-section { position: relative; width: 100%; height: 75vh; min-height: 500px; overflow: hidden; display: flex; align-items: center; justify-content: center; }
.hero-video-background { position: absolute; top: 50%; left: 50%; transform: translateX(-50%) translateY(-50%) scale(1.25); min-width: 100%; min-height: 100%; width: auto; height: auto; z-index: 1; }
.hero-video-overlay { position: absolute; top: 0; left: 0; width: 100%; height: 100%; background-color: rgba(0, 0, 0, 0.4); z-index: 2; }
.hero-content { position: relative; z-index: 3; color: #fff; text-align: center; }
.hero-title { font-size: 3.5rem; font-weight: 800; line-height: 1.2; text-shadow: 0 2px 8px rgba(0,0,0,0.5); }
.hero-subtitle { font-size: 1.25rem; max-width: 600px; margin: 1.5rem auto 2.5rem; color: #e5e7eb; text-shadow: 0 1px 4px rgba(0,0,0,0.5); }
.hero-actions { display: flex; justify-content: center; gap: 1rem; }
.impact-section { background-color: transparent; padding: 6rem 0; }
.impact-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); gap: 1.5rem; background: rgba(255, 255, 255, 0.5); backdrop-filter: blur(10px); padding: 2rem; border-radius: 16px; border: 1px solid rgba(255, 255, 255, 0.7); }
.impact-item { display: flex; align-items: center; gap: 1rem; }
.impact-icon { flex-shrink: 0; width: 50px; height: 50px; display: flex; align-items: center; justify-content: center; background-color: var(--color-primary-soft); color: var(--color-primary); border-radius: 50%; font-size: 1.5rem; }
.impact-text { display: flex; flex-direction: column; }
.impact-number { font-size: 2rem; font-weight: 700; color: var(--color-text-heading); }
.impact-label { font-size: 0.9rem; color: var(--color-text-muted); }

/* ▼▼▼【热门活动 - 自动滚动播放效果】▼▼▼ */
.featured-activities-section {
  padding: 4rem 0 6rem;
}

/* 【新增】定义滚动动画 */
@keyframes infinite-scroll {
  from { transform: translateX(0); }
  /* 移动距离为列表总宽度的一半（因为我们复制了一份） */
  to { transform: translateX(-50%); }
}

/* 【新增】外部容器，用于隐藏滚动条和溢出的内容 */
.carousel-container {
  overflow: hidden;
  /* 添加左右遮罩效果，让滚动看起来更自然 */
  -webkit-mask-image: linear-gradient(to right, transparent, black 10%, black 90%, transparent);
  mask-image: linear-gradient(to right, transparent, black 10%, black 90%, transparent);
}

/* 【变更】滚动容器应用动画 */
.horizontal-scroll-container {
  display: flex;
  width: fit-content; /* 让容器宽度等于所有卡片宽度之和 */
  gap: 1.5rem;
  padding: 1rem 0;
  /* 应用动画：动画名 动画时长 速度曲线 无限循环 */
  /* 时长可以根据卡片数量和宽度调整，比如 8 张卡片 * 5s/张 = 40s */
  animation: infinite-scroll 40s linear infinite;
}

/* 【新增】鼠标悬停时暂停动画 */
.carousel-container:hover .horizontal-scroll-container {
  animation-play-state: paused;
}

.activity-card {
  flex: 0 0 320px; /* 固定卡片宽度，防止缩放 */
}
.card-link { text-decoration: none; color: inherit; display: block; border-radius: 12px; overflow: hidden; background: var(--color-surface); box-shadow: 0 4px 6px -1px rgba(0,0,0,0.05); transition: transform 0.2s ease, box-shadow 0.2s ease; }
.card-link:hover { transform: translateY(-5px); box-shadow: 0 10px 15px -3px rgba(0,0,0,0.1); }
.card-image-wrapper { position: relative; }
.activity-image { width: 100%; height: 180px; object-fit: cover; }
.category-tag { position: absolute; top: 1rem; left: 1rem; background-color: rgba(255,255,255,0.8); backdrop-filter: blur(4px); color: var(--color-text-heading); padding: 0.25rem 0.75rem; border-radius: 99px; font-size: 0.75rem; font-weight: 600; }
.activity-card .card-content { padding: 1rem; }
.activity-card .card-title { font-size: 1.1rem; font-weight: 600; margin: 0 0 0.25rem; }
.card-location { font-size: 0.9rem; color: var(--color-text-muted); }

</style>