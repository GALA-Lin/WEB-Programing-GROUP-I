import apiClient from '@/api/axios.js';

/**
 * 获取公共的活动列表 (分页)
 * @param {number} page - 页码
 * @param {number} pageSize - 每页数量
 * @param {string} [category] - (可选) 活动分类
 * @returns {Promise<any>}
 */
export const getPublicActivities = (page = 1, pageSize = 10, category = '') => {
    // 【重要修改】移除了 currentUserId 参数
    // 后端会通过我们请求头里的 Authorization (Token) 来识别用户
    return apiClient.get('/api/activities', {
        params: {
            page,
            pageSize,
            category
        }
    });
};