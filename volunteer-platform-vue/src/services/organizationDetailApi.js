// 文件路径: src/services/organizationDetailApi.js
import apiClient from '@/api/axios.js';

/**
 * 根据ID获取单个组织的详细信息
 * @param {string|number} orgId - 组织的ID
 * @returns {Promise<any>}
 */
export const getOrganizationDetail = (orgId) => {
    return apiClient.get(`/api/organizations/${orgId}`);
};

/**
 * 申请加入组织
 * @param {string|number} orgId - 组织的ID
 * @returns {Promise<any>}
 */
export const applyToJoinOrganization = (orgId) => {
    return apiClient.post(`/api/organizations/${orgId}/apply`);
};