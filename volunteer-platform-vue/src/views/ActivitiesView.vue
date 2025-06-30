<template>
  <div class="activities-view">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1 class="page-title">志愿活动列表</h1>
      <p class="page-subtitle">探索并参与我们的志愿活动，一起为社会贡献力量</p>
    </div>

    <!-- 筛选和搜索区域 -->
    <div class="filter-section">
      <!-- 筛选条件 -->
      <div class="filter-row">
        <div class="filter-item category">
          <label for="category">活动类别</label>
          <select id="category" v-model="selectedCategory" class="smooth-select">
            <option value="">全部类别</option>
            <option value="environment">环境保护</option>
            <option value="education">教育支持</option>
            <option value="elderly">关爱老人</option>
            <option value="children">关爱儿童</option>
            <option value="disaster">灾害救援</option>
          </select>
        </div>

        <div class="filter-item location">
          <label for="location">活动地点</label>
          <select id="location" v-model="selectedLocation" class="smooth-select">
            <option value="">全部地区</option>
            <option value="beijing">北京</option>
            <option value="shanghai">上海</option>
            <option value="guangzhou">广州</option>
            <option value="shenzhen">深圳</option>
            <option value="hangzhou">杭州</option>
          </select>
        </div>

        <div class="filter-item date">
          <label for="date">活动日期</label>
          <select id="date" v-model="selectedDate" class="smooth-select">
            <option value="">全部时间</option>
            <option value="today">今天</option>
            <option value="week">本周</option>
            <option value="month">本月</option>
            <option value="future">未来</option>
          </select>
        </div>

        <div class="filter-item search-box">
          <label for="search">搜索</label>
          <div class="search-container">
            <input
                type="text"
                id="search"
                placeholder="搜索活动..."
                v-model="searchTerm"
                class="search-input"
            />
            <button type="button" @click="searchActivities" class="search-button">
              <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                   stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="search-icon">
                <circle cx="11" cy="11" r="8"></circle>
                <line x1="21" y1="21" x2="16.65" y2="16.65"></line>
              </svg>
            </button>
          </div>
        </div>
      </div>

      <!-- 已选标签 -->
      <div class="filter-tags">
        <transition-group name="fade-slide" mode="out-in">
          <span v-for="(tag, index) in filterTags" :key="index" class="filter-tag">
            {{ tag.text }}
            <button type="button" @click="removeTag(tag.key)" class="tag-remove">×</button>
          </span>
        </transition-group>
        <!-- 使用 v-if 控制显示状态 -->
        <button v-if="filterTags.length > 0" type="button" class="clear-all" @click="clearFilters">清除全部</button>
      </div>

      <!-- 分页大小控制 -->
      <div class="page-size-control">
      <label for="pageSize">每页显示:</label>
      <select id="pageSize" v-model="itemsPerPage" class="smooth-select page-size-select">
        <option v-for="size in selectedPageItems" :key="size" :value="size">{{ size }}</option>
      </select>
    </div>
    </div>

    <!-- 加载状态 -->
    <div v-if="loading" class="loading">加载中...</div>

    <!-- 错误提示 -->
    <div v-if="error" class="error">{{ error }}</div>

    <!-- 活动列表 -->
    <div class="activities-list">
      <div class="activity-item" v-for="activity in paginatedActivities" :key="activity.id">
        <div class="activity-image">
          <img :src="activity.image" :alt="activity.title" />
          <div class="activity-category">
            {{ getCategoryName(activity.category) }}
          </div>
        </div>

        <div class="activity-content">
          <div class="activity-header">
            <h2 class="activity-title">{{ activity.title }}</h2>
            <div class="activity-rating">
              <div class="rating-stars">
                <svg v-for="i in 5" :key="i" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" :fill="i <= activity.rating ? '#f59e0b' : 'none'" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="star-icon">
                  <polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2"></polygon>
                </svg>
              </div>
              <span class="rating-number">{{ activity.rating }} ({{ activity.reviews }} 评价)</span>
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
              <span>{{ formatDate(activity.date) }}</span>
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
              <span>{{ activity.duration }} 小时</span>
            </div>

            <div class="detail-item">
              <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="detail-icon">
                <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path>
                <circle cx="9" cy="7" r="4"></circle>
                <path d="M23 21v-2a4 4 0 0 0-3-3.87"></path>
                <path d="M16 3.13a4 4 0 0 1 0 7.75"></path>
              </svg>
              <span>{{ activity.participants }}/{{ activity.maxParticipants }} 人</span>
            </div>
          </div>

          <p class="activity-description">{{ activity.description }}</p>

          <div class="activity-footer">
            <div class="activity-organizer">
              <img :src="activity.organizer.avatar" :alt="activity.organizer.name" class="organizer-avatar" />
              <span class="organizer-name">{{ activity.organizer.name }}</span>
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

    <!-- 分页 -->
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

    <!-- 推荐活动 -->
    <div class="recommended-activities">
      <h2 class="section-title">你可能也感兴趣</h2>
      <div class="recommended-list">
        <div class="recommended-item" v-for="activity in recommendedActivities" :key="activity.id">
          <img :src="activity.image" :alt="activity.title" class="recommended-image" />
          <h3 class="recommended-title">{{ activity.title }}</h3>
          <p class="recommended-date">{{ formatDate(activity.date) }}</p>
        </div>
      </div>
    </div>
  </div>
