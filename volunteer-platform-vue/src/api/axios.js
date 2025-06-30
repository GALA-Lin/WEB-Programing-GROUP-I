import axios from 'axios';

const apiClient = axios.create({
    baseURL: 'http://localhost:8080', // 你的后端API基础地址
    headers: {
        'Content-Type': 'application/json'
    }
});

export default apiClient;