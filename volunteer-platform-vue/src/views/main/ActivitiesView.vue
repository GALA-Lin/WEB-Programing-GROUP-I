<template>
  <div class="activities-page">
    <div class="page-header">
      <h1 class="page-title">探索精彩活动</h1>
      <p class="page-subtitle">发现并参与我们的志愿活动，与我们一起为社会贡献力量。</p>
    </div>

    <div class="container">
      <div class="filter-section">
        <div class="filter-controls">
          <el-input v-model="searchTerm" placeholder="搜索活动名称、地点..." clearable @change="searchActivities" class="filter-search">
            <template #prepend>
              <el-select v-model="selectedCategory" placeholder="全部分类" @change="searchActivities" style="width: 130px;">
                <el-option label="全部分类" value=""></el-option>
                <el-option label="校内服务" value="校内服务"></el-option>
                <el-option label="社区服务" value="社区服务"></el-option>
                <el-option label="环保" value="环保"></el-option>
                <el-option label="教育" value="教育"></el-option>
                <el-option label="文化传播" value="文化传播"></el-option>
              </el-select>
            </template>
          </el-input>
        </div>
      </div>

      <div v-if="loading" class="loading-state">
        <el-skeleton animated>
          <template #template>
            <div class="activities-grid">
              <el-skeleton-item v-for="i in 6" :key="i" variant="image" style="width: 100%; height: 220px; border-radius: 12px;" />
            </div>
          </template>
        </el-skeleton>
      </div>
      <div v-if="error" class="error-state">{{ error }}</div>

      <div v-if="!loading && paginatedActivities.length > 0">
        <div class="activities-grid">
          <div class="activity-card" v-for="activity in paginatedActivities" :key="activity.id">
            <div class="activity-image-wrapper">
              <router-link :to="`/activities/${activity.id}`">
                <img :src="activity.coverImageUrl || `https://source.unsplash.com/random/400x300?sig=${activity.id}`" :alt="activity.title" class="activity-image" />
              </router-link>
              <span class="activity-category-tag">{{ activity.category }}</span>
            </div>
            <div class="activity-content">
              <h3 class="activity-title">
                <router-link :to="`/activities/${activity.id}`">{{ activity.title }}</router-link>
              </h3>

              <div class="activity-meta">
                <div class="meta-item">
                  <el-icon><Calendar /></el-icon>
                  <span>{{ formatDate(activity.startTime) }}</span>
                </div>
                <div class="meta-item">
                  <el-icon><LocationInformation /></el-icon>
                  <span>{{ activity.location }}</span>
                </div>
              </div>

              <div class="enrollment-progress">
                <el-progress :percentage="getEnrollmentPercentage(activity)" :stroke-width="6" :color="getStatusColor(activity.status)"/>
                <div class="progress-text">
                  <span>{{ activity.currentEnrollment }} / {{ activity.recruitmentQuota }} 人</span>
                  <el-tag :type="getStatusType(activity.status)" size="small" effect="light">{{ getStatusText(activity.status) }}</el-tag>
                </div>
              </div>

            </div>
          </div>
        </div>
        <div class="pagination-wrapper">
          <el-pagination
              background
              layout="prev, pager, next"
              :total="total"
              :page-size="pageSize"
              :current-page="currentPage"
              @current-change="handlePageChange"
          />
        </div>
      </div>
      <div v-if="!loading && paginatedActivities.length === 0" class="empty-state">
        <el-empty description="暂无相关活动"></el-empty>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { getPublicActivities } from '@/services/publicActivityApi.js';
import { ElMessage, ElSkeleton, ElSkeletonItem, ElPagination, ElIcon, ElTag, ElProgress, ElInput, ElSelect, ElOption, ElEmpty } from 'element-plus';
import { Calendar, LocationInformation } from '@element-plus/icons-vue';

const selectedCategory = ref('');
const searchTerm = ref('');
const loading = ref(true);
const error = ref(null);
const activities = ref([]);
const total = ref(0);
const currentPage = ref(1);
const pageSize = ref(9); // 每页9个活动，方便3x3布局

const paginatedActivities = computed(() => activities.value);

const searchActivities = () => {
  currentPage.value = 1;
  fetchActivities();
};

const fetchActivities = async () => {
  loading.value = true;
  try {
    // 假设 getPublicActivities 已被修改为接受搜索词
    const response = await getPublicActivities(currentPage.value, pageSize.value, selectedCategory.value, searchTerm.value);
    activities.value = response.list || [];
    total.value = response.total || 0;
  } catch (err) {
    error.value = '加载活动列表失败，请稍后再试。';
    ElMessage.error(error.value);
  } finally {
    loading.value = false;
  }
};

const handlePageChange = (page) => {
  currentPage.value = page;
  fetchActivities();
};

const formatDate = (dateString) => {
  if (!dateString) return '待定';
  return new Date(dateString).toLocaleDateString('zh-CN', { month: 'long', day: 'numeric' });
};

const getEnrollmentPercentage = (activity) => {
  if (!activity.recruitmentQuota) return 0;
  return Math.min(Math.round((activity.currentEnrollment / activity.recruitmentQuota) * 100), 100);
};

const getStatusText = (status) => {
  const map = { recruiting: '招募中', ongoing: '进行中', finished: '已结束', canceled: '已取消' };
  return map[status] || '未知';
}
const getStatusType = (status) => {
  const map = { recruiting: 'success', ongoing: 'primary', finished: 'info', canceled: 'warning' };
  return map[status] || 'info';
}
const getStatusColor = (status) => {
  const map = { recruiting: '#67c23a', ongoing: '#409eff', finished: '#909399', canceled: '#e6a23c' };
  return map[status] || '#909399';
}

onMounted(() => {
  fetchActivities();
});
</script>

<style scoped>
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
}
.page-subtitle {
  font-size: 1.1rem;
  color: var(--color-text-muted);
  max-width: 600px;
  margin: 0.5rem auto 0;
}
.container {
  padding: 2rem 20px;
}
.filter-section {
  margin-bottom: 2rem;
  display: flex;
  justify-content: center;
}
.filter-controls {
  max-width: 600px;
  width: 100%;
}
.loading-state, .error-state, .empty-state {
  text-align: center;
  padding: 4rem;
  font-size: 1.2rem;
  color: var(--color-text-muted);
}
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
  transition: transform 0.2s ease, box-shadow 0.2s ease;
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
  background-color: #f0f2f5;
}
.activity-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}
.activity-card:hover .activity-image {
  transform: scale(1.05);
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
  backdrop-filter: blur(4px);
}
.activity-content {
  padding: 1.5rem;
  display: flex;
  flex-direction: column;
  flex-grow: 1;
}
.activity-title {
  font-size: 1.2rem;
  font-weight: 700;
  color: var(--color-text-heading);
  margin: 0 0 0.75rem;
  line-height: 1.4;
}
.activity-title a {
  color: inherit;
  text-decoration: none;
}
.activity-title a:hover {
  color: var(--color-primary);
}
.activity-meta {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  margin-bottom: 1rem;
  font-size: 0.9rem;
}
.meta-item {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: var(--color-text-body);
}
.enrollment-progress {
  margin-top: auto;
  padding-top: 1rem;
}
.progress-text {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 0.8rem;
  color: var(--color-text-muted);
  margin-top: 0.25rem;
}
.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 3rem;
}
</style>