</template>


<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
const filterTags = computed(() => {
  const tags = [];

  if (selectedCategory.value)
    tags.push({ key: 'category', text: `分类：${getCategoryName(selectedCategory.value)}` });

  if (selectedLocation.value)
    tags.push({ key: 'location', text: `地点：${getLocationName(selectedLocation.value)}` });

  if (selectedDate.value)
    tags.push({ key: 'date', text: `时间：${getDateName(selectedDate.value)}` });

  if (searchTerm.value)
    tags.push({ key: 'search', text: `搜索：“${searchTerm.value}”` });

  return tags;
});

// 删除标签的统一方法
const removeTag = (key) => {
  switch (key) {
    case 'category': selectedCategory.value = ''; break;
    case 'location': selectedLocation.value = ''; break;
    case 'date': selectedDate.value = ''; break;
    case 'search': searchTerm.value = ''; break;
  }
};

// Vue Router 实例
const router = useRouter();

// 模拟活动数据
const activities = ref([
  {
    id: 1,
    title: "城市公园清洁日",
    category: "environment",
    image: "https://picsum.photos/id/1018/800/500",
    date: "2023-10-15",
    location: "北京市朝阳区奥林匹克公园",
    duration: 4,
    participants: 25,
    maxParticipants: 50,
    description: "加入我们的城市公园清洁日活动，一起为创造更美好的环境贡献力量。活动包括垃圾分类宣传、公园垃圾清理等环节。",
    rating: 4.8,
    reviews: 124,
    organizer: {
      name: "绿色北京环保协会",
      avatar: "https://picsum.photos/id/1005/200/200"
    }
  },
  {
    id: 2,
    title: "山区小学支教活动",
    category: "education",
    image: "https://picsum.photos/id/1019/800/500",
    date: "2023-10-20",
    location: "河北省承德市围场满族蒙古族自治县",
    duration: 10,
    participants: 12,
    maxParticipants: 20,
    description: "前往山区小学进行为期两天的支教活动，教授基础课程、开展趣味活动，为山区儿童带来知识和欢乐。",
    rating: 4.9,
    reviews: 87,
    organizer: {
      name: "爱心支教联盟",
      avatar: "https://picsum.photos/id/1012/200/200"
    }
  },
  {
    id: 3,
    title: "敬老院陪伴日",
    category: "elderly",
    image: "https://picsum.photos/id/1025/800/500",
    date: "2023-10-16",
    location: "上海市浦东新区康桥镇敬老院",
    duration: 3,
    participants: 18,
    maxParticipants: 30,
    description: "在敬老院与老人们聊天、表演节目、陪他们度过愉快的时光。用我们的陪伴，温暖每一位老人的心。",
    rating: 4.7,
    reviews: 105,
    organizer: {
      name: "夕阳红关爱协会",
      avatar: "https://picsum.photos/id/1027/200/200"
    }
  },
  {
    id: 4,
    title: "自闭症儿童关爱活动",
    category: "children",
    image: "https://picsum.photos/id/1028/800/500",
    date: "2023-10-22",
    location: "广州市天河区特殊教育学校",
    duration: 5,
    participants: 15,
    maxParticipants: 25,
    description: "通过游戏、手工制作等活动，与自闭症儿童建立联系，帮助他们融入社会，感受关爱与温暖。",
    rating: 4.8,
    reviews: 93,
    organizer: {
      name: "星星点灯关爱中心",
      avatar: "https://picsum.photos/id/1029/200/200"
    }
  },
  {
    id: 5,
    title: "社区环保宣传活动",
    category: "environment",
    image: "https://picsum.photos/id/1039/800/500",
    date: "2023-10-18",
    location: "深圳市南山区科技园社区",
    duration: 4,
    participants: 22,
    maxParticipants: 40,
    description: "在社区开展环保知识宣传活动，包括垃圾分类指导、节能减排讲座、环保手工制作等内容。",
    rating: 4.6,
    reviews: 78,
    organizer: {
      name: "绿色家园社区",
      avatar: "https://picsum.photos/id/1033/200/200"
    }
  },
  {
    id: 6,
    title: "图书捐赠与整理活动",
    category: "education",
    image: "https://picsum.photos/id/1042/800/500",
    date: "2023-10-25",
    location: "杭州市西湖区图书馆",
    duration: 6,
    participants: 8,
    maxParticipants: 15,
    description: "收集并整理适合不同年龄段阅读的书籍，捐赠给需要的学校和社区图书馆，让更多人能够享受阅读的乐趣。",
    rating: 4.5,
    reviews: 65,
    organizer: {
      name: "阅读点亮未来",
      avatar: "https://picsum.photos/id/1040/200/200"
    }
  }
]);

