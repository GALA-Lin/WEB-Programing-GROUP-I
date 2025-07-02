<template>
  <div class="container news-detail-view">
    <div v-if="loading" class="loading-tip">正在加载...</div>
    <div v-if="error" class="error-tip">{{ error }}</div>

    <article v-if="news">
      <h1 class="news-title">{{ news.title }}</h1>
      <div class="news-meta">
        <span>作者: {{ news.authorName }}</span>
        <span>发布于: {{ news.publishedAt }}</span>
      </div>

      <div class="news-content" v-html="news.content"></div>

      <div class="actions-bar">
        <button class="like-button" @click="handleLike">
          👍 点赞 ({{ news.likesCount }})
        </button>
      </div>
    </article>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { getNewsDetail, likeNews } from '@/services/newsApi.js';
import { ElMessage } from 'element-plus'; // 引入 Element Plus 消息提示

const news = ref(null);
const loading = ref(true);
const error = ref(null);

const route = useRoute();
const newsId = route.params.id; // 从路由中获取新闻ID

// 获取新闻详情
const fetchNewsDetail = async () => {
  try {
    loading.value = true;
    news.value = await getNewsDetail(newsId);
  } catch (err) {
    error.value = '无法加载新闻详情。';
    console.error(err);
  } finally {
    loading.value = false;
  }
};

// 处理点赞事件
const handleLike = async () => {
  try {
    const response = await likeNews(newsId);
    // 使用后端返回的最新点赞数来更新页面
    if (news.value) {
      news.value.likesCount = response.newLikesCount;
    }
    ElMessage.success('点赞成功！');
  } catch (err) {
    ElMessage.error('点赞失败，请稍后再试。');
    console.error(err);
  }
};

onMounted(fetchNewsDetail);
</script>

<style scoped>
.news-detail-view {
  padding: 40px 0;
  max-width: 800px;
}
.news-title {
  font-size: 2.8rem;
  margin-bottom: 20px;
}
.news-meta {
  display: flex;
  gap: 20px;
  color: #888;
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #eee;
}
.news-content {
  line-height: 1.8;
  font-size: 1.1rem;
}
.news-content :deep(p) { /* 让v-html里的p标签生效 */
  margin-bottom: 1em;
}
.actions-bar {
  margin-top: 40px;
  text-align: center;
}
.like-button {
  padding: 10px 25px;
  font-size: 1rem;
  cursor: pointer;
  background-color: #f1f5f9;
  border: 1px solid #cbd5e1;
  border-radius: 20px;
  transition: all 0.3s;
}
.like-button:hover {
  background-color: #e2e8f0;
  border-color: #94a3b8;
}
</style>