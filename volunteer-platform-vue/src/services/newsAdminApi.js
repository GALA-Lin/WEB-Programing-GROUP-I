import apiClient from '@/api/axios.js';

export const getNewsPage = (page = 1, pageSize = 10) => {
    return apiClient.get('/api/admin/news', { params: { page, pageSize } });
};

export const createNews = (newsData) => {
    return apiClient.post('/api/admin/news', newsData);
};

export const updateNews = (id, newsData) => {
    return apiClient.put(`/api/admin/news/${id}`, newsData);
};

export const deleteNews = (id) => {
    return apiClient.delete(`/api/admin/news/${id}`);
};