// 推荐活动数据
const recommendedActivities = ref([
  {
    id: 7,
    title: "流浪动物救助站志愿者",
    category: "animal",
    image: "https://picsum.photos/id/1062/400/300",
    date: "2023-10-28",
    location: "北京市海淀区流浪动物救助中心"
  },
  {
    id: 8,
    title: "马拉松赛事志愿者",
    category: "sports",
    image: "https://picsum.photos/id/1058/400/300",
    date: "2023-11-05",
    location: "上海市浦东新区世纪公园"
  },
  {
    id: 9,
    title: "博物馆讲解员培训",
    category: "culture",
    image: "https://picsum.photos/id/1071/400/300",
    date: "2023-10-29",
    location: "中国国家博物馆"
  }
]);

// 筛选状态
const selectedCategory = ref('');
const selectedLocation = ref('');
const selectedDate = ref('');
const searchTerm = ref('');

// 分页状态
const itemsPerPage = ref(3);
const selectedPageItems = [3, 6, 9, 12];
const currentPage = ref(1);

// 加载状态和错误信息
const loading = ref(false);
const error = ref(null);
const loadingEnrollments = ref([]);

// 用户报名的活动ID列表
const enrolledActivities = ref([]);

// 格式化日期
const formatDate = (dateString) => {
  const options = { year: 'numeric', month: 'long', day: 'numeric' };
  return new Date(dateString).toLocaleDateString('zh-CN', options);
};

// 获取分类名称
const getCategoryName = (category) => {
  const categories = {
    'environment': '环境保护',
    'education': '教育支持',
    'elderly': '关爱老人',
    'children': '关爱儿童',
    'disaster': '灾害救援',
    'animal': '动物保护',
    'sports': '体育赛事',
    'culture': '文化传播'
  };
  return categories[category] || category;
};

// 获取地点名称
const getLocationName = (location) => {
  const locations = {
    'beijing': '北京',
    'shanghai': '上海',
    'guangzhou': '广州',
    'shenzhen': '深圳',
    'hangzhou': '杭州'
  };
  return locations[location] || location;
};

// 获取日期筛选名称
const getDateName = (date) => {
  const dates = {
    'today': '今天',
    'week': '本周',
    'month': '本月',
    'future': '未来'
  };
  return dates[date] || date;
};

