<template>
  <div class="news-detail-page">
    <div v-if="loading" class="loading-container">
    </div>

    <div v-if="error" class="error-tip">{{ error }}</div>

    <article v-if="news" class="news-article">
      <div class="container article-body-container">
        <div class="article-content">
          <v-md-preview :text="news.content"></v-md-preview>
        </div>

        <div class="actions-bar">
          <div class="like-wrapper">
            <button class="like-button" @click="handleLike" :disabled="isLiking">
              <el-icon><Pointer /></el-icon>
              <span>点赞 ({{ news.likesCount }})</span>
            </button>
          </div>
        </div>

      </div>
    </article>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
// ▼▼▼ 不再需要 unlikeNews ▼▼▼
import { getNewsDetail, likeNews } from '@/services/newsApi.js';
import { ElMessage, ElSkeleton, ElSkeletonItem, ElIcon } from 'element-plus';
import { User, Calendar, Pointer, Link } from '@element-plus/icons-vue';
import VMdPreview from '@kangc/v-md-editor/lib/preview';
// ... v-md-editor config ...

VMdPreview.use(githubTheme, { Hljs: hljs });

const news = ref(null);
const loading = ref(true);
const error = ref(null);
const isLiking = ref(false); // 保留这个状态以防止快速连点

const route = useRoute();
const newsId = route.params.id;

const fetchNewsDetail = async () => {
  try {
    loading.value = true;
    news.value = await getNewsDetail(newsId);
    // 不再需要 isLiked 状态
  } catch (err) {
    error.value = '无法加载新闻详情。';
  } finally {
    loading.value = false;
  }
};

// ▼▼▼【核心改造】handleLike 函数回归简单 ▼▼▼
const handleLike = async () => {
  if (isLiking.value) return;
  isLiking.value = true;

  try {
    const response = await likeNews(newsId);
    if (news.value) {
      news.value.likesCount = response.newLikesCount;
    }
    // ElMessage.success('感谢您的支持！'); // 频繁点赞时可以不提示
  } catch (err) {
    ElMessage.error('操作失败，请稍后再试。');
  } finally {
    // 快速解除锁定，允许连续点赞
    setTimeout(() => { isLiking.value = false; }, 200);
  }
};

onMounted(fetchNewsDetail);
</script>

<style scoped>
/* 样式部分保持不变 */
</style>