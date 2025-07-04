<template>
  <div class="organizations-view">
    <div class="page-header">
      <h1 class="page-title">志愿组织列表</h1>
      <p class="page-subtitle">寻找你感兴趣的团体，与志同道合的伙伴同行</p>
    </div>
    <div v-if="loading" class="loading">正在加载...</div>
    <div v-else class="organizations-grid">
      <el-card v-for="org in organizations" :key="org.id" class="org-card" shadow="hover">
        <template #header>
          <div class="card-header">
            <router-link :to="`/organizations/${org.id}`" class="org-name">{{ org.name }}</router-link>
          </div>
        </template>
        <p class="org-description">{{ org.description }}</p>
        <div class="org-meta">
          <span>负责人: {{ org.leaderName }}</span>
          <span>成员数: {{ org.totalMembers }}</span>
        </div>
        <div class="card-footer">
          <el-button type="primary" @click="$router.push(`/organizations/${org.id}`)">查看详情</el-button>
        </div>
      </el-card>
    </div>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue';
import { getOrganizations } from '@/services/organizationApi.js';
import { ElMessage } from 'element-plus';

const organizations = ref([]);
const loading = ref(true);

onMounted(async () => {
  try {
    organizations.value = await getOrganizations();
  } catch (error) {
    ElMessage.error('加载组织列表失败');
  } finally {
    loading.value = false;
  }
});
</script>
<style scoped>
.organizations-view { padding: 20px; }
.page-header { text-align: center; margin-bottom: 40px; }
.page-title { font-size: 32px; }
.page-subtitle { color: #666; }
.organizations-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(300px, 1fr)); gap: 20px; }
.org-card { transition: all 0.2s; }
.org-card:hover { transform: translateY(-5px); box-shadow: 0 4px 20px rgba(0,0,0,0.1); }
.card-header { display: flex; justify-content: space-between; align-items: center; }
.org-name { font-weight: bold; font-size: 18px; color: #333; text-decoration: none; }
.org-name:hover { color: #409eff; }
.org-description { color: #666; margin-bottom: 20px; min-height: 40px; }
.org-meta { font-size: 14px; color: #999; display: flex; justify-content: space-between; }
.card-footer { border-top: 1px solid #ebeef5; margin-top: 20px; padding-top: 15px; text-align: right; }
</style>