// 筛选活动
const filteredActivities = computed(() => {
  let result = [...activities.value];

  // 按分类筛选
  if (selectedCategory.value) {
    result = result.filter(activity => activity.category === selectedCategory.value);
  }

  // 按地点筛选
  if (selectedLocation.value) {
    result = result.filter(activity => activity.location.includes(getLocationName(selectedLocation.value)));
  }

  // 按日期筛选
  if (selectedDate.value) {
    const today = new Date();
    today.setHours(0, 0, 0, 0);

    const activityDate = (dateString) => {
      const date = new Date(dateString);
      date.setHours(0, 0, 0, 0);
      return date;
    };

    switch (selectedDate.value) {
      case 'today':
        result = result.filter(activity => activityDate(activity.date).getTime() === today.getTime());
        break;
      case 'week':
        const nextWeek = new Date(today);
        nextWeek.setDate(today.getDate() + 7);
        result = result.filter(activity => {
          const date = activityDate(activity.date);
          return date >= today && date <= nextWeek;
        });
        break;
      case 'month':
        const nextMonth = new Date(today);
        nextMonth.setMonth(today.getMonth() + 1);
        result = result.filter(activity => {
          const date = activityDate(activity.date);
          return date >= today && date <= nextMonth;
        });
        break;
      case 'future':
        result = result.filter(activity => activityDate(activity.date) > today);
        break;
    }
  }

  // 按搜索词筛选
  if (searchTerm.value) {
    const term = searchTerm.value.toLowerCase();
    result = result.filter(activity =>
        activity.title.toLowerCase().includes(term) ||
        activity.description.toLowerCase().includes(term) ||
        activity.organizer.name.toLowerCase().includes(term)
    );
  }

  return result;
});

// 分页相关计算
const totalPages = computed(() => {
  return Math.ceil(filteredActivities.value.length / itemsPerPage.value);
});

const paginatedActivities = computed(() => {
  const startIndex = (currentPage.value - 1) * itemsPerPage.value;
  return filteredActivities.value.slice(startIndex, startIndex + itemsPerPage.value);
});

const pageNumbers = computed(() => {
  // 显示当前页附近的页码
  const maxVisiblePages = 5;
  let startPage = Math.max(1, currentPage.value - Math.floor(maxVisiblePages / 2));
  let endPage = startPage + maxVisiblePages - 1;

  if (endPage > totalPages.value) {
    endPage = totalPages.value;
    startPage = Math.max(1, endPage - maxVisiblePages + 1);
  }

  return Array.from({ length: endPage - startPage + 1 }, (_, i) => startPage + i);
});

// 搜索活动
const searchActivities = () => {
  // 搜索逻辑已在 computed 中实现
  currentPage.value = 1; // 重置到第一页
};

// 清除筛选条件
const clearFilters = () => {
  selectedCategory.value = '';
  selectedLocation.value = '';
  selectedDate.value = '';
  searchTerm.value = '';
  currentPage.value = 1;
};

// 检查用户是否已报名某个活动
const isEnrolled = (activityId) => {
  return enrolledActivities.value.includes(activityId);
};

// 用户报名活动
const enrollActivity = async (activityId) => {
  try {
    loadingEnrollments.value.push(activityId);

    // 调用API报名活动
    const response = await fetch(`/api/activities/${activityId}/enroll`, {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    });

    if (response.ok) {
      // 更新本地活动信息
      const activity = activities.value.find(a => a.id === activityId);
      if (activity && activity.participants < activity.maxParticipants) {
        activity.participants += 1;
        enrolledActivities.value.push(activityId);
      }
    } else {
      const data = await response.json();
      throw new Error(data.message || '报名失败');
    }
  } catch (err) {
    console.error('报名失败:', err);
    alert(`报名失败：${err.message}`);
  } finally {
    loadingEnrollments.value = loadingEnrollments.value.filter(id => id !== activityId);
  }
};

// 用户取消报名活动
const unenrollActivity = async (activityId) => {
  try {
    loadingEnrollments.value.push(activityId);

    // 调用API取消报名
    const response = await fetch(`/api/activities/${activityId}/enroll`, {
      method: 'DELETE',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    });

    if (response.ok) {
      // 更新本地活动信息
      const activity = activities.value.find(a => a.id === activityId);
      if (activity && activity.participants > 0) {
        activity.participants -= 1;
        enrolledActivities.value = enrolledActivities.value.filter(id => id !== activityId);
      }
    } else {
      const data = await response.json();
      throw new Error(data.message || '取消报名失败');
    }
  } catch (err) {
    console.error('取消报名失败:', err);
    alert(`取消报名失败：${err.message}`);
  } finally {
    loadingEnrollments.value = loadingEnrollments.value.filter(id => id !== activityId);
  }
};

