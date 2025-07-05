// 文件路径: AAA/volunteer-platform-vue/src/services/activityApi.js
import apiClient from '@/api/axios.js'; // 【修改】导入共享的 apiClient 实例

/**
 * 获取活动列表 (分页)
 * @param {number} page - 页码
 * @param {number} pageSize - 每页数量
 */
export const getActivities = (page = 1, pageSize = 10) => {
    return apiClient.get('/api/admin/activities', { // 【修改】路径要与后端Controller一致
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
    return apiClient.post('/api/admin/activities', activityData);
};

/**
 * 更新活动
 * @param {number} id - 活动ID
 * @param {object} activityData - 要更新的活动数据
 */
export const updateActivity = (id, activityData) => {
    return apiClient.put(`/api/admin/activities/${id}`, activityData);
};

/**
 * 删除活动
 * @param {number} id - 活动ID
 */
export const deleteActivity = (id) => {
    return apiClient.delete(`/api/admin/activities/${id}`);
};

/**
 * 获取活动报名列表
 */
export const getEnrollments = (activityId) => {
    return apiClient.get(`/api/admin/activities/${activityId}/enrollments`);
};

/**
 * 导出活动报名表为Excel
 */
export const exportEnrollments = (activityId) => {
    return apiClient.get(`/api/admin/activities/${activityId}/enrollments/export`, {
        responseType: 'blob' // 关键：告诉axios期望接收一个二进制文件
    });
};