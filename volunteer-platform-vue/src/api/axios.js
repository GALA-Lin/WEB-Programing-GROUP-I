// 文件路径: /volunteer-platform-vue/src/api/axios.js
import axios from 'axios';
import { ElMessage } from 'element-plus';
// 【新增】为了安全地访问 store，我们在这里不直接导入，而是在拦截器内部获取
import { useUserStore } from '@/stores/userStore';

const apiClient = axios.create({
    baseURL: 'http://localhost:8080', // 您的后端API基础地址
    headers: {
        'Content-Type': 'application/json'
    }
});

// 【核心修改】添加一个请求拦截器
apiClient.interceptors.request.use(
    (config) => {
        // 在发送请求之前，从 localStorage 中获取 token
        const token = localStorage.getItem('token');
        if (token) {
            // 如果 token 存在，则将其添加到请求的 Authorization 头中
            config.headers['Authorization'] = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        // 对请求错误做些什么
        return Promise.reject(error);
    }
);


// 响应拦截器（这部分保持不变）
apiClient.interceptors.response.use(
    response => {
        const res = response.data;
        if (typeof res.code !== 'undefined') {
            const successCodes = [200, 201];
            if (successCodes.includes(res.code)) {
                return res.data;
            } else {
                ElMessage.error(res.message || '操作失败');

                // 如果是未登录或权限不足的错误码，可以触发登出逻辑
                if (res.code === 401 || res.code === 403) {
                    const userStore = useUserStore();
                    userStore.logout();
                }

                return Promise.reject(new Error(res.message || 'Error'));
            }
        }
        return response.data;
    },
    error => {
        console.error('API Error:', error.response);
        ElMessage.error(error.response?.data?.message || '网络错误，请稍后再试');
        return Promise.reject(error);
    }
);

export default apiClient;