// 初始化
onMounted(async () => {
  loading.value = true;
  try {
    // 这里可以添加实际从API获取数据的逻辑
    // const response = await fetch('/api/activities');
    // if (!response.ok) throw new Error('网络响应失败');
    // const data = await response.json();
    // activities.value = data.content;
  } catch (err) {
    error.value = err.message;
  } finally {
    loading.value = false;
  }
});
</script>

<style scoped>

.page-size-control {
  display: flex;
  align-items: center;
  gap: 8px; /* 调整 label 和 select 之间的间距 */
  margin-top: 10px;
}

.page-size-control label {
  font-size: 0.9rem;
  color: #475569;
  white-space: nowrap; /* 防止 label 内容换行 */
}

/* 筛选区域整体 */
.filter-section {
  margin-bottom: 30px;
}

/* 筛选条件行 */
.filter-row {
  display: flex;
  flex-wrap: nowrap;
  gap: 15px;
  margin-bottom: 15px;
}

/* 单个筛选项 */
.filter-item {
  flex: 1 1 200px;
  min-width: 150px;
  max-width: 250px;
  display: flex;
  flex-direction: column;
}

.filter-item label {
  font-size: 0.9rem;
  font-weight: 500;
  color: #475569;
  margin-bottom: 5px;
}

/* 下拉选择器样式优化 */
.smooth-select {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #cbd5e1;
  border-radius: 6px;
  font-size: 0.95rem;
  color: #334155;
  background-color: white;
  transition: all 0.2s ease;
  appearance: none;
  background-image: url("data:image/svg+xml;charset=US-ASCII,%3Csvg xmlns='http://www.w3.org/2000/svg' width='12' height='12' viewBox='0 0 24 24'%3E%3Cpath d='M7 10l5 5 5-5z' fill='%2364748b'/%3E%3C/svg%3E");
  background-repeat: no-repeat;
  background-position: right 10px center;
  background-size: 12px;
}

.smooth-select:focus {
  outline: none;
  border-color: #2563eb;
  box-shadow: 0 0 0 2px rgba(37, 99, 235, 0.2);
}

/* 搜索框容器 */
.search-container {
  position: relative;
  display: flex;
  align-items: center;
}

/* 搜索输入框 */
.search-input {
  flex: 1;
  padding: 10px 12px;
  border: 1px solid #cbd5e1;
  border-radius: 6px;
  font-size: 0.95rem;
  transition: all 0.2s ease;
  width: 100%;
}

.search-input:focus {
  outline: none;
  border-color: #2563eb;
  box-shadow: 0 0 0 2px rgba(37, 99, 235, 0.2);
}

/* 搜索按钮 */
.search-button {
  margin-left: -30px;
  z-index: 1;
  background: none;
  border: none;
  cursor: pointer;
  color: #64748b;
  transition: color 0.2s ease;
}

.search-button:hover {
  color: #2563eb;
}

.search-icon {
  width: 18px;
  height: 18px;
}

/* 筛选标签 */
.filter-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  padding: 10px 0;
  min-height: 36px;
}

.filter-tag {
  display: inline-flex;
  align-items: center;
  padding: 5px 10px;
  background-color: #f1f5f9;
  border-radius: 4px;
  color: #334155;
  font-size: 0.9rem;
  transition: all 0.2s ease;
}

.filter-tag:hover {
  background-color: #e2e8f0;
}

.tag-remove {
  background: none;
  border: none;
  margin-left: 6px;
  cursor: pointer;
  color: #64748b;
  font-weight: bold;
  transition: color 0.2s ease;
}

.tag-remove:hover {
  color: #ef4444;
}

.clear-all {
  background: none;
  border: none;
  color: #2563eb;
  font-size: 0.9rem;
  cursor: pointer;
  transition: color 0.2s ease;
}

.clear-all:hover {
  text-decoration: underline;
}

/* 分页大小选择器 */
.page-size-control {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-top: 10px;
}

.page-size-control label {
  font-size: 0.9rem;
  color: #475569;
}

.page-size-select {
  width: 60px;
}

