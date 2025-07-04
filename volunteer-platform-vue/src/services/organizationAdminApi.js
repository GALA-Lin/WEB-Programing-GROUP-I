import apiClient from '@/api/axios.js';

export const getAdminOrganizations = (page = 1, pageSize = 10) => {
    return apiClient.get('/api/admin/organizations', { params: { page, pageSize } });
};

export const createOrganization = (orgData) => {
    return apiClient.post('/api/admin/organizations', orgData);
};

export const updateOrganization = (id, orgData) => {
    return apiClient.put(`/api/admin/organizations/${id}`, orgData);
};

export const deleteOrganization = (id) => {
    return apiClient.delete(`/api/admin/organizations/${id}`);
};