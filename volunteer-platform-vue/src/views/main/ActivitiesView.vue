<template>
  <div class="activities-view">
    <div class="page-header">
      <h1 class="page-title">志愿活动列表</h1>
      <p class="page-subtitle">探索并参与我们的志愿活动，一起为社会贡献力量</p>
    </div>

    <div class="filter-bar">
      <div class="filter-row">
        <div class="filter-item search-item">
          <label for="search">搜索</label>
          <div class="search-container">
            <input
                type="text"
                id="search"
                placeholder="搜索活动..."
                v-model="searchTerm"
                class="search-input search-input-large"
            />
            <button type="button" @click="searchActivities" class="search-button search-button-large">
              <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                   stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="search-icon">
                <circle cx="11" cy="11" r="8"></circle>
                <line x1="21" y1="21" x2="16.65" y2="16.65"></line>
              </svg>
            </button>
          </div>
        </div>

        <div class="filter-item page-size-item">
          <label for="pageSize">每页显示</label>
          <select id="pageSize" v-model="itemsPerPage" class="smooth-select page-size-select">
            <option v-for="size in selectedPageItems" :key="size" :value="size">{{ size }}</option>
          </select>
        </div>

        <div class="filter-item">
          <label for="category">活动类别</label>
          <select id="category" v-model="selectedCategory" class="smooth-select">
            <option value="">全部类别</option>
            <option value="环境保护">环境保护</option>
            <option value="教育支持">教育支持</option>
            <option value="关爱老人">关爱老人</option>
            <option value="关爱儿童">关爱儿童</option>
            <option value="校内服务">校内服务</option>
          </select>
        </div>
        <div class="filter-item">
          <label for="location">活动地点</label>
          <select id="location" v-model="selectedLocation" class="smooth-select">
            <option value="">全部</option>
            <option v-for="loc in uniqueLocations" :key="loc" :value="loc">{{ loc }}</option>
          </select>
        </div>

        <div class="filter-item">
          <label for="date">活动日期</label>
          <select id="date" v-model="selectedDate" class="smooth-select">
            <option value="">全部时间</option>
            <option value="today">今天</option>
            <option value="week">本周</option>
            <option value="month">本月</option>
            <option value="future">未来</option>
          </select>
        </div>
      </div>
    </div>

    <div v-if="loading" class="loading">加载中...</div>

    <div v-if="error" class="error">{{ error }}</div>

    <div class="main-content">
      <div class="activities-list">
        <div class="activity-item" v-for="activity in paginatedActivities" :key="activity.id" @click="goToDetail(activity.id)" style="cursor:pointer">
          <div class="activity-image">
            <img :src="activity.coverImageUrl" :alt="activity.title" />
            <div class="activity-category">
              {{ getCategoryName(activity.category) }}
            </div>
          </div>

          <div class="activity-content">
            <div class="activity-header">
              <h2 class="activity-title">{{ activity.title }}</h2>
              <div class="activity-rating">
                <div class="rating-stars">
                  <svg v-for="i in 5" :key="i" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" :fill="i <= 5 ? '#f59e0b' : 'none'" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="star-icon">
                    <polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2"></polygon>
                  </svg>
                </div>
                <span class="rating-number">5 (0 评价)</span>
              </div>
            </div>

            <div class="activity-details">
              <div class="detail-item">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="detail-icon">
                  <rect x="3" y="4" width="18" height="18" rx="2" ry="2"></rect>
                  <line x1="16" y1="2" x2="16" y2="6"></line>
                  <line x1="8" y1="2" x2="8" y2="6"></line>
                  <line x1="3" y1="10" x2="21" y2="10"></line>
                </svg>
                <span>{{ formatDate(activity.startTime) }}</span>
              </div>

              <div class="detail-item">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="detail-icon">
                  <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"></path>
                  <circle cx="12" cy="10" r="3"></circle>
                </svg>
                <span>{{ activity.location }}</span>
              </div>

              <div class="detail-item">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="detail-icon">
                  <circle cx="12" cy="12" r="10"></circle>
                  <polyline points="12 6 12 12 16 14"></polyline>
                </svg>
                <span>{{ getDuration(activity.startTime, activity.endTime) }} 小时</span>
              </div>

              <div class="detail-item">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="detail-icon">
                  <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path>
                  <circle cx="9" cy="7" r="4"></circle>
                  <path d="M23 21v-2a4 4 0 0 0-3-3.87"></path>
                  <path d="M16 3.13a4 4 0 0 1 0 7.75"></path>
                </svg>
                <span>{{ activity.currentEnrollment }}/{{ activity.recruitmentQuota }} 人</span>
              </div>
            </div>

            <p class="activity-description" v-html="activity.description"></p>

            <div class="activity-footer">
              <div class="activity-organizer">
                <img :src="defaultAvatar" alt="主办方" class="organizer-avatar" />
                <span class="organizer-name">主办方</span>
              </div>

              <div class="activity-actions">
                <button type="button" class="btn btn-outline">收藏</button>
                <button
                    type="button"
                    class="btn btn-primary"
                    @click="isEnrolled(activity.id) ? unenrollActivity(activity.id) : enrollActivity(activity.id)"
                    :disabled="loadingEnrollments.includes(activity.id)"
                >
                  {{ isEnrolled(activity.id) ? '取消报名' : '立即报名' }}
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="pagination">
        <button type="button" class="page-btn" :disabled="currentPage === 1" @click="currentPage--">
          上一页
        </button>
        <div class="page-numbers">
          <button type="button" class="page-number" :class="{ active: page === currentPage }" v-for="page in pageNumbers" :key="page" @click="currentPage = page">
            {{ page }}
          </button>
        </div>
        <button type="button" class="page-btn" :disabled="currentPage === totalPages" @click="currentPage++">
          下一页
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { getPublicActivities } from '@/services/publicActivityApi';
import { useRouter } from 'vue-router';

