import apiClient from '@/api/axios.js';

/**
 * 获取公共的活动列表 (分页)
 * @param {number} page - 页码
 * @param {number} pageSize - 每页数量
 * @param {string} [category] - (可选) 活动分类
 * @returns {Promise<any>}
 */
export const getPublicActivities = (page = 1, pageSize = 10, category = '') => {
    // 使用 params 对象来传递参数，而不是手动拼接字符串
    const params = {
        page,
        pageSize,
        category
    };

    return apiClient.get('/api/activities', { params });
};

/**
 * 获取活动详情
 * @param {number|string} id
 * @returns {Promise<any>}
 */
export const getActivityDetail = (id) => {
    return apiClient.get(`/api/activities/${id}`);
};

/**
 * 报名活动
 * @param {number|string} id
 * @returns {Promise<any>}
 */
export const enrollActivity = (id) => {
    return apiClient.post(`/api/activities/${id}/enroll`);
};

/**
 * 取消报名
 * @param {number|string} id
 * @returns {Promise<any>}
 */
export const unenrollActivity = (id) => {
    return apiClient.delete(`/api/activities/${id}/enroll`);
};
