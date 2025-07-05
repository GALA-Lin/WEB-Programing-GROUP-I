import apiClient from '@/api/axios.js';

/**
 * 【新增】根据活动ID，分页获取服务时长记录
 * @param {number} activityId - 活动的ID
 * @param {number} page - 页码
 * @param {number} pageSize - 每页数量
 */
export const getServiceRecordsByActivity = (activityId, page = 1, pageSize = 10) => {
    return apiClient.get(`/api/admin/activities/${activityId}/service-records`, {
        params: { page, pageSize }
    });
};

/**
 * 【新增】为指定活动和用户创建一条服务时长记录
 * @param {object} recordData - 包含 userId, activityId, serviceHours, remarks 的对象
 */
export const createServiceRecord = (recordData) => {
    return apiClient.post('/api/admin/service-records', recordData);
};

/**
 * 【新增】更新指定ID的服务时长记录
 * @param {number} recordId - 时长记录的ID
 * @param {object} recordData - 包含 serviceHours, remarks 的更新对象
 */
export const updateServiceRecord = (recordId, recordData) => {
    return apiClient.put(`/api/admin/service-records/${recordId}`, recordData);
};

/**
 * 【新增】删除指定ID的服务时长记录
 * @param {number} recordId - 时长记录的ID
 */
export const deleteServiceRecord = (recordId) => {
    return apiClient.delete(`/api/admin/service-records/${recordId}`);
};