/* 过渡动画 */
.fade-slide-enter-active,
.fade-slide-leave-active {
  transition: all 0.3s ease;
}
.fade-slide-enter-from,
.fade-slide-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .filter-row {
    display: flex;
    justify-content: space-between; /* 均匀分布每个筛选项 */
    gap: 15px;
    margin-bottom: 15px;
  }

  .filter-item {
    flex: 1; /* 每个筛选项占据相等的空间 */
    min-width: 0; /* 允许内容换行 */
    display: flex;
    flex-direction: column;
  }


  .page-size-control {
    display: flex;
    align-items: center;
    gap: 8px;
    margin-top: 10px;
    flex-wrap: nowrap; /* 确保内容不换行 */
  }
}

/* 筛选项 */
.filter-row .filter-item {
  flex: 1 1 200px;
  min-width: 200px;
  display: flex;
  flex-direction: column;
  transition: all 0.2s ease;
}

.filter-row .filter-item:hover {
  transform: translateY(-2px);
}

/* 下拉选择器样式优化 */
.smooth-select {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #cbd5e1;
  border-radius: 6px;
  font-size: 0.95rem;
  color: #334155;
  background-color: white;
  transition: all 0.2s ease;
  appearance: none;
  background-image: url("data:image/svg+xml;charset=US-ASCII,%3Csvg xmlns='http://www.w3.org/2000/svg' width='12' height='12' viewBox='0 0 24 24'%3E%3Cpath d='M7 10l5 5 5-5z' fill='%2364748b'/%3E%3C/svg%3E");
  background-repeat: no-repeat;
  background-position: right 10px center;
  background-size: 12px;
}

.smooth-select:focus {
  outline: none;
  border-color: #2563eb;
  box-shadow: 0 0 0 2px rgba(37, 99, 235, 0.2);
}

/* 搜索框容器 */
.search-container {
  position: relative;
  display: flex;
  align-items: center;
}

/* 搜索输入框 */
.search-input {
  flex: 1;
  padding: 10px 12px;
  border: 1px solid #cbd5e1;
  border-radius: 6px;
  font-size: 0.95rem;
  transition: all 0.2s ease;
  width: 100%;
}

.search-input:focus {
  outline: none;
  border-color: #2563eb;
  box-shadow: 0 0 0 2px rgba(37, 99, 235, 0.2);
}

/* 搜索按钮 */
.search-button {
  margin-left: -30px;
  z-index: 1;
  background: none;
  border: none;
  cursor: pointer;
  color: #64748b;
  transition: color 0.2s ease;
}

.search-button:hover {
  color: #2563eb;
}

.search-icon {
  width: 18px;
  height: 18px;
}

/* 筛选标签 */
.filter-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  padding: 10px 0;
  min-height: 36px;
}

.filter-tag {
  display: inline-flex;
  align-items: center;
  padding: 5px 10px;
  background-color: #f1f5f9;
  border-radius: 4px;
  color: #334155;
  font-size: 0.9rem;
  transition: all 0.2s ease;
}

.filter-tag:hover {
  background-color: #e2e8f0;
}

.tag-remove {
  background: none;
  border: none;
  margin-left: 6px;
  cursor: pointer;
  color: #64748b;
  font-weight: bold;
  transition: color 0.2s ease;
}

.tag-remove:hover {
  color: #ef4444;
}

.clear-all {
  background: none;
  border: none;
  color: #2563eb;
  font-size: 0.9rem;
  cursor: pointer;
  transition: color 0.2s ease;
}

.clear-all:hover {
  text-decoration: underline;
}

/* 分页大小选择器 */
.page-size-control {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-top: 10px;
}

.page-size-control label {
  font-size: 0.9rem;
  color: #475569;
}

/* 过渡动画 */
.fade-slide-enter-active,
.fade-slide-leave-active {
  transition: all 0.3s ease;
}
.fade-slide-enter-from,
.fade-slide-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}

/* 筛选区域整体 */
.filter-section {
  margin-bottom: 30px;
}

/* 筛选条件行 */
.filter-row {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  margin-bottom: 15px;
}

/* 单个筛选项 */
.filter-item {
  flex: 1 1 200px;
  min-width: 200px;
  display: flex;
  flex-direction: column;
}

.filter-item label {
  font-size: 0.9rem;
  font-weight: 500;
  color: #475569;
  margin-bottom: 5px;
}

.filter-item select,
.filter-item input {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #cbd5e1;
  border-radius: 4px;
  font-size: 0.95rem;
  color: #334155;
  transition: border-color 0.2s;
}

