<template>
  <el-card class="box-card">
    <template #header>
      <div class="card-header">
        <span>最新动态</span>
        <el-button class="button" text @click="refreshData">
          <el-icon><Refresh /></el-icon>
          刷新
        </el-button>
      </div>
    </template>
    <el-tabs v-model="activeTab">
      <el-tab-pane label="新用户" name="users">
        <ul class="update-list">
          <li v-for="user in latestData.latestUsers" :key="user.id">
            <el-avatar :size="24" :src="user.avatarUrl || defaultAvatar" style="margin-right: 10px;"/>
            <span>{{ user.username }}</span>
            <span class="timestamp">{{ formatTimeAgo(user.registrationDate) }}</span>
          </li>
        </ul>
      </el-tab-pane>
      <el-tab-pane label="新活动" name="activities">
        <ul class="update-list">
          <li v-for="activity in latestData.latestActivities" :key="activity.id">
            <el-icon><Flag /></el-icon>
            <span>{{ activity.title }}</span>
            <span class="timestamp">{{ formatTimeAgo(activity.creationTime) }}</span>
          </li>
        </ul>
      </el-tab-pane>
    </el-tabs>
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { getDashboardLatestUpdates } from '@/services/dashboardApi.js'; // 稍后创建
import { ElMessage } from 'element-plus';
import { Refresh, Flag } from '@element-plus/icons-vue';
import { formatDistanceToNow } from 'date-fns';
import { zhCN } from 'date-fns/locale';

const activeTab = ref('users');
const latestData = ref({
  latestUsers: [],
  latestActivities: [],
});
const defaultAvatar = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png';

const fetchData = async () => {
  try {
    const data = await getDashboardLatestUpdates();
    latestData.value = data;
  } catch (error) {
    ElMessage.error('加载最新动态失败');
    console.error(error);
  }
};

const refreshData = () => {
  fetchData();
  ElMessage.success('动态已刷新！');
};

const formatTimeAgo = (dateString) => {
  if (!dateString) return '';
  return formatDistanceToNow(new Date(dateString), { addSuffix: true, locale: zhCN });
};

onMounted(fetchData);
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.update-list {
  list-style: none;
  padding: 0;
  margin: 0;
}
.update-list li {
  display: flex;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #e4e7ed;
  font-size: 14px;
}
.update-list li:last-child {
  border-bottom: none;
}
.update-list .el-icon {
  margin-right: 10px;
  color: #909399;
}
.timestamp {
  margin-left: auto;
  font-size: 12px;
  color: #909399;
}
</style>