<template>
  <div class="activities-page">
    <div class="page-header">
      <h1 class="page-title">探索精彩活动</h1>
      <p class="page-subtitle">发现并参与我们的志愿活动，与我们一起为社会贡献力量。</p>
    </div>

    <div class="container">
      <div class="filter-section">
        <div class="filter-controls">
          <select v-model="selectedCategory" class="filter-select">
            <option value="">全部分类</option>
            <option value="environment">环境保护</option>
            <option value="education">教育支持</option>
          </select>
          <input type="text" v-model="searchTerm" placeholder="搜索活动名称、地点..." class="filter-search">
          <button @click="searchActivities" class="btn btn-primary">搜索</button>
        </div>
      </div>

      <div v-if="loading" class="loading-state">正在加载活动...</div>
      <div v-if="error" class="error-state">{{ error }}</div>

      <div v-if="!loading && !error" class="activities-grid">
        <div class="activity-card" v-for="activity in paginatedActivities" :key="activity.id">
          <div class="activity-image-wrapper">
            <img :src="activity.image" :alt="activity.title" class="activity-image" />
            <span class="activity-category-tag">{{ getCategoryName(activity.category) }}</span>
          </div>
          <div class="activity-content">
            <h3 class="activity-title">
              <router-link :to="`/activities/${activity.id}`">{{ activity.title }}</router-link>
            </h3>
            <p class="activity-organizer">由 <strong>{{ activity.organizer.name }}</strong> 主办</p>

            <div class="activity-meta">
              <div class="meta-item">
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24"><path fill="currentColor" d="M12 2C6.486 2 2 6.486 2 12s4.486 10 10 10s10-4.486 10-10S17.514 2 12 2m0 18c-4.411 0-8-3.589-8-8s3.589-8 8-8s8 3.589 8 8s-3.589 8-8 8"/><path fill="currentColor" d="M13 7h-2v5.414l3.293 3.293l1.414-1.414L13 11.586z"/></svg>
                <span>{{ formatDate(activity.date) }}</span>
              </div>
              <div class="meta-item">
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24"><path fill="currentColor" d="M12 2C7.589 2 4 5.589 4 10c0 4.411 8 12 8 12s8-7.589 8-12c0-4.411-3.589-8-8-8m0 12c-2.206 0-4-1.794-4-4s1.794-4 4-4s4 1.794 4 4s-1.794 4-4 4"/></svg>
                <span>{{ activity.location }}</span>
              </div>
            </div>

            <div class="enrollment-progress">
              <div class="progress-bar">
                <div class="progress-bar-inner" :style="{ width: (activity.participants / activity.maxParticipants * 100) + '%' }"></div>
              </div>
              <div class="progress-text">
                <span>{{ activity.participants }} / {{ activity.maxParticipants }} 人</span>
              </div>
            </div>

            <div class="activity-actions">
              <router-link :to="`/activities/${activity.id}`" class="btn btn-primary-outline">查看详情</router-link>
            </div>
          </div>
        </div>
      </div>
      <div v-if="!loading && totalPages > 1" class="pagination">
        <button @click="currentPage--" :disabled="currentPage === 1" class="page-btn">上一页</button>
        <span class="page-info">{{ currentPage }} / {{ totalPages }}</span>
        <button @click="currentPage++" :disabled="currentPage === totalPages" class="page-btn">下一页</button>
      </div>
    </div>
  </div>
</template>

<script setup>
// 您的 <script setup> 部分保持不变，这里为了简洁省略了
// 我们只修改 <template> 和 <style> 部分
import { ref, computed, onMounted } from 'vue';

const selectedCategory = ref('');
const searchTerm = ref('');
const loading = ref(true);
const error = ref(null);
const activities = ref([]);
const currentPage = ref(1);
const itemsPerPage = ref(6); // 每页显示6个活动