const activities = ref([]);
const loading = ref(false);
const error = ref('');
const selectedCategory = ref('');
const selectedLocation = ref('');
const selectedDate = ref('');
const searchTerm = ref('');
const itemsPerPage = ref(3);
const selectedPageItems = [3, 6, 9, 12];
const currentPage = ref(1);
const loadingEnrollments = ref([]);
const enrolledActivities = ref([]);
const defaultAvatar = 'https://avatars.githubusercontent.com/u/9919?s=200&v=4';
const router = useRouter();

const filterTags = computed(() => {
  const tags = [];
  if (selectedCategory.value) tags.push({ key: 'category', text: `分类：${getCategoryName(selectedCategory.value)}` });
  if (selectedLocation.value) tags.push({ key: 'location', text: `地点：${getLocationName(selectedLocation.value)}` });
  if (selectedDate.value) tags.push({ key: 'date', text: `时间：${getDateName(selectedDate.value)}` });
  if (searchTerm.value) tags.push({ key: 'search', text: `搜索：“${searchTerm.value}”` });
  return tags;
});

const removeTag = (key) => {
  switch (key) {
    case 'category': selectedCategory.value = ''; break;
    case 'location': selectedLocation.value = ''; break;
    case 'date': selectedDate.value = ''; break;
    case 'search': searchTerm.value = ''; break;
  }
};

const getCategoryName = (category) => category;
const getLocationName = (location) => {
  const locations = {
    'beijing': '北京',
    'shanghai': '上海',
    'guangzhou': '广州',
    'shenzhen': '深圳',
    'hangzhou': '杭州',
    '学校图书馆三楼': '学校图书馆三楼',
  };
  return locations[location] || location;
};

const getDateName = (date) => {
  const dates = {
    'today': '今天',
    'week': '本周',
    'month': '本月',
    'future': '未来'
  };
  return dates[date] || date;
};

const formatDate = (dateString) => {
  if (!dateString) return '';
  const date = new Date(dateString);
  if (isNaN(date.getTime())) return dateString;
  return date.toLocaleString('zh-CN', { year: 'numeric', month: 'long', day: 'numeric', hour: '2-digit', minute: '2-digit' });
};

const getDuration = (start, end) => {
  if (!start || !end) return 0;
  const s = new Date(start);
  const e = new Date(end);
  const diff = (e - s) / (1000 * 60 * 60);
  return Math.max(1, Math.round(diff));
};

const filteredActivities = computed(() => {
  let result = [...activities.value];
  if (selectedCategory.value) {
    result = result.filter(a => a.category === selectedCategory.value);
  }
  if (selectedLocation.value) {
    result = result.filter(a => a.location && (a.location.includes(selectedLocation.value) || getLocationName(a.location) === getLocationName(selectedLocation.value)));
  }
  if (selectedDate.value) {
    const today = new Date();
    today.setHours(0,0,0,0);
    const activityDate = (d) => { const date = new Date(d); date.setHours(0,0,0,0); return date; };
    switch (selectedDate.value) {
      case 'today':
        result = result.filter(a => activityDate(a.startTime).getTime() === today.getTime());
        break;
      case 'week': {
        const nextWeek = new Date(today);
        nextWeek.setDate(today.getDate() + 7);
        result = result.filter(a => { const d = activityDate(a.startTime); return d >= today && d <= nextWeek; });
        break;
      }
      case 'month': {
        const nextMonth = new Date(today);
        nextMonth.setMonth(today.getMonth() + 1);
        result = result.filter(a => { const d = activityDate(a.startTime); return d >= today && d <= nextMonth; });
        break;
      }
      case 'future':
        result = result.filter(a => new Date(a.startTime) > today);
        break;
    }
  }
  if (searchTerm.value) {
    const term = searchTerm.value.toLowerCase();
    result = result.filter(a => a.title.toLowerCase().includes(term) || (a.description && a.description.toLowerCase().includes(term)));
  }
  return result;
});

