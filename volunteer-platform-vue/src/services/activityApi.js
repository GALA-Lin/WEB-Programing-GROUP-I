// src/services/activityApi.js
import axios from 'axios';

// 设置axios的基础URL，指向您的后端服务地址
// 在开发环境下，您可能需要配置代理来解决跨域问题
const apiClient = axios.create({
    baseURL: '/api', // 假设Vue的devServer已配置代理到后端
    headers: {
        'Content-Type': 'application/json'
    }
});

// 响应拦截器，用于处理后端返回的 {code, message, data} 结构
apiClient.interceptors.response.use(
    response => {
        const res = response.data;
        // 后端自定义的成功码，例如 200, 201
        const successCodes = [200, 201];
        if (successCodes.includes(res.code)) {
            return res.data; // 直接返回 data 字段
        } else {
            // 如果业务码表示失败，则抛出错误
            // Element Plus 的 ElMessage 组件可以捕获并显示
            console.error(res.message || 'Error');
            return Promise.reject(new Error(res.message || 'Error'));
        }
    },
    error => {
        // 处理 HTTP 错误
        console.error('API Error:', error.response);
        return Promise.reject(error);
    }
);


/**
 * 获取活动列表 (分页)
 * @param {number} page - 页码
 * @param {number} pageSize - 每页数量
 */
export const getActivities = (page = 1, pageSize = 10) => {
    return apiClient.get('/admin/activities', {
        params: {
            page,
            pageSize
        }
    });
};

/**
 * 创建新活动
 * @param {object} activityData - 活动数据，结构遵循 ActivityCreateDTO
 */
export const createActivity = (activityData) => {
    return apiClient.post('/admin/activities', activityData);
};

/**
 * 更新活动
 * @param {number} id - 活动ID
 * @param {object} activityData - 要更新的活动数据
 */
export const updateActivity = (id, activityData) => {
    return apiClient.put(`/admin/activities/${id}`, activityData);
};

/**
 * 删除活动
 * @param {number} id - 活动ID
 */
export const deleteActivity = (id) => {
    return apiClient.delete(`/admin/activities/${id}`);
};