.filter-item select:focus,
.filter-item input:focus {
  outline: none;
  border-color: #2563eb;
}

/* 搜索框 */
.search-container {
  position: relative;
}

.search-container input {
  padding-right: 40px;
}

.search-container button {
  position: absolute;
  right: 0;
  top: 0;
  height: 100%;
  width: 40px;
  background: none;
  border: none;
  cursor: pointer;
  color: #64748b;
}

.search-container button:hover {
  color: #2563eb;
}

.search-icon {
  width: 18px;
  height: 18px;
}

/* 筛选标签 */
.filter-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  padding: 10px 0;
}

.filter-tag {
  display: inline-flex;
  align-items: center;
  padding: 5px 10px;
  background-color: #f1f5f9;
  border-radius: 4px;
  color: #334155;
  font-size: 0.9rem;
}

.filter-tag button {
  background: none;
  border: none;
  margin-left: 5px;
  cursor: pointer;
  color: #64748b;
}

.filter-tag button:hover {
  color: #2563eb;
}

.clear-all {
  background: none;
  border: none;
  color: #2563eb;
  cursor: pointer;
  font-size: 0.9rem;
  padding: 5px;
}

.clear-all:hover {
  text-decoration: underline;
}

/* 分页大小选择器 */
.page-size-control {
  display: flex;
  align-items: center;
  gap: 8px;
  justify-content: flex-start;
  margin-top: 10px;
}

.page-size-control label {
  font-size: 0.9rem;
  color: #475569;
}

.page-size-control select {
  padding: 5px 8px;
  border: 1px solid #cbd5e1;
  border-radius: 4px;
  font-size: 0.9rem;
}

/* 页面标题 */
.page-header {
  text-align: center;
  padding: 40px 0;
  background-color: #f8fafc;
  margin-bottom: 30px;
}

.page-title {
  font-size: 2.5rem;
  font-weight: 700;
  color: #1e293b;
  margin-bottom: 15px;
}

.page-subtitle {
  font-size: 1.1rem;
  color: #64748b;
  max-width: 800px;
  margin: 0 auto;
}

/* 筛选区域 */
.filter-section {
  margin-bottom: 30px;
}

.filter-row {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  margin-bottom: 15px;
}

.filter-item {
  flex: 1;
  min-width: 200px;
}

.filter-item label {
  display: block;
  font-size: 0.9rem;
  font-weight: 500;
  color: #475569;
  margin-bottom: 5px;
}

.filter-item select,
.filter-item input {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #cbd5e1;
  border-radius: 4px;
  font-size: 0.95rem;
  color: #334155;
  transition: border-color 0.2s;
}

.filter-item select:focus,
.filter-item input:focus {
  outline: none;
  border-color: #2563eb;
}

.search-container {
  position: relative;
}

.search-container input {
  padding-right: 40px;
}

.search-container button {
  position: absolute;
  right: 0;
  top: 0;
  height: 100%;
  width: 40px;
  background: none;
  border: none;
  cursor: pointer;
  color: #64748b;
}

.search-container button:hover {
  color: #2563eb;
}

.search-icon {
  width: 18px;
  height: 18px;
}

.filter-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  padding: 10px 0;
}

.filter-tag {
  display: inline-flex;
  align-items: center;
  padding: 5px 10px;
  background-color: #f1f5f9;
  border-radius: 4px;
  color: #334155;
  font-size: 0.9rem;
}

.filter-tag button {
  background: none;
  border: none;
  margin-left: 5px;
  cursor: pointer;
  color: #64748b;
}

.filter-tag button:hover {
  color: #2563eb;
}

.clear-all {
  background: none;
  border: none;
  color: #2563eb;
  cursor: pointer;
  font-size: 0.9rem;
  padding: 5px;
}

.clear-all:hover {
  text-decoration: underline;
}

/* 分页大小选择器 */
.page-size-selector {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 15px;
}

.page-size-selector label {
  font-size: 0.9rem;
  color: #475569;
}

.page-size-selector select {
  padding: 5px 8px;
  border: 1px solid #cbd5e1;
  border-radius: 4px;
  font-size: 0.9rem;
}

/* 加载状态 */
.loading {
  text-align: center;
  padding: 20px;
  color: #475569;
}

