<template>
  <footer class="footer">
    <div class="container">
      <div class="footer-content">
        <div class="footer-section">
          <h3 class="footer-title">关于我们</h3>
          <p class="footer-text">我们致力于连接志愿者和需要帮助的组织，共同创造更美好的社会。</p>
        </div>
        <div class="footer-section">
          <h3 class="footer-title">快速链接</h3>
          <ul class="footer-links">
            <li><a href="#" class="footer-link">首页</a></li>
            <li><a href="#" class="footer-link">活动列表</a></li>
            <li><a href="#" class="footer-link">组织列表</a></li>
            <li><a href="#" class="footer-link">关于我们</a></li>
          </ul>
        </div>
        <div class="footer-section">
          <h3 class="footer-title">联系我们</h3>
          <ul class="footer-contacts">
            <li class="footer-contact">邮箱: contact@volunteerplatform.com</li>
            <li class="footer-contact">电话: 123-456-7890</li>
          </ul>
        </div>
      </div>
      <div class="footer-bottom">
        <p class="footer-copyright">&copy; 2023 志愿服务平台. 保留所有权利.</p>
      </div>
    </div>
  </footer>
</template>

<script setup>
// 页脚组件逻辑
</script>

<style scoped>
.footer {
  background-color: #333;
  color: #fff;
  padding: 40px 0;
}

.footer-content {
  display: flex;
  flex-wrap: wrap;
  gap: 40px;
}

.footer-section {
  flex: 1;
  min-width: 200px;
}

.footer-title {
  font-size: 18px;
  margin-bottom: 16px;
}

.footer-text {
  color: #ccc;
  line-height: 1.6;
}

.footer-links {
  list-style: none;
  padding: 0;
}

.footer-link {
  color: #ccc;
  text-decoration: none;
  line-height: 1.8;
  transition: color 0.2s;
}

.footer-link:hover {
  color: #fff;
}

.footer-contacts {
  list-style: none;
  padding: 0;
}

.footer-contact {
  color: #ccc;
  margin-bottom: 8px;
}

.footer-bottom {
  margin-top: 32px;
  border-top: 1px solid #444;
  padding-top: 20px;
}

.footer-copyright {
  text-align: center;
  color: #ccc;
  font-size: 14px;
}
</style>
    