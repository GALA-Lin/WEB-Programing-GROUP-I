<template>
  <div class="profile-page">
    <div class="profile-container">
      <div class="profile-card">
        <h1 class="card-title">用户中心</h1>
        <p class="card-subtitle">查看或更新你的个人信息</p>

        <div v-if="userStore.currentUser" class="profile-form">
          <div class="avatar-section">
            <el-avatar :size="100" :src="userStore.currentUser.avatarUrl || defaultAvatar" />
            <h2 class="username">{{ userStore.currentUser.username }}</h2>
            <p class="user-role">{{ userStore.currentUser.role }}</p>
          </div>

          <div class="service-hours-display">
            <div class="hours-value">{{ userStore.currentUser.totalServiceHours?.toFixed(2) || '0.00' }}</div>
            <div class="hours-label">累计志愿时长 (小时)</div>
          </div>

          <el-form :model="editForm" label-position="top" :disabled="!isEditing" class="info-form">
            <el-form-item label="真实姓名">
              <el-input v-model="editForm.realName" />
            </el-form-item>
            <el-form-item label="学号">
              <el-input :value="userStore.currentUser.studentId" disabled />
            </el-form-item>
            <el-form-item label="邮箱">
              <el-input v-model="editForm.email" />
            </el-form-item>
            <el-form-item label="手机号">
              <el-input v-model="editForm.phoneNumber" />
            </el-form-item>
          </el-form>

          <div class="actions">
            <el-button v-if="!isEditing" @click="startEditing" type="primary" round>编辑信息</el-button>
            <template v-if="isEditing">
              <el-button @click="handleSave" type="success" round>保存更改</el-button>
              <el-button @click="cancelEditing" round>取消</el-button>
            </template>
          </div>
        </div>
        <div v-else class="loading">正在加载用户信息...</div>
      </div>

      <div class="details-tabs">
        <el-tabs v-model="activeTab" class="profile-tabs">
          <el-tab-pane label="时长明细" name="records">
            <div v-if="loadingRecords" class="tab-loading">加载中...</div>
            <el-timeline v-else-if="serviceRecords.length" class="timeline">
              <el-timeline-item
                  v-for="record in serviceRecords"
                  :key="record.activityId"
                  :timestamp="formatDateTime(record.recordedAt)"
                  placement="top"
              >
                <el-card class="timeline-card">
                  <h4>{{ record.activityTitle }}</h4>
                  <p class="hours-badge">+ {{ record.serviceHours.toFixed(2) }} 小时</p>
                  <p class="remarks" v-if="record.remarks">备注: {{ record.remarks }}</p>
                </el-card>
              </el-timeline-item>
            </el-timeline>
            <el-empty v-else description="暂无时长记录"></el-empty>
          </el-tab-pane>

          <el-tab-pane label="我报名的活动" name="enrollments">
            <div v-if="loadingEnrollments" class="tab-loading">加载中...</div>
            <div v-else-if="enrolledActivities.length" class="enrolled-list">
              <div v-for="activity in enrolledActivities" :key="activity.activityId" class="activity-item">
                <div class="activity-info">
                  <p class="activity-title">{{ activity.title }}</p>
                  <p class="activity-time">时间: {{ formatDateTime(activity.startTime) }}</p>
                  <p class="activity-location">地点: {{ activity.location }}</p>
                </div>
                <el-tag :type="getStatusType(activity.status)" effect="light" size="small">{{ activity.status }}</el-tag>
              </div>
            </div>
            <el-empty v-else description="没有已报名的活动"></el-empty>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue';
import { useUserStore } from '@/stores/userStore.js';
import { getServiceRecords, getEnrolledActivities } from '@/services/profileApi.js';
import { ElMessage, ElMessageBox } from 'element-plus';
import { useRouter } from 'vue-router';

const userStore = useUserStore();
const router = useRouter();

const isEditing = ref(false);
const defaultAvatar = 'https://i.pravatar.cc/150';

const activeTab = ref('records');
const serviceRecords = ref([]);
const enrolledActivities = ref([]);
const loadingRecords = ref(false);
const loadingEnrollments = ref(false);

const editForm = ref({
  realName: '',
  email: '',
  phoneNumber: ''
});

watch(() => userStore.currentUser, (newProfile) => {
  if (newProfile) {
    Object.assign(editForm.value, {
      realName: newProfile.realName,
      email: newProfile.email,
      phoneNumber: newProfile.phoneNumber
    });
  }
}, { immediate: true, deep: true });

