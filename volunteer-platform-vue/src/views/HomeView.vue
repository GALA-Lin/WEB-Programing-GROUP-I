<template>
  <div class="home-view">
    <section class="hero">
      <div class="container">
        <div class="hero-content">
          <h1 class="hero-title">让志愿行动改变世界</h1>
          <p class="hero-subtitle">加入我们，一起为社区贡献力量，创造更美好的未来</p>
          <div class="hero-actions">
            <router-link to="/activities" class="btn-primary">浏览活动</router-link>
            <router-link to="/register" class="btn-secondary">成为志愿者</router-link>
          </div>
        </div>
      </div>
    </section>
    
    <section class="features">
      <div class="container">
        <h2 class="section-title">为什么选择我们</h2>
        <div class="features-grid">
          <div class="feature-card">
            <div class="feature-icon">1</div>
            <h3 class="feature-title">丰富的志愿活动</h3>
            <p class="feature-description">我们提供多样化的志愿活动，满足不同人群的兴趣和能力</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon">2</div>
            <h3 class="feature-title">可靠的组织合作</h3>
            <p class="feature-description">与经过认证的非营利组织合作，确保您的贡献能真正帮助到需要的人</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon">3</div>
            <h3 class="feature-title">个性化匹配</h3>
            <p class="feature-description">基于您的兴趣和技能，为您推荐最适合的志愿活动</p>
          </div>
        </div>
      </div>
    </section>
    
    <section class="recent-activities">
      <div class="container">
        <h2 class="section-title">近期热门活动</h2>
        <div class="activities-grid">
          <div class="activity-card" v-for="activity in recentActivities" :key="activity.id">
            <img :src="activity.image" alt="活动图片" class="activity-image">
            <div class="activity-content">
              <h3 class="activity-title">{{ activity.title }}</h3>
              <p class="activity-date">{{ activity.date }}</p>
              <p class="activity-location">{{ activity.location }}</p>
              <router-link :to="`/activities/${activity.id}`" class="btn-view">查看详情</router-link>
            </div>
          </div>
        </div>
        <div class="view-all">
          <router-link to="/activities" class="btn-view-all">查看全部活动</router-link>
        </div>
      </div>
    </section>
    
    <section class="testimonials">
      <div class="container">
        <h2 class="section-title">志愿者心声</h2>
        <div class="testimonials-slider">
          <div class="testimonial" v-for="testimonial in testimonials" :key="testimonial.id">
            <div class="testimonial-content">
              <p class="testimonial-text">{{ testimonial.text }}</p>
              <div class="testimonial-author">
                <img :src="testimonial.avatar" alt="志愿者头像" class="testimonial-avatar">
                <div>
                  <p class="testimonial-name">{{ testimonial.name }}</p>
                  <p class="testimonial-position">{{ testimonial.position }}</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref } from 'vue';

// 模拟数据
const recentActivities = ref([
  {
    id: 1,
    title: "社区环保清洁日",
    date: "2023-06-15",
    location: "城市公园",
    image: "https://picsum.photos/id/1002/600/400"
  },
  {
    id: 2,
    title: "儿童阅读推广",
    date: "2023-06-20",
    location: "市图书馆",
    image: "https://picsum.photos/id/20/600/400"
  },
  {
    id: 3,
    title: "敬老院关爱活动",
    date: "2023-06-25",
    location: "夕阳红敬老院",
    image: "https://picsum.photos/id/1003/600/400"
  }
]);

const testimonials = ref([
  {
    id: 1,
    text: "通过这个平台，我找到了很多志同道合的朋友，一起参与了很多有意义的活动，收获颇丰！",
    name: "张明",
    position: "志愿者（1年）",
    avatar: "https://picsum.photos/id/1012/100/100"
  },
  {
    id: 2,
    text: "作为一个企业，我们通过这个平台组织了多次员工志愿活动，不仅增强了团队凝聚力，也为社会做出了贡献。",
    name: "李华",
    position: "企业社会责任主管",
    avatar: "https://picsum.photos/id/1027/100/100"
  }
]);
</script>

<style scoped>
.home-view {
  font-family: 'Inter', sans-serif;
}

