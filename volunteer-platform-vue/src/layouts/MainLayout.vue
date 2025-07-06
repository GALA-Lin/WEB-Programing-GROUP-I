<template>
  <div>
    <Navbar />
    <main class="main-content">
      <router-view />
    </main>
    <Footer />
  </div>
</template>

<script setup>
import Navbar from '@/components/Navbar.vue';
import Footer from '@/components/Footer.vue';
</script>

<style scoped>
.main-content {
  /* 确保内容区不会被固定导航栏遮挡，并有最小高度 */
  padding-top: 60px;
  min-height: calc(100vh - 120px); /* 视口高度减去Navbar和Footer的高度 */
}
</style>