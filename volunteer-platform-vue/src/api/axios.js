import axios from 'axios';
import { ElMessage } from 'element-plus'; // 引入 Element Plus 的消息提示

const apiClient = axios.create({
    baseURL: 'http://localhost:8080', // 您的后端API基础地址
    headers: {
        'Content-Type': 'application/json'
    }
});

// ✨ 添加全局响应拦截器 ✨
apiClient.interceptors.response.use(
    response => {
        // 后端返回的整个响应体，例如 { code: 200, message: "成功", data: { ... } }
        const res = response.data;

        // 业务成功的状态码
        const successCodes = [200, 201];

        if (successCodes.includes(res.code)) {
            // 如果业务成功，则直接返回 res.data，也就是后端 Result 对象的 data 字段
            return res.data;
        } else {
            // 如果业务码表示失败，则使用 Element Plus 弹出错误消息
            ElMessage.error(res.message || '操作失败');
            // 抛出错误，让组件中的 .catch() 能捕获到
            return Promise.reject(new Error(res.message || 'Error'));
        }
    },
    error => {
        // 处理 HTTP 层面（网络、服务器等）的错误
        console.error('API Error:', error.response);
        ElMessage.error(error.response?.data?.message || '网络错误，请检查您的连接');
        return Promise.reject(error);
    }
);

export default apiClient;