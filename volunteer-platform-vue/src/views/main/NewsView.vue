<!--占位图服务 source.unsplash.com/random 来模拟-->

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
      </div>

      <div v-if="error" class="error-tip">{{ error }}</div>

      <div v-if="!loading && newsList.length > 0">
        <div class="featured-news-card" v-if="featuredNews">
          <img :src="featuredNews.coverImageUrl || 'https://images.unsplash.com/photo-1495020689067-958852a7765e?q=80&w=1200'" alt="Featured news image" class="featured-image">
          <div class="featured-content">
            <span class="featured-tag">最新动态</span>
            <h2 class="featured-title">
              <router-link :to="'/news/' + featuredNews.id">{{ featuredNews.title }}</router-link>
            </h2>
            <p class="featured-summary">{{ featuredNews.summary }}</p>
            <div class="news-meta">
              <span><el-icon><User /></el-icon>{{ featuredNews.authorName }}</span>
              <span><el-icon><Calendar /></el-icon>{{ featuredNews.publishedAt }}</span>
              <span><el-icon><Pointer /></el-icon>{{ featuredNews.likesCount }} 人点赞</span>
            </div>
          </div>
        </div>

        <div class="news-grid">
          <div v-for="news in remainingNews" :key="news.id" class="news-item-card">
            <div class="card-image-wrapper">
              <img :src="news.coverImageUrl || `https://source.unsplash.com/random/400x250?sig=${news.id}`" alt="News image" class="card-image">
            </div>
            <div class="card-content">
              <h3 class="news-title">
                <router-link :to="'/news/' + news.id">{{ news.title }}</router-link>
              </h3>
              <p class="news-summary">{{ news.summary }}</p>
              <div class="news-meta">
                <span><el-icon><User /></el-icon>{{ news.authorName }}</span>
                <span><el-icon><Calendar /></el-icon>{{ news.publishedAt }}</span>
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
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { getNewsList } from '@/services/newsApi.js';
import { ElMessage, ElSkeleton, ElPagination, ElIcon } from 'element-plus';
import { User, Calendar, Pointer } from '@element-plus/icons-vue';

const newsList = ref([]);
const loading = ref(true);
const error = ref(null);

// 分页相关状态
const total = ref(0);
const currentPage = ref(1);
const pageSize = ref(7); // 每页7条，这样1条做头条，剩下6条可以2x3布局

// 计算属性，分离出头条新闻和列表新闻
const featuredNews = computed(() => newsList.value.length > 0 ? newsList.value[0] : null);
const remainingNews = computed(() => newsList.value.length > 1 ? newsList.value.slice(1) : []);

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

/* 头条新闻卡片 */
.featured-news-card {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 2rem;
  background-color: var(--color-surface);
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 8px 30px rgba(0,0,0,0.08);
  margin-bottom: 3rem;
}

.featured-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  min-height: 350px;
}

.featured-content {
  padding: 2.5rem;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.featured-tag {
  display: inline-block;
  background-color: var(--color-primary-soft);
  color: var(--color-primary);
  padding: 0.3rem 0.8rem;
  border-radius: 99px;
  font-size: 0.8rem;
  font-weight: 600;
  margin-bottom: 1rem;
  align-self: flex-start;
}

.featured-title {
  font-size: 2rem;
  font-weight: 700;
  margin-bottom: 1rem;
}

.featured-title a {
  color: var(--color-text-heading);
  text-decoration: none;
  transition: color 0.2s;
}

.featured-title a:hover {
  color: var(--color-primary);
}

.featured-summary {
  font-size: 1rem;
  color: var(--color-text-body);
  line-height: 1.7;
  margin-bottom: 1.5rem;
}

/* 新闻网格布局 */
.news-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 2rem;
}

.news-item-card {
  background-color: var(--color-surface);
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05), 0 2px 4px -2px rgba(0, 0, 0, 0.05);
  transition: transform 0.2s ease, box-shadow 0.2s ease;
  display: flex;
  flex-direction: column;
}

.news-item-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.07), 0 4px 6px -4px rgba(0, 0, 0, 0.07);
}

.card-image-wrapper {
  height: 200px;
}
.card-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.card-content {
  padding: 1.5rem;
  display: flex;
  flex-direction: column;
  flex-grow: 1;
}

.news-title {
  font-size: 1.25rem;
  font-weight: 600;
  margin: 0 0 0.5rem;
  line-height: 1.4;
  flex-grow: 1;
}
.news-title a {
  color: inherit;
  text-decoration: none;
}
.news-title a:hover {
  color: var(--color-primary);
}

.news-summary {
  font-size: 0.95rem;
  color: var(--color-text-muted);
  margin-bottom: 1rem;
}

.news-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 1.5rem;
  font-size: 0.85rem;
  color: var(--color-text-muted);
  margin-top: auto;
  padding-top: 1rem;
  border-top: 1px solid var(--color-border);
}
.news-meta span {
  display: flex;
  align-items: center;
  gap: 6px;
}

/* 分页 */
.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 3rem;
}

/* 响应式设计 */
@media (max-width: 992px) {
  .featured-news-card {
    grid-template-columns: 1fr;
  }
  .featured-image {
    min-height: 250px;
  }
}
</style>