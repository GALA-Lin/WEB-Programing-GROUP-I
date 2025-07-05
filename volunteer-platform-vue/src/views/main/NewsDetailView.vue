<template>
  <div class="news-detail-page">
    <div v-if="loading" class="loading-container">
      <el-skeleton animated>
        <template #template>
          <el-skeleton-item variant="image" style="width: 100%; height: 300px;" />
          <div class="container" style="padding: 24px;">
            <el-skeleton-item variant="h1" style="width: 60%; margin-bottom: 20px;" />
            <el-skeleton :rows="5" />
          </div>
        </template>
      </el-skeleton>
    </div>

    <div v-if="error" class="error-container">
      <el-result icon="error" title="加载失败" :sub-title="error">
        <template #extra>
          <el-button type="primary" @click="$router.back()">返回上一页</el-button>
        </template>
      </el-result>
    </div>

    <article v-if="news" class="news-article">
      <header class="article-header" :style="{ backgroundImage: `url(${news.coverImageUrl || 'https://images.unsplash.com/photo-1585829365295-ab7cd400c167?q=80&w=1200'})` }">
        <div class="header-overlay"></div>
        <div class="container header-content">
          <h1 class="article-title">{{ news.title }}</h1>
          <div class="article-meta-header">
            <span><el-icon><User /></el-icon>{{ news.authorName }}</span>
            <span><el-icon><Calendar /></el-icon>发布于 {{ news.publishedAt }}</span>
          </div>
        </div>
      </header>

      <div class="container article-body-container">
        <div class="article-content">
          <v-md-preview :text="news.content"></v-md-preview>
        </div>

        <div class="actions-bar">
          <div class="like-wrapper">
            <el-button class="like-button" @click="handleLike" :loading="isLiking" size="large" round>
              <template #icon><Pointer /></template>
              为我点赞 ({{ news.likesCount }})
            </el-button>
          </div>
        </div>
      </div>
    </article>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { getNewsDetail, likeNews } from '@/services/newsApi.js';
import { ElMessage, ElSkeleton, ElSkeletonItem, ElIcon, ElButton, ElResult } from 'element-plus';
import { User, Calendar, Pointer } from '@element-plus/icons-vue';

import VMdPreview from '@kangc/v-md-editor/lib/preview';
import '@kangc/v-md-editor/lib/style/preview.css';
import githubTheme from '@kangc/v-md-editor/lib/theme/github.js';
import '@kangc/v-md-editor/lib/theme/style/github.css';
import hljs from 'highlight.js';

VMdPreview.use(githubTheme, { Hljs: hljs });

const news = ref(null);
const loading = ref(true);
const error = ref(null);
const isLiking = ref(false);

const route = useRoute();
const newsId = route.params.id;

const fetchNewsDetail = async () => {
  try {
    loading.value = true;
    news.value = await getNewsDetail(newsId);
  } catch (err) {
    error.value = '无法加载新闻详情，该文章可能已被删除或不存在。';
  } finally {
    loading.value = false;
  }
};

const handleLike = async () => {
  if (isLiking.value) return;
  isLiking.value = true;
  try {
    const response = await likeNews(newsId);
    if (news.value) {
      news.value.likesCount = response.newLikesCount;
    }
  } catch (err) {
    ElMessage.error('操作失败，请稍后再试。');
  } finally {
    setTimeout(() => { isLiking.value = false; }, 300);
  }
};

onMounted(fetchNewsDetail);
</script>

<style scoped>
.news-detail-page {
  background-color: var(--color-surface);
}

.loading-container {
  padding: 2rem;
}

.error-container {
  padding: 4rem 0;
}

.article-header {
  position: relative;
  width: 100%;
  height: 45vh;
  min-height: 300px;
  background-size: cover;
  background-position: center;
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
  color: white;
}
.header-overlay {
  position: absolute;
  top: 0; left: 0; width: 100%; height: 100%;
  background: linear-gradient(to top, rgba(0,0,0,0.7), rgba(0,0,0,0.2));
}
.header-content {
  position: relative;
  z-index: 2;
  padding: 0 1rem;
}
.article-title {
  font-size: 2.8rem;
  font-weight: 800;
  text-shadow: 0 2px 10px rgba(0,0,0,0.5);
  margin-bottom: 1rem;
}
.article-meta-header {
  display: flex;
  justify-content: center;
  gap: 2rem;
  font-size: 0.9rem;
  opacity: 0.9;
}
.article-meta-header span {
  display: flex;
  align-items: center;
  gap: 8px;
}

.article-body-container {
  padding-top: 3rem;
  padding-bottom: 4rem;
}
.article-content {
  max-width: 760px;
  margin: 0 auto;
}

.article-content :deep(h1),
.article-content :deep(h2),
.article-content :deep(h3) {
  color: var(--color-text-heading);
  margin-top: 2.5em;
  margin-bottom: 1em;
  font-weight: 700;
}
.article-content :deep(p) {
  line-height: 1.8;
  margin-bottom: 1.5em;
  font-size: 1.05rem;
  color: var(--color-text-body);
}
.article-content :deep(blockquote) {
  border-left: 4px solid var(--color-primary);
  padding-left: 1.5em;
  margin: 2em 0;
  color: var(--color-text-muted);
  font-style: italic;
}

.actions-bar {
  max-width: 760px;
  margin: 3rem auto 0;
  padding-top: 2rem;
  border-top: 1px solid var(--color-border);
  text-align: center;
}
.like-button {
  --el-button-text-color: var(--color-primary);
  --el-button-border-color: #dcdfe6;
}
.like-button:hover, .like-button:focus {
  --el-button-hover-text-color: var(--color-primary-hover);
  --el-button-hover-border-color: var(--color-primary-hover);
  --el-button-hover-bg-color: var(--color-primary-soft);
}
</style>