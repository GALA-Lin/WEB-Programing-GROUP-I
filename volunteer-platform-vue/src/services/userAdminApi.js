// src/services/userAdminApi.js
import apiClient from '@/api/axios.js';

export const getUsers = (page = 1, pageSize = 10) => {
    return apiClient.get('/api/admin/users', { params: { page, pageSize } });
};

export const createUser = (userData) => {
    return apiClient.post('/api/admin/users', userData);
};

export const updateUser = (id, userData) => {
    return apiClient.put(`/api/admin/users/${id}`, userData);
};

export const deleteUser = (id) => {
    return apiClient.delete(`/api/admin/users/${id}`);
};