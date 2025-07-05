<template>
  <div class="activity-detail-view">
    <div v-if="loading" class="loading">加载中...</div>
    <div v-else-if="error" class="error">{{ error }}</div>
    <div v-else-if="activity" class="detail-container">
      <!-- 顶部大图横幅 -->
      <div class="banner">
        <img v-if="activity.coverImageUrl" :src="activity.coverImageUrl" class="banner-img" :alt="activity.title" />
        <div class="banner-overlay"></div>
        <div class="banner-title">
          <h1>{{ activity.title }}</h1>
          <div class="tags">
            <span class="tag category">{{ activity.category }}</span>
            <span class="tag status" :class="activity.status">{{ activity.status === 'recruiting' ? '招募中' : '已结束' }}</span>
            <span class="tag organizer" v-if="activity.organizerName">主办方：{{ activity.organizerName }}</span>
          </div>
        </div>
      </div>
      <!-- 信息卡片区块 -->
      <div class="info-cards">
        <div class="info-card">
          <span class="info-label">时间</span>
          <span class="info-value">{{ activity.startTime }} ~ {{ activity.endTime }}</span>
        </div>
        <div class="info-card">
          <span class="info-label">地点</span>
          <span class="info-value">{{ activity.location }}</span>
        </div>
        <div class="info-card">
          <span class="info-label">名额</span>
          <span class="info-value">{{ activity.currentEnrollment }}/{{ activity.recruitmentQuota }}</span>
        </div>
      </div>
      <!-- 描述区块 -->
      <div class="desc-block">
        <h2>活动介绍</h2>
        <div class="desc-content" v-html="activity.description"></div>
      </div>
      <!-- 报名按钮 -->
      <div class="actions-block">
        <button v-if="activity.isEnrolled" class="btn btn-outline" @click="unenroll" :disabled="loadingAction">取消报名</button>
        <button v-else class="btn btn-primary" @click="enroll" :disabled="loadingAction || activity.currentEnrollment >= activity.recruitmentQuota">立即报名</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { getActivityDetail, enrollActivity, unenrollActivity } from '@/services/publicActivityApi';
import { ElMessage } from 'element-plus';

const route = useRoute();
const router = useRouter();
const activity = ref(null);
const loading = ref(true);
const error = ref('');
const loadingAction = ref(false);

const fetchDetail = async () => {
  loading.value = true;
  error.value = '';
  try {
    const id = route.params.id;
    let res = await getActivityDetail(id);
    let data = res?.data ? res.data : res;
    if (data && (data.code === 200 || data.id)) {
      activity.value = data.data || data;
      error.value = '';
    } else {
      error.value = data?.message || '未找到该活动';
    }
  } catch (e) {
    error.value = e?.response?.data?.message || '活动详情加载失败';
  } finally {
    loading.value = false;
  }
};

const enroll = async () => {
  if (!activity.value) return;
  loadingAction.value = true;
  try {
    await enrollActivity(activity.value.id);
    ElMessage.success('报名成功！');
    await fetchDetail();
  } catch (e) {
    ElMessage.error(e?.response?.data?.message || '报名失败');
    error.value = e?.response?.data?.message || '报名失败';
  } finally {
    loadingAction.value = false;
  }
};

const unenroll = async () => {
  if (!activity.value) return;
  loadingAction.value = true;
  try {
    await unenrollActivity(activity.value.id);
    ElMessage.success('已取消报名');
    await fetchDetail();
  } catch (e) {
    ElMessage.error(e?.response?.data?.message || '取消报名失败');
    error.value = e?.response?.data?.message || '取消报名失败';
  } finally {
    loadingAction.value = false;
  }
};

onMounted(fetchDetail);
</script>

