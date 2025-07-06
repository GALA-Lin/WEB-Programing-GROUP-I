<template>
  <div class="news-page-container">
    <div class="container">
      <div class="page-header">
        <h1 class="page-title">新闻中心</h1>
        <p class="page-subtitle">了解平台的最新动态与志愿故事</p>
      </div>

      <div v-if="loading" class="skeleton-container">
        <el-skeleton :rows="5" animated />
        <el-skeleton :rows="5" animated style="margin-top: 20px" />
        <el-skeleton :rows="5" animated style="margin-top: 20px" />
      </div>

      <div v-if="error" class="error-tip">{{ error }}</div>

      <div v-if="!loading && newsList.length > 0">
        <div class="news-list">
          <div v-for="news in newsList" :key="news.id" class="news-list-item">
            <router-link :to="'/news/' + news.id" class="item-link">
              <div class="item-content">
                <h2 class="item-title">{{ news.title }}</h2>
                <p class="item-summary">{{ news.summary }}</p>
                <div class="news-meta">
                  <span><el-icon><User /></el-icon>{{ news.authorName || '匿名作者' }}</span>
                  <span><el-icon><Calendar /></el-icon>{{ news.publishedAt }}</span>
                </div>
              </div>
            </router-link>
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
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { getNewsList } from '@/services/newsApi.js';
import { ElMessage, ElSkeleton, ElPagination, ElIcon } from 'element-plus';
import { User, Calendar } from '@element-plus/icons-vue';

const newsList = ref([]);
const loading = ref(true);
const error = ref(null);

// 分页相关状态
const total = ref(0);
const currentPage = ref(1);
const pageSize = ref(10); // 每页显示10条

const fetchNews = async (page = 1) => {
  try {
    loading.value = true;
    const response = await getNewsList(page, pageSize.value);
    newsList.value = response.list;
    total.value = response.total;
    currentPage.value = page;
  } catch (err) {
    error.value = '无法加载新闻列表，请稍后再试。';
    ElMessage.error(error.value);
    console.error(err);
  } finally {
    loading.value = false;
  }
};

const handlePageChange = (newPage) => {
  fetchNews(newPage);
};

onMounted(() => {
  fetchNews(currentPage.value);
});
</script>

<style scoped>
.news-page-container {
  background-color: var(--color-background-soft);
  padding: 40px 20px;
  min-height: 100vh;
}

.page-header {
  text-align: center;
  margin-bottom: 3rem;
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

.news-list {
  max-width: 800px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.news-list-item {
  background-color: var(--color-surface);
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
  transition: box-shadow 0.3s ease, transform 0.3s ease;
}

.news-list-item:hover {
  transform: translateY(-3px);
  box-shadow: 0 6px 12px rgba(0,0,0,0.08);
}

.item-link {
  display: block;
  padding: 1.5rem 2rem;
  text-decoration: none;
  color: inherit;
}

.item-title {
  font-size: 1.5rem;
  font-weight: 600;
  color: var(--color-text-heading);
  margin: 0 0 0.75rem;
  transition: color 0.3s ease;
}

.item-link:hover .item-title {
  color: var(--color-primary);
}

.item-summary {
  font-size: 1rem;
  color: var(--color-text-body);
  line-height: 1.7;
  margin-bottom: 1.25rem;
  /* 最多显示三行 */
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.news-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 1.5rem;
  font-size: 0.9rem;
  color: var(--color-text-muted);
}

.news-meta span {
  display: flex;
  align-items: center;
  gap: 6px;
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 3rem;
}
</style>