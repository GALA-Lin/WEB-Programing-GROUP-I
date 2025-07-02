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