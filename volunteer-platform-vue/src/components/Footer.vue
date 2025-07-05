<template>
  <footer class="site-footer">
    <div class="container">
      <div class="footer-content">
        <div class="footer-section about">
          <h3 class="footer-title">油炬智愿</h3>
          <p>一个专为中国石油大学师生打造的志愿服务平台，致力于连接志愿者与需要帮助的组织，共同创造更美好的社会。</p>
        </div>

        <div class="footer-section links">
          <h3 class="footer-title">快速链接</h3>
          <ul>
            <li><router-link to="/activities" class="footer-link">查找活动</router-link></li>
            <li><router-link to="/organizations" class="footer-link">发现组织</router-link></li>
            <li><router-link to="/news" class="footer-link">新闻动态</router-link></li>
            <li><router-link to="/profile" class="footer-link">个人中心</router-link></li>
          </ul>
        </div>

        <div class="footer-section contact">
          <h3 class="footer-title">联系我们</h3>
          <ul>
            <li>Gala-lin@outlook.com</li>
            <li>地址：新疆维吾尔自治区克拉玛依市克拉玛依区安定路355号</li>
          </ul>
        </div>

        <div class="footer-section social">
          <h3 class="footer-title">关注我们</h3>
          <div class="social-icons">
            <a href="#" class="social-link" aria-label="Gitee">
              <img src="https://gitee.com/favicon.ico" alt="Gitee" />
            </a>
            <a href="#" class="social-link" aria-label="GitHub">
              <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="currentColor"><path d="M12 0C5.37 0 0 5.37 0 12c0 5.3 3.438 9.8 8.203 11.383.6.112.82-.26.82-.577 0-.285-.01-1.04-.015-2.04-3.34.725-4.04-1.61-4.04-1.61-.547-1.39-1.335-1.76-1.335-1.76-1.09-.745.082-.73.082-.73 1.205.084 1.838 1.238 1.838 1.238 1.07 1.833 2.809 1.303 3.495.998.108-.775.418-1.303.76-1.605-2.665-.303-5.466-1.332-5.466-5.93 0-1.31.465-2.38 1.23-3.22-.125-.303-.535-1.524.115-3.176 0 0 1.005-.322 3.3 1.23.96-.267 1.98-.4 3-.405 1.02.005 2.04.138 3 .405 2.28-1.552 3.285-1.23 3.285-1.23.65 1.652.24 2.873.12 3.176.765.84 1.23 1.91 1.23 3.22 0 4.61-2.805 5.625-5.475 5.92.43.37.815 1.1.815 2.22 0 1.606-.015 2.896-.015 3.286 0 .315.21.69.825.57C20.565 21.795 24 17.295 24 12c0-6.63-5.37-12-12-12"/></svg>
            </a>
          </div>
        </div>
      </div>

      <div class="footer-bottom">
        <p>&copy; {{ new Date().getFullYear() }} 油炬智愿. All Rights Reserved.</p>
      </div>
    </div>
  </footer>
</template>

<script setup>
// No script needed
</script>

<style scoped>
.site-footer {
  background-color: #1a202c;
  color: #a0aec0;
  padding: 4rem 0 2rem;
  font-size: 14px;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.footer-content {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 2rem;
  margin-bottom: 3rem;
}

.footer-section h3 {
  font-size: 16px;
  color: #ffffff;
  margin-bottom: 1rem;
  font-weight: 600;
}

.footer-section p {
  line-height: 1.7;
}

.footer-section.about p {
  padding-right: 1rem;
}

.footer-section ul {
  list-style: none;
  padding: 0;
  margin: 0; /* 修正：移除默认的ul margin */
}

.footer-section ul li {
  margin-bottom: 0.75rem;
}

.footer-link {
  color: #a0aec0;
  text-decoration: none;
  transition: color 0.2s;
}

.footer-link:hover {
  color: #ffffff;
}

/* 修正：为 contact 区的 li 添加样式 */
.footer-section.contact li {
  line-height: 1.7;
}

.social-icons {
  display: flex;
  gap: 1rem;
}

.social-link {
  color: #a0aec0;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background-color: #2d3748;
  transition: background-color 0.2s, transform 0.2s;
}
.social-link:hover {
  background-color: var(--color-primary);
  color: white;
  transform: translateY(-2px);
}
.social-link svg, .social-link img {
  width: 20px;
  height: 20px;
}

.footer-bottom {
  border-top: 1px solid #2d3748;
  padding-top: 2rem;
  text-align: center;
  font-size: 13px;
}
</style>