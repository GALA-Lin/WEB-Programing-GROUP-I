<template>
  <div class="container news-view">
    <h1 class="page-title">新闻资讯</h1>
    <p class="page-subtitle">了解平台的最新动态与志愿故事</p>

    <div v-if="loading" class="loading-tip">正在加载新闻...</div>
    <div v-if="error" class="error-tip">{{ error }}</div>

    <div v-if="!loading && newsList.length > 0" class="news-list">
      <div v-for="news in newsList" :key="news.id" class="news-item-card">
        <div class="news-item-content">
          <h2 class="news-title">
            <router-link :to="'/news/' + news.id">{{ news.title }}</router-link>
          </h2>
          <p class="news-summary">{{ news.summary }}</p>
          <div class="news-meta">
            <span>作者: {{ news.authorName }}</span>
            <span>发布于: {{ news.publishedAt }}</span>
            <span>点赞: {{ news.likesCount }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { getNewsList } from '@/services/newsApi.js';

const newsList = ref([]);
const loading = ref(true);
const error = ref(null);

const fetchNews = async () => {
  try {
    loading.value = true;
    // 调用API获取第一页的数据
    const response = await getNewsList(1, 10);
    newsList.value = response.list; // API返回的数据在 list 字段中
  } catch (err) {
    error.value = '无法加载新闻列表，请稍后再试。';
    console.error(err);
  } finally {
    loading.value = false;
  }
};

// 组件加载完成后，自动获取新闻数据
onMounted(fetchNews);
</script>

<style scoped>
.news-view {
  padding: 40px 0;
}
.page-title {
  text-align: center;
  font-size: 2.5rem;
  margin-bottom: 10px;
}
.page-subtitle {
  text-align: center;
  color: #666;
  margin-bottom: 40px;
}
.news-list {
  display: grid;
  gap: 20px;
}
.news-item-card {
  border: 1px solid #e0e0e0;
  padding: 20px;
  border-radius: 8px;
  transition: box-shadow 0.3s;
}
.news-item-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}
.news-title a {
  text-decoration: none;
  color: #333;
  font-size: 1.5rem;
}
.news-title a:hover {
  color: #2563eb;
}
.news-summary {
  color: #555;
  margin: 15px 0;
}
.news-meta {
  display: flex;
  gap: 20px;
  font-size: 0.9rem;
  color: #888;
}
</style>