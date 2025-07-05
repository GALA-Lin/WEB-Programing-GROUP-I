<template>
  <div class="news-detail-page">
    <div v-if="loading" class="loading-container">
      <el-skeleton animated>
        <template #template>
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
      <header class="article-header">
        <div class="container header-content">
          <h1 class="article-title">{{ news.title }}</h1>
          <div class="article-meta-header">
            <span><el-icon><User /></el-icon>{{ news.authorName || '匿名作者' }}</span>
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
/* 【变更】整体页面背景色 */
.news-detail-page {
  background-color: var(--color-background-soft);
}

.loading-container, .error-container {
  padding: 2rem;
  background-color: var(--color-surface);
  min-height: 100vh;
}

.news-article {
  background-color: var(--color-surface);
}

/* 【变更】文章头部的全新样式 */
.article-header {
  padding: 3rem 0;
  background-color: var(--color-surface);
  border-bottom: 1px solid var(--color-border);
  text-align: center;
}

.header-content {
  max-width: 800px;
  margin: 0 auto;
  padding: 0 1rem;
}

.article-title {
  font-size: 2.5rem;
  font-weight: 800;
  color: var(--color-text-heading);
  margin-bottom: 1.5rem;
  line-height: 1.3;
}

.article-meta-header {
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
  gap: 1rem 2rem;
  font-size: 0.95rem;
  color: var(--color-text-muted);
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
  max-width: 800px;
  margin: 0 auto;
  background-color: var(--color-surface);
  padding: 0 2rem; /* 增加内边距，让内容更舒适 */
}

/* 保持正文样式不变 */
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
  max-width: 800px;
  margin: 3rem auto 0;
  padding: 2rem 2rem 0;
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