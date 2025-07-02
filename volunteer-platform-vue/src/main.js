// 文件路径: src/main.js
import { createApp } from 'vue';
import { createPinia } from 'pinia'; // 1. 导入 createPinia

// 1. 完整导入 Element Plus
import ElementPlus from 'element-plus'
// 2. 导入 Element Plus 的样式文件
import 'element-plus/dist/index.css'
// 3. (可选但推荐) 导入 Element Plus 的图标
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
// 4. (如果需要中文) 导入中文语言包
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'

import App from './App.vue';
import router from './router';

// 导入全局样式
import './assets/main.css';

const app = createApp(App);
// 5. 注册 Element Plus 图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}
// 6. 全局注册 Element Plus 并设置中文
app.use(ElementPlus, {
    locale: zhCn,
})

app.use(createPinia()); // 2. 在 app.use(router) 之前，先启用 Pinia
app.use(router);

app.mount('#app');