const totalPages = computed(() => Math.ceil(filteredActivities.value.length / itemsPerPage.value) || 1);
const paginatedActivities = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage.value;
  return filteredActivities.value.slice(start, start + itemsPerPage.value);
});

const pageNumbers = computed(() => {
  const maxVisible = 5;
  let start = Math.max(1, currentPage.value - Math.floor(maxVisible / 2));
  let end = start + maxVisible - 1;
  if (end > totalPages.value) { end = totalPages.value; start = Math.max(1, end - maxVisible + 1); }
  return Array.from({ length: end - start + 1 }, (_, i) => start + i);
});

const searchActivities = () => { currentPage.value = 1; };
const clearFilters = () => {
  selectedCategory.value = '';
  selectedLocation.value = '';
  selectedDate.value = '';
  searchTerm.value = '';
  currentPage.value = 1;
};

const isEnrolled = (activityId) => enrolledActivities.value.includes(activityId);
const enrollActivity = async (activityId) => {
  try {
    loadingEnrollments.value.push(activityId);
    const activity = activities.value.find(a => a.id === activityId);
    if (activity && activity.currentEnrollment < activity.recruitmentQuota) {
      activity.currentEnrollment += 1;
      enrolledActivities.value.push(activityId);
    }
  } finally {
    loadingEnrollments.value = loadingEnrollments.value.filter(id => id !== activityId);
  }
};

const unenrollActivity = async (activityId) => {
  try {
    loadingEnrollments.value.push(activityId);
    const activity = activities.value.find(a => a.id === activityId);
    if (activity && activity.currentEnrollment > 0) {
      activity.currentEnrollment -= 1;
      enrolledActivities.value = enrolledActivities.value.filter(id => id !== activityId);
    }
  } finally {
    loadingEnrollments.value = loadingEnrollments.value.filter(id => id !== activityId);
  }
};

const uniqueLocations = computed(() => {
  const set = new Set();
  activities.value.forEach(a => {
    if (a.location && a.location.trim()) set.add(a.location.trim());
  });
  return Array.from(set);
});

const goToDetail = (id) => {
  router.push({ name: 'ActivityDetail', params: { id } });
};

onMounted(async () => {
  loading.value = true;
  error.value = '';
  try {
    const res = await getPublicActivities(1, 20);
    let list = [];
    if (res && res.data && res.data.data && Array.isArray(res.data.data.list)) {
      list = res.data.data.list;
    } else if (Array.isArray(res.list)) {
      list = res.list;
    } else if (Array.isArray(res)) {
      list = res;
    } else if (res && typeof res === 'object') {
      list = [res];
    }
    activities.value = list;
  } catch (e) {
    error.value = '活动数据加载失败';
  } finally {
    loading.value = false;
  }
});
</script>