// 模拟的活动数据 (与您项目中的保持一致)
const allActivities = [
  { id: 1, title: "城市公园清洁日", category: "environment", image: "https://images.unsplash.com/photo-1599059813005-72827a055398?q=80&w=800", date: "2025-10-15", location: "奥林匹克公园", duration: 4, participants: 25, maxParticipants: 50, description: "...", rating: 4.8, reviews: 124, organizer: { name: "绿色北京环保协会", avatar: "..." } },
  { id: 2, title: "山区小学支教活动", category: "education", image: "https://images.unsplash.com/photo-1521587760476-6c12a4b040da?q=80&w=800", date: "2025-10-20", location: "河北承德", duration: 10, participants: 12, maxParticipants: 20, description: "...", rating: 4.9, reviews: 87, organizer: { name: "爱心支教联盟", avatar: "..." } },
  { id: 3, title: "敬老院陪伴日", category: "elderly", image: "https://images.unsplash.com/photo-1555421689-491a97ff2040?q=80&w=800", date: "2025-10-16", location: "上海康桥镇敬老院", duration: 3, participants: 18, maxParticipants: 30, description: "...", rating: 4.7, reviews: 105, organizer: { name: "夕阳红关爱协会", avatar: "..." } },
  { id: 4, title: "自闭症儿童关爱", category: "children", image: "https://images.unsplash.com/photo-1512428209355-e51c243a4e5c?q=80&w=800", date: "2025-10-22", location: "广州特殊教育学校", duration: 5, participants: 24, maxParticipants: 25, description: "...", rating: 4.8, reviews: 93, organizer: { name: "星星点灯中心", avatar: "..." } },
  { id: 5, title: "社区环保宣传", category: "environment", image: "https://images.unsplash.com/photo-1576495199011-bde07d3b5336?q=80&w=800", date: "2025-10-18", location: "深圳科技园社区", duration: 4, participants: 38, maxParticipants: 40, description: "...", rating: 4.6, reviews: 78, organizer: { name: "绿色家园社区", avatar: "..." } },
  { id: 6, title: "图书捐赠整理", category: "education", image: "https://images.unsplash.com/photo-1481627834876-b7833e8f5570?q=80&w=800", date: "2025-10-25", location: "杭州西湖区图书馆", duration: 6, participants: 8, maxParticipants: 15, description: "...", rating: 4.5, reviews: 65, organizer: { name: "阅读点亮未来", avatar: "..." } }
];

const filteredActivities = computed(() => {
  return allActivities.filter(activity => {
    const categoryMatch = !selectedCategory.value || activity.category === selectedCategory.value;
    const searchMatch = !searchTerm.value ||
        activity.title.toLowerCase().includes(searchTerm.value.toLowerCase()) ||
        activity.location.toLowerCase().includes(searchTerm.value.toLowerCase());
    return categoryMatch && searchMatch;
  });
});

const totalPages = computed(() => {
  return Math.ceil(filteredActivities.value.length / itemsPerPage.value);
});

const paginatedActivities = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage.value;
  const end = start + itemsPerPage.value;
  return filteredActivities.value.slice(start, end);
});

const searchActivities = () => {
  currentPage.value = 1;
}

const formatDate = (dateString) => {
  return new Date(dateString).toLocaleDateString('zh-CN', { year: 'numeric', month: 'long', day: 'numeric' });
};

const getCategoryName = (category) => {
  const categories = {
    'environment': '环境保护', 'education': '教育支持', 'elderly': '关爱老人',
    'children': '关爱儿童', 'disaster': '灾害救援', 'animal': '动物保护',
    'sports': '体育赛事', 'culture': '文化传播'
  };
  return categories[category] || category;
};

onMounted(() => {
  setTimeout(() => {
    activities.value = allActivities;
    loading.value = false;
  }, 500);
});
</script>

<style scoped>
/* 样式使用了我们在 main.css 中定义的变量 */
.activities-page {
  background-color: var(--color-background);
  min-height: 100vh;
}

.page-header {
  text-align: center;
  padding: 4rem 1rem 3rem;
  background-color: var(--color-surface);
  border-bottom: 1px solid var(--color-border);
}

.page-title {
  font-size: 2.5rem;
  font-weight: 800;
  color: var(--color-text-heading);
  margin-bottom: 0.5rem;
}

.page-subtitle {
  font-size: 1.1rem;
  color: var(--color-text-muted);
  max-width: 600px;
  margin: 0 auto;
}

