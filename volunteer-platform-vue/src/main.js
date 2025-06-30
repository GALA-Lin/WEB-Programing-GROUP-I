// 文件路径: src/main.js
import { createApp } from 'vue';
import { createPinia } from 'pinia'; // 1. 导入 createPinia

import App from './App.vue';
import router from './router';

// 导入全局样式
import './assets/main.css';

const app = createApp(App);

app.use(createPinia()); // 2. 在 app.use(router) 之前，先启用 Pinia
app.use(router);

app.mount('#app');