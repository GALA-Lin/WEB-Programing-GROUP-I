<template>
  <div class="organizations-page">
    <div class="container">
      <div class="page-header">
        <h1 class="page-title">发现组织</h1>
        <p class="page-subtitle">寻找你感兴趣的团体，与志同道合的伙伴同行</p>
      </div>

      <div v-if="loading" class="organizations-grid">
        <el-skeleton v-for="i in 6" :key="i" animated>
          <template #template>
            <div class="org-card-skeleton">
              <el-skeleton-item variant="p" style="height: 80px;" />
              <div style="padding: 14px; text-align: center;">
                <el-skeleton-item variant="image" style="width: 80px; height: 80px; border-radius: 50%;" />
                <el-skeleton-item variant="h3" style="width: 50%; margin-top: 1rem;" />
                <el-skeleton-item variant="text" style="width: 80%;" />
              </div>
            </div>
          </template>
        </el-skeleton>
      </div>

      <div v-if="error" class="error-tip">{{ error }}</div>

      <div v-else class="organizations-grid">
        <div v-for="(org, index) in organizations" :key="org.id" class="org-card">
          <div class="card-brand-header" :style="{ backgroundColor: getBrandColor(index) }"></div>
          <div class="card-logo-wrapper">
            <img :src="`https://ui-avatars.com/api/?name=${org.name.charAt(0)}&size=128&background=f1f5f9&color=1e3a8a&font-size=0.5`" alt="Organization Logo" class="card-logo">
          </div>
          <div class="card-content">
            <h3 class="org-name">
              <router-link :to="`/organizations/${org.id}`">{{ org.name }}</router-link>
            </h3>
            <p class="org-description">{{ org.description }}</p>
          </div>
          <div class="card-footer">
            <div class="org-meta">
              <span><el-icon><UserFilled /></el-icon>负责人: {{ org.leaderName }}</span>
              <span><el-icon><DataAnalysis /></el-icon>成员数: {{ org.totalMembers }}</span>
            </div>
            <el-button type="primary" plain round @click="$router.push(`/organizations/${org.id}`)">查看详情</el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { getOrganizations } from '@/services/organizationApi.js';
import { ElMessage, ElSkeleton, ElSkeletonItem, ElIcon, ElButton } from 'element-plus';
import { UserFilled, DataAnalysis } from '@element-plus/icons-vue';

const organizations = ref([]);
const loading = ref(true);
const error = ref(null);

const brandColors = ['#3b82f6', '#10b981', '#f97316', '#8b5cf6', '#ec4899', '#ef4444'];
const getBrandColor = (index) => brandColors[index % brandColors.length];

onMounted(async () => {
  try {
    const response = await getOrganizations();
    // 后端返回的数据可能在 list 字段中，也可能直接是数组，做个兼容
    organizations.value = response.list || response;
  } catch (err) {
    error.value = '加载组织列表失败';
    ElMessage.error(error.value);
  } finally {
    loading.value = false;
  }
});
</script>

<style scoped>
.organizations-page {
  background-color: var(--color-background-soft);
  padding: 40px 20px;
  min-height: calc(100vh - 120px);
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

.organizations-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 2rem;
}

/* 卡片骨架屏样式 */
.org-card-skeleton {
  background-color: var(--color-surface);
  border-radius: 12px;
  overflow: hidden;
}

/* 组织卡片样式 */
.org-card {
  background-color: var(--color-surface);
  border-radius: 12px;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05);
  transition: transform 0.2s ease, box-shadow 0.2s ease;
  display: flex;
  flex-direction: column;
  position: relative;
  padding-top: 40px; /* 为顶部色块留出空间 */
}
.org-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1);
}

.card-brand-header {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 80px;
  border-radius: 12px 12px 0 0;
}

.card-logo-wrapper {
  position: relative;
  text-align: center;
  margin-top: -40px; /* 使Logo一半在色块上，一半在下面 */
  margin-bottom: 1rem;
}
.card-logo {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  border: 4px solid var(--color-surface);
  background-color: #e2e8f0;
}

.card-content {
  padding: 0 1.5rem;
  text-align: center;
  flex-grow: 1;
}
.org-name {
  font-size: 1.25rem;
  font-weight: 700;
}
.org-name a {
  color: var(--color-text-heading);
  text-decoration: none;
}
.org-name a:hover {
  color: var(--color-primary);
}
.org-description {
  font-size: 0.95rem;
  color: var(--color-text-muted);
  line-height: 1.6;
  margin-top: 0.5rem;
  min-height: 50px;
}

.card-footer {
  padding: 1.5rem;
  margin-top: 1rem;
  border-top: 1px solid var(--color-border);
  display: flex;
  flex-direction: column;
  gap: 1rem;
}
.org-meta {
  display: flex;
  justify-content: space-between;
  font-size: 0.85rem;
  color: var(--color-text-body);
}
.org-meta span {
  display: flex;
  align-items: center;
  gap: 6px;
}

.card-footer .el-button {
  width: 100%;
}
</style>