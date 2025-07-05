<template>
  <div class="organization-detail-page">
    <div v-if="loading" class="loading-container">
      <el-skeleton animated>
        <template #template>
          <el-skeleton-item variant="image" style="width: 100%; height: 250px;" />
          <div class="container" style="padding-top: 2rem;">
            <el-skeleton-item variant="h1" style="width: 40%;" />
            <el-skeleton :rows="3" style="margin-top: 2rem;"/>
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

    <div v-if="organization" class="org-content-wrapper">
      <header class="org-header">
        <div class="container header-content">
          <img :src="organization.logoUrl || `https://ui-avatars.com/api/?name=${organization.name.charAt(0)}&size=128&background=eef2ff&color=4338ca`" :alt="organization.name" class="org-logo">
          <div class="header-text">
            <h1 class="org-name">{{ organization.name }}</h1>
            <p class="org-leader">负责人: {{ organization.leaderName }}</p>
          </div>
          <div class="header-action">
            <el-button v-if="!organization.isMember" type="primary" size="large" round @click="handleApply" :loading="isApplying">
              申请加入
            </el-button>
            <el-button v-else type="success" size="large" round disabled>
              您已加入
            </el-button>
          </div>
        </div>
      </header>

      <main class="container org-main">
        <el-row :gutter="30">
          <el-col :md="16">
            <el-card class="box-card">
              <template #header><h3>关于我们</h3></template>
              <p class="org-description">{{ organization.description || '暂无详细介绍。' }}</p>
            </el-card>
          </el-col>
          <el-col :md="8">
            <el-card class="box-card">
              <template #header><h3>核心成员 ({{ organization.totalMembers || 0 }})</h3></template>
              <div v-if="organization.members && organization.members.length > 0" class="member-list">
                <div v-for="member in organization.members" :key="member.userId" class="member-item">
                  <el-avatar :src="member.avatarUrl" />
                  <span>{{ member.realName }}</span>
                  <el-tag v-if="member.memberRole === 'officer'" type="warning" size="small" effect="light">干部</el-tag>
                </div>
              </div>
              <el-empty v-else description="暂无公开成员信息"></el-empty>
            </el-card>
          </el-col>
        </el-row>
      </main>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useUserStore } from '@/stores/userStore';
// ▼▼▼ 确保从正确的API服务中导入函数 ▼▼▼
import { getOrganizationDetails, applyToOrganization } from '@/services/organizationApi.js';
import { ElMessage, ElCard, ElButton, ElSkeleton, ElSkeletonItem, ElResult, ElRow, ElCol, ElAvatar, ElTag, ElEmpty } from 'element-plus';

const route = useRoute();
const router = useRouter();
const userStore = useUserStore();
const organization = ref(null);
const loading = ref(true);
const error = ref(null);
const isApplying = ref(false);

const orgId = route.params.id;

const fetchOrganization = async () => {
  try {
    loading.value = true;
    organization.value = await getOrganizationDetails(orgId);
  } catch (err) {
    error.value = err.message || '无法加载组织详情，请检查组织是否存在或稍后再试。';
    ElMessage.error(error.value);
  } finally {
    loading.value = false;
  }
};

const handleApply = async () => {
  if (!userStore.isLoggedIn) {
    ElMessage.warning('请先登录再申请加入！');
    router.push('/login');
    return;
  }
  isApplying.value = true;
  try {
    await applyToOrganization(orgId);
    ElMessage.success('申请已发送，请等待管理员审核！');
  } catch(err) {
    ElMessage.error(err.message || '申请失败，您可能已经申请过了。');
  } finally {
    isApplying.value = false;
  }
};

onMounted(fetchOrganization);
</script>

<style scoped>
.organization-detail-page {
  background-color: #f7f8fa;
  min-height: calc(100vh - 60px);
}
.org-header {
  padding: 3rem 0;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  position: relative;
}
.header-content {
  display: flex;
  align-items: center;
  gap: 2rem;
}
.org-logo {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  border: 4px solid rgba(255,255,255,0.8);
  flex-shrink: 0;
  background-color: #fff;
}
.header-text {
  flex-grow: 1;
}
.org-name {
  font-size: 2.5rem;
  font-weight: 800;
  margin: 0;
}
.org-leader {
  font-size: 1rem;
  opacity: 0.9;
  margin-top: 0.5rem;
}
.header-action {
  flex-shrink: 0;
}
.org-main {
  padding: 2rem 20px;
}
.box-card {
  border: none;
  border-radius: 12px;
  margin-bottom: 2rem;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, .05), 0 2px 4px -2px rgba(0, 0, 0, .05);
}
.org-description {
  line-height: 1.8;
  color: var(--color-text-body);
}
.member-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}
.member-item {
  display: flex;
  align-items: center;
  gap: 12px;
}
</style>