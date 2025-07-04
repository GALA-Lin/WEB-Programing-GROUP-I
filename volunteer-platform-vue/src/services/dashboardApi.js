// volunteer-platform-vue/src/services/dashboardApi.js
import apiClient from '@/api/axios.js';

/**
 * 获取【前台公开】的仪表盘统计数据
 * @returns {Promise<any>}
 */
export const getPublicDashboardStats = () => {
    return apiClient.get('/api/dashboard/stats');
};

/**
 * 【新增】获取【后台管理】的仪表盘统计数据
 * @returns {Promise<any>}
 */
export const getAdminDashboardStats = () => {
    return apiClient.get('/api/admin/dashboard/stats');
};

// 如果您的同事还添加了其他函数，也一并保留在这里
// export const getDashboardLatestUpdates = () => {
//     return apiClient.get('/api/admin/dashboard/latest-updates');
// };