.hero {
  background-image: url('https://zyh365.com/images/indexbg.jpg');
  color: white;
  padding: 100px 0;
  text-align: center;
}

.hero-content {
  max-width: 800px;
  margin: 0 auto;
}

.hero-title {
  font-size: 48px;
  margin-bottom: 24px;
}

.hero-subtitle {
  font-size: 20px;
  margin-bottom: 40px;
  color: #e0e7ff;
}

.hero-actions {
  display: flex;
  justify-content: center;
  gap: 20px;
}

.btn-primary {
  background-color: white;
  color: #2563eb;
  border: none;
  padding: 12px 24px;
  border-radius: 4px;
  text-decoration: none;
  font-weight: 600;
  transition: background-color 0.2s;
}

.btn-primary:hover {
  background-color: #f1f5f9;
}

.btn-secondary {
  background-color: white;
  color: #2563eb;
  border: none;
  padding: 12px 24px;
  border-radius: 4px;
  text-decoration: none;
  font-weight: 600;
  transition: background-color 0.2s;
}

.btn-secondary:hover {
  background-color: rgba(255, 255, 255, 0.1);
}

.features {
  padding: 80px 0;
  background-color: #f8fafc;
}

.section-title {
  font-size: 32px;
  text-align: center;
  margin-bottom: 60px;
  color: #334155;
}

.features-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 40px;
  justify-content: center;
}

.feature-card {
  max-width: 300px;
  background-color: white;
  padding: 32px;
  border-radius: 8px;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
  text-align: center;
  transition: transform 0.2s;
}

.feature-card:hover {
  transform: translateY(-5px);
}

.feature-icon {
  display: inline-flex;
  justify-content: center;
  align-items: center;
  width: 48px;
  height: 48px;
  background-color: #2563eb;
  color: white;
  border-radius: 50%;
  font-size: 24px;
  margin-bottom: 20px;
}

.feature-title {
  font-size: 20px;
  margin-bottom: 16px;
  color: #334155;
}

.feature-description {
  color: #64748b;
  line-height: 1.6;
}

.recent-activities {
  padding: 80px 0;
}

.activities-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 40px;
}

.activity-card {
  background-color: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s;
}

.activity-card:hover {
  transform: translateY(-5px);
}

.activity-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.activity-content {
  padding: 24px;
}

.activity-title {
  font-size: 20px;
  margin-bottom: 12px;
  color: #334155;
}

.activity-date,
.activity-location {
  color: #64748b;
  margin-bottom: 8px;
  font-size: 14px;
}

.btn-view {
  display: inline-block;
  background-color: #2563eb;
  color: white;
  padding: 8px 16px;
  border-radius: 4px;
  text-decoration: none;
  font-size: 14px;
  transition: background-color 0.2s;
  margin-top: 16px;
}

.btn-view:hover {
  background-color: #1d4ed8;
}

.view-all {
  text-align: center;
  margin-top: 40px;
}

.btn-view-all {
  display: inline-block;
  background-color: transparent;
  color: #2563eb;
  border: 2px solid #2563eb;
  padding: 10px 24px;
  border-radius: 4px;
  text-decoration: none;
  font-weight: 600;
  transition: background-color 0.2s;
}

.btn-view-all:hover {
  background-color: #f8fafc;
}

.testimonials {
  padding: 80px 0;
  background-color: #f8fafc;
}

.testimonials-slider {
  display: flex;
  flex-wrap: wrap;
  gap: 40px;
  justify-content: center;
}

.testimonial {
  max-width: 400px;
  background-color: white;
  padding: 32px;
  border-radius: 8px;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
}

.testimonial-text {
  color: #64748b;
  line-height: 1.6;
  margin-bottom: 24px;
}

.testimonial-author {
  display: flex;
  align-items: center;
  gap: 16px;
}

.testimonial-avatar {
  width: 64px;
  height: 64px;
  border-radius: 50%;
  object-fit: cover;
}

.testimonial-name {
  font-weight: 600;
  color: #334155;
}

.testimonial-position {
  color: #64748b;
  font-size: 14px;
}
</style>
    