/* 错误提示 */
.error {
  text-align: center;
  padding: 20px;
  color: #ef4444;
  background-color: #fef2f2;
  border: 1px solid #fecaca;
  border-radius: 4px;
  margin: 20px;
}

/* 活动列表 */
.activities-list {
  display: grid;
  gap: 30px;
  margin-bottom: 40px;
}

.activity-item {
  display: flex;
  gap: 25px;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  overflow: hidden;
  transition: transform 0.2s, box-shadow 0.2s;
}

.activity-item:hover {
  transform: translateY(-3px);
  box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1);
}

.activity-image {
  position: relative;
  width: 350px;
  height: 250px;
  flex-shrink: 0;
}

.activity-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.activity-category {
  position: absolute;
  top: 15px;
  left: 15px;
  background-color: #2563eb;
  color: white;
  font-size: 0.85rem;
  font-weight: 500;
  padding: 4px 8px;
  border-radius: 4px;
}

.activity-content {
  flex: 1;
  padding: 20px;
  display: flex;
  flex-direction: column;
}

.activity-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 15px;
}

.activity-title {
  font-size: 1.5rem;
  font-weight: 600;
  color: #1e293b;
  margin: 0;
}

.activity-rating {
  display: flex;
  align-items: center;
  gap: 8px;
}

.rating-stars {
  display: flex;
}

.star-icon {
  width: 16px;
  height: 16px;
  fill: #f59e0b;
  color: #f59e0b;
}

.rating-number {
  font-size: 0.9rem;
  color: #64748b;
}

.activity-details {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  margin-bottom: 15px;
}

.detail-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 0.95rem;
  color: #475569;
}

.detail-icon {
  width: 18px;
  height: 18px;
  color: #2563eb;
}

.activity-description {
  flex: 1;
  color: #64748b;
  line-height: 1.5;
  margin-bottom: 15px;
}

.activity-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.activity-organizer {
  display: flex;
  align-items: center;
  gap: 10px;
}

.organizer-avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  object-fit: cover;
}

.organizer-name {
  font-size: 0.95rem;
  color: #475569;
  font-weight: 500;
}

.activity-actions {
  display: flex;
  gap: 10px;
}

.btn {
  padding: 8px 16px;
  border-radius: 4px;
  font-weight: 500;
  transition: all 0.2s ease;
}

.btn-outline {
  border: 1px solid #2563eb;
  color: #2563eb;
  background-color: transparent;
}

.btn-outline:hover {
  background-color: #bfdbfe;
}

.btn-primary {
  background-color: #2563eb;
  color: white;
  border: none;
}

.btn-primary:hover {
  background-color: #1d4ed8;
}

.btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

/* 分页 */
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 10px;
  margin-bottom: 50px;
}

.page-btn,
.page-number {
  padding: 8px 16px;
  border: 1px solid #e2e8f0;
  border-radius: 4px;
  background-color: white;
  color: #334155;
  cursor: pointer;
  transition: background-color 0.2s;
}

.page-btn:hover,
.page-number:hover {
  background-color: #f8fafc;
}

.page-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.page-number.active {
  background-color: #2563eb;
  color: white;
  border-color: #2563eb;
}

/* 推荐活动 */
.recommended-activities {
  margin-bottom: 50px;
}

.recommended-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 25px;
}

.recommended-item {
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  overflow: hidden;
  transition: transform 0.2s, box-shadow 0.2s;
}

.recommended-item:hover {
  transform: translateY(-3px);
  box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1);
}

.recommended-image {
  width: 100%;
  height: 180px;
  object-fit: cover;
}

.recommended-title {
  font-size: 1.1rem;
  font-weight: 600;
  color: #1e293b;
  margin: 15px;
}

.recommended-date {
  font-size: 0.9rem;
  color: #64748b;
  margin: 0 15px 15px;
}

/* 响应式布局 */
@media (max-width: 768px) {
  .activity-item {
    flex-direction: column;
  }

  .activity-image {
    width: 100%;
    height: 200px;
  }

  .activity-header {
    flex-direction: column;
    align-items: flex-start;
  }

  .activity-rating {
    margin-top: 8px;
  }

  .activity-footer {
    flex-direction: column;
    align-items: flex-start;
    gap: 15px;
  }

  .recommended-list {
    grid-template-columns: 1fr;
  }
}
</style>
