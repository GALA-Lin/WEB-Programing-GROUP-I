import { fileURLToPath, URL } from 'node:url'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  // 添加这个代理配置
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080', // 你的Spring Boot后端地址
        changeOrigin: true,
        secure: false
      }
    }
  }
})
