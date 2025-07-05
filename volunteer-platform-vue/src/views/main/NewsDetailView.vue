<template>
  <div class="news-detail-page">
    <div v-if="loading" class="loading-container">
      <el-skeleton animated>
        <template #template>
          <el-skeleton-item variant="image" style="width: 100%; height: 300px;" />
          <div style="padding: 14px;">
            <el-skeleton-item variant="p" style="width: 50%" />
            <el-skeleton-item variant="text" style="margin-right: 16px;" />
            <el-skeleton-item variant="text" style="width: 30%;" />
            <el-skeleton :rows="5" />
          </div>
        </template>
      </el-skeleton>
    </div>

    <div v-if="error" class="error-tip">{{ error }}</div>

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
            <button class="like-button" @click="handleLike" :class="{ liked: isLiked }">
              <el-icon><Pointer /></el-icon>
              <span>{{ isLiked ? '已赞' : '点赞' }} ({{ news.likesCount }})</span>
            </button>
          </div>
          <div class="share-wrapper">
            <span>分享至:</span>
            <div class="social-icons">
              <a href="#" class="social-icon" aria-label="WeChat"><img src="https://i.postimg.cc/L851hPnG/wechat.png" alt="WeChat"/></a>
              <a href="#" class="social-icon" aria-label="Weibo"><img src="https://i.postimg.cc/8c3Kym9p/sina-weibo.png" alt="Weibo"/></a>
              <button class="social-icon" aria-label="Copy Link"><el-icon><Link /></el-icon></button>
            </div>
          </div>
        </div>

        <div class="author-card">
          <img :src="news.authorAvatar || 'https://ui-avatars.com/api/?name=Admin&background=random&color=fff'" alt="Author Avatar" class="author-avatar">
          <div class="author-info">
            <span class="author-label">本文作者</span>
            <h4 class="author-name">{{ news.authorName }}</h4>
            <p class="author-bio">“油炬智愿”平台内容管理员，致力于传播志愿精神，分享感动瞬间。</p>
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
import { ElMessage, ElSkeleton, ElSkeletonItem, ElIcon } from 'element-plus';
import { User, Calendar, Pointer, Link } from '@element-plus/icons-vue';

// 引入 v-md-editor 的预览组件及其样式
import VMdPreview from '@kangc/v-md-editor/lib/preview';
import '@kangc/v-md-editor/lib/style/preview.css';
import githubTheme from '@kangc/v-md-editor/lib/theme/github.js';
import '@kangc/v-md-editor/lib/theme/style/github.css';
import hljs from 'highlight.js';

VMdPreview.use(githubTheme, { Hljs: hljs });

const news = ref(null);
const loading = ref(true);
const error = ref(null);
const isLiked = ref(false); // 点赞状态

const route = useRoute();
const newsId = route.params.id;

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

const handleLike = async () => {
  if (isLiked.value) {
    ElMessage.info('您已经点过赞了');
    return;
  }
  try {
    const response = await likeNews(newsId);
    if (news.value) {
      news.value.likesCount = response.newLikesCount;
    }
    isLiked.value = true; // 设置为已点赞
    ElMessage.success('感谢您的支持！');
  } catch (err) {
    ElMessage.error('点赞失败，请稍后再试。');
    console.error(err);
  }
};

onMounted(fetchNewsDetail);
</script>

<style scoped>
.news-detail-page {
  background-color: var(--color-surface);
}

.loading-container {
  max-width: 800px;
  margin: 40px auto;
}

/* 1. 文章页眉样式 */
.article-header {
  position: relative;
  width: 100%;
  height: 40vh;
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
  padding-bottom: 2rem;
}
.article-title {
  font-size: 3rem;
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

/* 2. 文章主体布局 */
.article-body-container {
  padding-top: 3rem;
  padding-bottom: 4rem;
}
.article-content {
  max-width: 720px; /* 黄金阅读宽度 */
  margin: 0 auto;
}

/* 3. 使用 :deep() 精细化排版 v-md-preview 渲染出的内容 */
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
.article-content :deep(ul),
.article-content :deep(ol) {
  padding-left: 2em;
}

/* 4. 互动与分享栏 */
.actions-bar {
  max-width: 720px;
  margin: 3rem auto;
  padding-top: 2rem;
  border-top: 1px solid var(--color-border);
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.like-button {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 24px;
  font-size: 1rem;
  font-weight: 600;
  border: 1px solid var(--color-border);
  border-radius: 99px;
  cursor: pointer;
  background-color: var(--color-background-soft);
  transition: all 0.2s;
}
.like-button:hover {
  border-color: var(--color-primary);
  color: var(--color-primary);
  transform: scale(1.05);
}
.like-button.liked {
  background-color: var(--color-primary);
  color: white;
  border-color: var(--color-primary);
}
.share-wrapper {
  display: flex;
  align-items: center;
  gap: 1rem;
  color: var(--color-text-muted);
}
.social-icons {
  display: flex;
  gap: 0.5rem;
}
.social-icon {
  width: 36px; height: 36px;
  border-radius: 50%;
  background-color: var(--color-background-soft);
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  color: var(--color-text-body);
  border: none;
}
.social-icon:hover {
  background-color: var(--color-border);
}
.social-icon img {
  width: 20px;
}

/* 5. 作者信息卡片 */
.author-card {
  max-width: 720px;
  margin: 2rem auto 0;
  background-color: var(--color-background-soft);
  border-radius: 12px;
  padding: 1.5rem;
  display: flex;
  gap: 1.5rem;
  align-items: flex-start;
}
.author-avatar {
  width: 64px;
  height: 64px;
  border-radius: 50%;
  flex-shrink: 0;
}
.author-info {
  flex-grow: 1;
}
.author-label {
  font-size: 0.8rem;
  color: var(--color-text-muted);
}
.author-name {
  margin: 0.25rem 0 0.5rem;
  font-size: 1.2rem;
  font-weight: 700;
  color: var(--color-text-heading);
}
.author-bio {
  font-size: 0.9rem;
  color: var(--color-text-body);
  margin: 0;
  line-height: 1.6;
}

@media(max-width: 768px) {
  .article-title { font-size: 2rem; }
  .actions-bar { flex-direction: column; gap: 1.5rem; }
}
</style>