<style scoped>
.activity-detail-view {
  max-width: 800px;
  margin: 40px auto;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 4px 24px 0 rgba(59,130,246,0.10);
  overflow: hidden;
  padding: 0 0 32px 0;
}
.banner {
  position: relative;
  width: 100%;
  height: 260px;
  overflow: hidden;
  background: #f3f4f6;
}
.banner-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}
.banner-overlay {
  position: absolute;
  left: 0; right: 0; top: 0; bottom: 0;
  background: linear-gradient(180deg,rgba(0,0,0,0.18) 60%,rgba(0,0,0,0.38) 100%);
  z-index: 1;
}
.banner-title {
  position: absolute;
  left: 32px;
  bottom: 24px;
  z-index: 2;
  color: #fff;
}
.banner-title h1 {
  font-size: 2.2rem;
  font-weight: 700;
  margin-bottom: 10px;
  text-shadow: 0 2px 8px rgba(0,0,0,0.18);
}
.tags {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}
.tag {
  display: inline-block;
  padding: 3px 12px;
  border-radius: 12px;
  font-size: 1rem;
  font-weight: 500;
  background: rgba(255,255,255,0.18);
  color: #fff;
  box-shadow: 0 1px 4px 0 rgba(0,0,0,0.08);
}
.tag.category {
  background: #2563eb;
}
.tag.status.recruiting {
  background: #22c55e;
}
.tag.status:not(.recruiting) {
  background: #d1d5db;
  color: #444;
}
.tag.organizer {
  background: #f59e42;
  color: #fff;
}
.info-cards {
  display: flex;
  gap: 24px;
  justify-content: center;
  margin: 32px 0 0 0;
}
.info-card {
  background: #f7f8fa;
  border-radius: 12px;
  box-shadow: 0 2px 8px 0 rgba(59,130,246,0.04);
  padding: 18px 28px;
  min-width: 160px;
  text-align: center;
}
.info-label {
  display: block;
  font-size: 1.02rem;
  color: #888;
  margin-bottom: 6px;
}
.info-value {
  font-size: 1.18rem;
  font-weight: 600;
  color: #222;
}
.desc-block {
  margin: 36px 36px 0 36px;
  padding: 24px 24px 18px 24px;
  background: #f9fafb;
  border-radius: 12px;
  box-shadow: 0 2px 8px 0 rgba(59,130,246,0.04);
}
.desc-block h2 {
  font-size: 1.25rem;
  font-weight: 600;
  margin-bottom: 12px;
  color: #2563eb;
}
.desc-content {
  font-size: 1.08rem;
  color: #333;
  line-height: 1.7;
}
.actions-block {
  display: flex;
  justify-content: center;
  margin-top: 36px;
  gap: 24px;
}
.btn {
  padding: 12px 38px;
  border-radius: 10px;
  font-size: 1.15rem;
  font-weight: 600;
  cursor: pointer;
  border: none;
  transition: background 0.2s, color 0.2s;
  box-shadow: 0 2px 8px 0 rgba(59,130,246,0.08);
}
.btn-primary {
  background: #2563eb;
  color: #fff;
}
.btn-primary:disabled {
  background: #a5b4fc;
  color: #fff;
  cursor: not-allowed;
}
.btn-outline {
  background: #fff;
  color: #2563eb;
  border: 2px solid #2563eb;
}
.btn-outline:disabled {
  color: #a5b4fc;
  border-color: #a5b4fc;
  cursor: not-allowed;
}
.loading, .error {
  text-align: center;
  color: #888;
  font-size: 1.1rem;
  margin: 40px 0;
}
@media (max-width: 700px) {
  .activity-detail-view {
    max-width: 100vw;
    border-radius: 0;
    padding: 0 0 18px 0;
  }
  .banner-title {
    left: 16px;
    bottom: 12px;
  }
  .info-cards {
    flex-direction: column;
    gap: 12px;
    margin: 24px 0 0 0;
    align-items: center;
  }
  .desc-block {
    margin: 18px 8px 0 8px;
    padding: 14px 8px 10px 8px;
  }
  .actions-block {
    margin-top: 18px;
    gap: 12px;
  }
}
</style>