<style scoped>
body, .activities-view { background: #f7f8fa; min-height: 100vh; }
.page-header { text-align: center; margin-bottom: 32px; padding: 32px 0 16px 0; background: #fff; border-radius: 0 0 16px 16px; box-shadow: 0 2px 8px 0 rgba(59,130,246,0.04); color: #222; position: relative; }
.page-title { font-size: 2.2rem; font-weight: 700; letter-spacing: 1px; text-shadow: none; display: flex; align-items: center; justify-content: center; gap: 12px; }
.page-subtitle { font-size: 1rem; margin-top: 8px; color: #888; text-shadow: none; }
.filter-bar { margin-bottom: 24px; }
.filter-row { display: flex; flex-wrap: wrap; gap: 16px; align-items: flex-end; justify-content: center; }
.filter-item { display: flex; flex-direction: column; min-width: 120px; }
.search-item { flex: 1; min-width: 200px; max-width: 320px; }
.filter-item.page-size-item { margin-left: 24px; }
@media (max-width: 900px) {
  .filter-row { flex-direction: column; gap: 8px; }
  .filter-item, .search-item { min-width: 0; width: 100%; margin-left: 0 !important; }
}
.filter-item label { font-size: 0.98rem; font-weight: 500; color: #444; margin-bottom: 4px; text-align: left; letter-spacing: 0.2px; }
.smooth-select, .search-input { border-radius: 6px; border: 1px solid #d1d5db; background: #fff; font-size: 1rem; padding: 8px 10px; transition: box-shadow 0.2s, border-color 0.2s; box-shadow: none; width: 100%; }
.smooth-select:focus, .search-input:focus { border-color: #2563eb; box-shadow: 0 0 0 2px #2563eb22; }
.search-container { position: relative; width: 100%; }
.search-button { position: absolute; right: 6px; top: 50%; transform: translateY(-50%); background: #2563eb; border: none; border-radius: 50%; width: 28px; height: 28px; display: flex; align-items: center; justify-content: center; color: #fff; box-shadow: none; cursor: pointer; transition: background 0.2s; }
.search-button:hover { background: #1d4ed8; }
.activities-list { display: grid; gap: 32px; margin-bottom: 48px; grid-template-columns: 1fr; max-width: 1100px; margin-left: auto; margin-right: auto; }
.activity-item { display: flex; flex-direction: row; border-radius: 14px; overflow: hidden; background: #fff; box-shadow: 0 4px 16px 0 rgba(59,130,246,0.06); border: 1.5px solid #e5e7eb; position: relative; min-height: 220px; }
.activity-image { position: relative; width: 200px; min-width: 160px; height: 200px; flex-shrink: 0; overflow: hidden; border-radius: 0 14px 14px 0; display: flex; align-items: center; justify-content: center; margin-top: 24px; margin-bottom: 24px; }
.activity-image img { width: 90%; height: 90%; object-fit: cover; border-radius: 12px; display: block; margin: 0 auto; }
.activity-category { position: absolute; top: 10px; left: 10px; background: #2563eb; color: #fff; font-size: 0.92rem; font-weight: 500; padding: 2px 10px; border-radius: 8px; box-shadow: none; letter-spacing: 0.2px; }
.activity-content { flex: 1; padding: 14px 14px 10px 14px; display: flex; flex-direction: column; justify-content: space-between; min-width: 0; }
.activity-header { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 6px; }
.activity-title { font-size: 1.1rem; font-weight: 600; color: #222; margin: 0; letter-spacing: 0.2px; line-height: 1.5; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; text-overflow: ellipsis; min-height: calc(1.1rem * 1.5 * 2); }
.activity-rating { display: flex; align-items: center; gap: 6px; }
.rating-stars { display: flex; }
.star-icon { width: 14px; height: 14px; fill: #fbbf24; color: #fbbf24; filter: none; transition: none; }
.rating-number { font-size: 0.92rem; color: #888; }
.activity-details { display: flex; flex-wrap: wrap; gap: 8px; margin-bottom: 8px; }
.detail-item { display: flex; align-items: center; gap: 4px; font-size: 0.95rem; color: #444; background: #f3f4f6; border-radius: 6px; padding: 2px 8px; box-shadow: none; }
.detail-icon { width: 14px; height: 14px; color: #2563eb; }
.activity-description { flex: 1; color: #444; line-height: 1.5; margin-bottom: 8px; font-size: 0.95rem; background: #f7f8fa; border-radius: 4px; padding: 4px 8px; box-shadow: none; overflow: hidden; text-overflow: ellipsis; max-height: 48px; }
.activity-footer { display: flex; justify-content: space-between; align-items: center; margin-top: 4px; }
.activity-organizer { display: flex; align-items: center; gap: 6px; }
.organizer-avatar { width: 28px; height: 28px; border-radius: 50%; object-fit: cover; border: 1px solid #e5e7eb; box-shadow: none; }
.organizer-name { font-size: 0.95rem; color: #888; font-weight: 500; }
.activity-actions { display: flex; gap: 6px; }
.btn { padding: 6px 14px; border-radius: 8px; font-weight: 500; font-size: 0.95rem; transition: all 0.2s; box-shadow: none; border: none; cursor: pointer; }
.btn-outline { background: #fff; color: #2563eb; border: 1px solid #2563eb; }
.btn-outline:hover { background: #f3f4f6; color: #1d4ed8; border-color: #1d4ed8; }
.btn-primary { background: #2563eb; color: #fff; }
.btn-primary:hover { background: #1d4ed8; color: #fff; }
.pagination { display: flex; justify-content: center; align-items: center; gap: 10px; margin-bottom: 40px; }
.page-btn, .page-number { background: #fff; color: #2563eb; border: 1px solid #e5e7eb; padding: 6px 14px; border-radius: 8px; font-weight: 500; font-size: 0.95rem; cursor: pointer; transition: background 0.2s, color 0.2s, border-color 0.2s; box-shadow: none; }
.page-btn:hover, .page-number:hover { background: #f3f4f6; color: #1d4ed8; border-color: #1d4ed8; }
.page-btn:disabled { opacity: 0.5; cursor: not-allowed; }
.page-number.active { background: #2563eb; color: #fff; border-color: #2563eb; }
@media (max-width: 900px) {
  .activities-list { grid-template-columns: 1fr; }
  .activity-item { flex-direction: column; align-items: stretch; min-width: 0; min-height: 0; }
  .activity-image { width: 100%; height: 180px; border-radius: 14px 14px 0 0; margin-top: 16px; margin-bottom: 0; }
  .activity-image img { border-radius: 12px; width: 90%; height: 90%; }
}
</style>