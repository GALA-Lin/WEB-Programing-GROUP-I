import apiClient from '@/api/axios.js';

/**
 * 获取新闻列表 (分页)
 * @param {number} page - 页码
 * @param {number} pageSize - 每页数量
 * @returns {Promise<any>}
 */
export const getNewsList = (page = 1, pageSize = 10) => {
    return apiClient.get('/api/news', {
        params: {
            page,
            pageSize
        }
    });
};

/**
 * 根据ID获取新闻详情
 * @param {number} newsId - 新闻的ID
 * @returns {Promise<any>}
 */
export const getNewsDetail = (newsId) => {
    return apiClient.get(`/api/news/${newsId}`);
};

/**
 * 点赞新闻
 * @param {number} newsId - 新闻的ID
 * @returns {Promise<any>}
 */
export const likeNews = (newsId) => {
    return apiClient.post(`/api/news/${newsId}/like`);
};

/**
 * 取消点赞新闻
 * @param {number} newsId - 新闻的ID
 * @returns {Promise<any>}
 */
export const unlikeNews = (newsId) => {
    // 【重要】我们约定使用 DELETE 方法来表示取消点赞
    return apiClient.delete(`/api/news/${newsId}/like`);
};