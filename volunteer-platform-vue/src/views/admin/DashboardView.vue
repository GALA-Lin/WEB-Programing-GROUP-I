<template>
  <div class="dashboard-view">
    <el-row :gutter="20" class="stats-row">
      <el-col :xs="24" :sm="12" :md="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon-wrapper" style="background-color: #e8f5ff;"><el-icon :size="32" color="#409eff"><User /></el-icon></div>
          <div class="stat-content"><div class="stat-label">总用户数</div><div class="stat-value">{{ stats.totalUsers }}</div></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon-wrapper" style="background-color: #e9fbf4;"><el-icon :size="32" color="#67c23a"><Flag /></el-icon></div>
          <div class="stat-content"><div class="stat-label">活动总数</div><div class="stat-value">{{ stats.totalActivities }}</div></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon-wrapper" style="background-color: #fff8e6;"><el-icon :size="32" color="#e6a23c"><Timer /></el-icon></div>
          <div class="stat-content"><div class="stat-label">累计志愿时长</div><div class="stat-value">{{ stats.totalServiceHours }}</div></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon-wrapper" style="background-color: #fef0f0;"><el-icon :size="32" color="#f56c6c"><OfficeBuilding /></el-icon></div>
          <div class="stat-content"><div class="stat-label">组织总数</div><div class="stat-value">{{ stats.totalOrganizations }}</div></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="quick-actions-row">
      <el-col>
        <el-card>
          <template #header><div>快捷操作</div></template>
          <div class="quick-actions-container">
            <div class="action-item" @click="navigateTo('/admin/activities?action=create')">
              <el-icon class="action-icon" color="#409eff"><CirclePlus /></el-icon>
              <span>发布活动</span>
            </div>
            <div class="action-item" @click="navigateTo('/admin/news?action=create')">
              <el-icon class="action-icon" color="#67c23a"><DocumentAdd /></el-icon>
              <span>创建新闻</span>
            </div>
            <div class="action-item" @click="navigateTo('/admin/users')">
              <el-icon class="action-icon" color="#e6a23c"><Search /></el-icon>
              <span>搜索用户</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="24">
        <LatestUpdates />
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { getAdminDashboardStats } from '@/services/dashboardApi.js';
import LatestUpdates from '@/components/LatestUpdates.vue'; // 导入新组件
import { ElMessage } from 'element-plus';
import { User, Flag, Timer, OfficeBuilding, CirclePlus, DocumentAdd, Search } from '@element-plus/icons-vue';

const router = useRouter();
const stats = ref({
  totalUsers: 0,
  totalActivities: 0,
  totalServiceHours: 0,
  totalOrganizations: 0,
});
const loading = ref(true);

onMounted(async () => {
  try {
    loading.value = true;
    const data = await getAdminDashboardStats();
    stats.value = data;
  } catch (error) {
    ElMessage.error('加载看板数据失败');
    console.error(error);
  } finally {
    loading.value = false;
  }
});

const navigateTo = (path) => {
  router.push(path);
};
</script>

<style scoped>
.dashboard-view {
  padding: 10px;
  display: flex;
  flex-direction: column;
  gap: 20px; /* 控制行间距 */
}
.stats-row .el-col {
  margin-bottom: 20px;
}
.stat-card .el-card__body {
  display: flex;
  align-items: center;
  padding: 25px;
}
.stat-icon-wrapper {
  height: 60px;
  width: 60px;
  border-radius: 8px; /* 轻微圆角 */
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20px;
}
.stat-content {
  display: flex;
  flex-direction: column;
}
.stat-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 5px;
}
.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}

/* 快捷操作样式 */
.quick-actions-container {
  display: flex;
  gap: 40px;
  padding: 10px;
}
.action-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  cursor: pointer;
  color: #606266;
  transition: color 0.2s;
}
.action-item:hover {
  color: #409eff;
}
.action-icon {
  font-size: 32px;
  margin-bottom: 10px;
}
</style>