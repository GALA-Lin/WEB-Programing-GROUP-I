// volunteer-platform-vue/src/services/serviceRecordAdminApi.js

import apiClient from '@/api/axios.js';

/**
 * 获取服务时长记录列表 (分页)
 * @param {number} page - 页码
 * @param {number} pageSize - 每页数量
 */
export const getServiceRecords = (page = 1, pageSize = 10) => {
    // 注意：我们将创建一个新的后端接口来获取这个列表
    return apiClient.get('/api/admin/service-records', {
        params: { page, pageSize }
    });
};

/**
 * 创建新的服务时长记录
 * @param {object} recordData - 记录数据
 */
export const createServiceRecord = (recordData) => {
    return apiClient.post('/api/admin/service-records', recordData);
};

/**
 * 更新服务时长记录
 * @param {number} id - 记录ID
 * @param {object} recordData - 更新后的记录数据
 */
export const updateServiceRecord = (id, recordData) => {
    return apiClient.put(`/api/admin/service-records/${id}`, recordData);
};

/**
 * 删除服务时长记录
 * @param {number} id - 记录ID
 */
export const deleteServiceRecord = (id) => {
    return apiClient.delete(`/api/admin/service-records/${id}`);
};