.container {
  padding: 2rem 20px;
}

/* 筛选区域 */
.filter-section {
  margin-bottom: 2rem;
}
.filter-controls {
  display: flex;
  gap: 1rem;
  flex-wrap: wrap;
}
.filter-select, .filter-search {
  padding: 0.75rem 1rem;
  border: 1px solid var(--color-border);
  border-radius: 8px;
  font-size: 1rem;
  background-color: var(--color-surface);
  flex-grow: 1;
}
.filter-select:focus, .filter-search:focus {
  outline: none;
  border-color: var(--color-primary);
  box-shadow: 0 0 0 3px var(--color-primary-soft);
}

/* 状态提示 */
.loading-state, .error-state {
  text-align: center;
  padding: 4rem;
  font-size: 1.2rem;
  color: var(--color-text-muted);
}
.error-state {
  color: #c53030;
  background-color: #fff5f5;
  border-radius: 8px;
}

/* 活动卡片网格 */
.activities-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 2rem;
}

.activity-card {
  background-color: var(--color-surface);
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05), 0 2px 4px -2px rgba(0, 0, 0, 0.05);
  transition: transform 0.2s ease-in-out, box-shadow 0.2s ease-in-out;
  display: flex;
  flex-direction: column;
}

.activity-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.07), 0 4px 6px -4px rgba(0, 0, 0, 0.07);
}

.activity-image-wrapper {
  position: relative;
  height: 200px;
}

.activity-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.activity-category-tag {
  position: absolute;
  top: 1rem;
  left: 1rem;
  background-color: rgba(0, 0, 0, 0.5);
  color: white;
  padding: 0.25rem 0.75rem;
  border-radius: 99px;
  font-size: 0.8rem;
  font-weight: 500;
}

.activity-content {
  padding: 1.5rem;
  display: flex;
  flex-direction: column;
  flex-grow: 1;
}

.activity-title {
  font-size: 1.25rem;
  font-weight: 700;
  color: var(--color-text-heading);
  margin: 0 0 0.25rem;
}

.activity-title a {
  color: inherit;
  text-decoration: none;
}
.activity-title a:hover {
  color: var(--color-primary);
}

.activity-organizer {
  font-size: 0.9rem;
  color: var(--color-text-muted);
  margin-bottom: 1rem;
}

.activity-meta {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
  margin-bottom: 1rem;
  font-size: 0.95rem;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: var(--color-text-body);
}

.meta-item svg {
  color: var(--color-primary);
  flex-shrink: 0;
}

/* 报名进度条 */
.enrollment-progress {
  margin: 1rem 0;
}
.progress-bar {
  width: 100%;
  height: 8px;
  background-color: var(--color-background-soft);
  border-radius: 4px;
  overflow: hidden;
}
.progress-bar-inner {
  height: 100%;
  background-color: var(--color-primary);
  border-radius: 4px;
  transition: width 0.5s ease;
}
.progress-text {
  text-align: right;
  font-size: 0.8rem;
  color: var(--color-text-muted);
  margin-top: 0.25rem;
}

.activity-actions {
  margin-top: auto; /* 将按钮推到底部 */
  padding-top: 1rem;
  text-align: center; /* 新增：使按钮居中 */
}

.btn {
  padding: 0.6rem 1.2rem; /* 调整按钮内边距 */
  border-radius: 6px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
  border: 1px solid transparent;
  display: inline-block; /* 使按钮宽度自适应内容 */
  font-size: 0.9rem; /* 稍微缩小字体 */
}

.btn-primary-outline {
  background-color: transparent;
  color: var(--color-primary);
  border: 1px solid var(--color-primary);
}
.btn-primary-outline:hover {
  background-color: var(--color-primary-soft);
}

/* 分页 */
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 1rem;
  margin-top: 2rem;
}

.page-btn {
  padding: 0.5rem 1rem;
  border: 1px solid var(--color-border);
  background-color: var(--color-surface);
  border-radius: 6px;
  cursor: pointer;
}
.page-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
.page-info {
  font-weight: 500;
  color: var(--color-text-muted);
}
</style>