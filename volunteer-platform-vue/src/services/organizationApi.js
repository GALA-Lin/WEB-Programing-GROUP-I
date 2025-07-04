import apiClient from '@/api/axios.js';

// 获取所有组织列表
export const getOrganizations = () => {
    return apiClient.get('/api/organizations');
};

// 获取单个组织详情
export const getOrganizationDetails = (id) => {
    return apiClient.get(`/api/organizations/${id}`);
};

// 申请加入组织
export const applyToOrganization = (id) => {
    return apiClient.post(`/api/organizations/${id}/apply`);
};

// 退出组织
export const leaveOrganization = (id) => {
    return apiClient.delete(`/api/organizations/${id}/leave`);
};

// 获取待审批列表
export const getPendingApprovals = (id) => {
    return apiClient.get(`/api/admin/organizations/${id}/pending-approvals`);
};

// 批准申请
export const approveApplication = (membershipId) => {
    return apiClient.post(`/api/admin/memberships/${membershipId}/approve`);
};

// 拒绝或移除成员
export const rejectOrRemoveMember = (membershipId) => {
    return apiClient.delete(`/api/admin/memberships/${membershipId}`);
};