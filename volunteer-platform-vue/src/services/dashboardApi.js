import apiClient from '@/api/axios.js';

/**
 * 获取仪表盘统计数据
 */
export const getDashboardStats = () => {
    return apiClient.get('/api/dashboard/stats');
};