const fetchServiceRecords = async () => {
  loadingRecords.value = true;
  try {
    serviceRecords.value = await getServiceRecords();
  } catch (error) {
    ElMessage.error('获取时长明细失败，请稍后重试。');
  } finally {
    loadingRecords.value = false;
  }
};

const fetchEnrolledActivities = async () => {
  loadingEnrollments.value = true;
  try {
    enrolledActivities.value = await getEnrolledActivities();
  } catch (error) {
    ElMessage.error('获取报名活动失败，请稍后重试。');
  } finally {
    loadingEnrollments.value = false;
  }
};

onMounted(() => {
  if (!userStore.currentUser) {
    userStore.fetchCurrentUser();
  }
  fetchServiceRecords();
  fetchEnrolledActivities();
});

const startEditing = () => { isEditing.value = true; };

const cancelEditing = () => {
  isEditing.value = false;
  if (userStore.currentUser) {
    Object.assign(editForm.value, {
      realName: userStore.currentUser.realName,
      email: userStore.currentUser.email,
      phoneNumber: userStore.currentUser.phoneNumber
    });
  }
};

const handleSave = async () => {
  try {
    await userStore.updateCurrentUser(editForm.value);
    ElMessage.success('信息更新成功！');
    isEditing.value = false;
  } catch (error) {
    // store中已处理错误提示
  }
};

const handleLogout = () => {
  ElMessageBox.confirm('您确定要退出登录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(() => {
    userStore.logout();
    router.push('/login');
  }).catch(() => {});
};

const formatDateTime = (dateTime) => {
  if (!dateTime) return 'N/A';
  return new Date(dateTime).toLocaleString('zh-CN', { year: 'numeric', month: 'long', day: 'numeric', hour: '2-digit', minute: '2-digit' });
}

const getStatusType = (status) => {
  const statusMap = {
    recruiting: 'success',
    ongoing: 'primary',
    finished: 'info',
    canceled: 'warning',
  };
  return statusMap[status] || 'info';
}
</script>

<style scoped>
/* 整体页面和容器布局 */
.profile-page {
  padding: 2rem;
  background-color: #ffffff; /* 修改1：背景色统一为白色 */
  min-height: calc(100vh - 120px);
}

.profile-container {
  max-width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: 380px 1fr; /* 左侧卡片宽度固定 */
  gap: 2rem;
}

/* 左侧卡片 */
.profile-card {
  background-color: #f8f9fa; /* 修改2：让左侧卡片带一点灰度，形成对比 */
  border-radius: 12px;
  padding: 2rem;
  display: flex;
  flex-direction: column;
  height: fit-content; /* 高度自适应内容 */
}

.avatar-section {
  text-align: center;
  margin-bottom: 1.5rem;
}
.username {
  font-size: 1.5rem;
  font-weight: 600;
  margin-top: 1rem;
}
.user-role {
  font-size: 0.9rem;
  color: #6c757d;
  text-transform: capitalize;
}

/* 时长展示 */
.service-hours-display {
  text-align: center;
  margin: 1.5rem 0;
  padding: 1rem;
  background-color: #e9ecef;
  border-radius: 8px;
}
.hours-value {
  font-size: 2.5rem;
  font-weight: 700;
  color: #0056b3;
}
.hours-label {
  font-size: 0.9rem;
  color: #495057;
}

.info-form .el-form-item {
  margin-bottom: 1rem;
}

.actions {
  margin-top: 1.5rem;
  display: flex;
  justify-content: center;
  gap: 1rem;
}

/* 右侧Tabs */
.details-tabs {
  background-color: #ffffff;
  border-radius: 12px;
  padding: 1.5rem 2rem;
}

.tab-loading {
  padding: 2rem;
  text-align: center;
  color: #6c757d;
}

.timeline-card {
  border-left: 4px solid #0d6efd;
}
.timeline-card h4 {
  font-weight: 600;
}
.hours-badge {
  color: #0d6efd;
  font-weight: bold;
}
.remarks {
  color: #6c757d;
  font-size: 0.9rem;
  margin-top: 0.5rem;
}

.enrolled-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}
.activity-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem;
  border: 1px solid #e9ecef;
  border-radius: 8px;
  transition: box-shadow 0.2s;
}
.activity-item:hover {
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
}
.activity-title {
  font-weight: 500;
  font-size: 1rem;
  margin: 0 0 0.25rem 0;
}
.activity-time, .activity-location {
  font-size: 0.85rem;
  color: #6c757d;
}
.activity-location {
  margin-top: 0.25rem;
}

/* 响应式布局 */
@media (max-width: 992px) {
  .profile-container {
    grid-template-columns: 1fr; /* 在小屏幕上变为单列布局 */
  }
}
</style>