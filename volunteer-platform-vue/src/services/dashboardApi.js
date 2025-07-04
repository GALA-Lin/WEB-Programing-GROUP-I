// volunteer-platform-vue/src/services/dashboardApi.js
import apiClient from '@/api/axios.js';

/**
 * 获取后台看板的统计数据
 * @returns {Promise<any>}
 */
export const getAdminDashboardStats = () => {
    return apiClient.get('/api/admin/dashboard/stats');
};

/**
 * 【新增】获取后台看板的最新动态数据
 * @returns {Promise<any>}
 */
export const getDashboardLatestUpdates = () => {
    return apiClient.get('/api/admin/dashboard/latest-updates');
};