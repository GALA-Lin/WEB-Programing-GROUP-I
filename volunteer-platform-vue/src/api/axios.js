// 文件路径: /volunteer-platform-vue/src/api/axios.js
import axios from 'axios';
import { ElMessage } from 'element-plus'; // 引入Element Plus消息提示

const apiClient = axios.create({
    baseURL: 'http://localhost:8080', // 你的后端API基础地址
    headers: {
        'Content-Type': 'application/json'
    }
});

// 请求拦截器：自动携带 token
apiClient.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  error => Promise.reject(error)
);

// 响应拦截器，用于处理后端返回的 {code, message, data} 结构
apiClient.interceptors.response.use(
    response => {
        const res = response.data;

        // 检查 res.code 是否存在，因为有些非业务请求可能没有这个结构
        if (typeof res.code !== 'undefined') {
            const successCodes = [200, 201];
            if (successCodes.includes(res.code)) {
                // 如果是成功的业务码，则直接返回 data 字段
                return res.data;
            } else {
                // 如果业务码表示失败，则使用 Element Plus 弹出错误消息
                ElMessage.error(res.message || '操作失败');
                // 抛出错误，中断Promise链
                return Promise.reject(new Error(res.message || 'Error'));
            }
        }
        // 如果没有 code 字段，直接返回原始的 response.data
        return response.data;
    },
    error => {
        // 处理 HTTP 错误
        console.error('API Error:', error.response);
        ElMessage.error(error.response?.data?.message || '网络错误，请稍后再试');
        return Promise.reject(error);
    }
);

export default apiClient;