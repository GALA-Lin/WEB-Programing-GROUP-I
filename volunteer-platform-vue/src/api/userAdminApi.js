// 文件路径: src/services/userAdminApi.js
import apiClient from '@/api/axios.js';

/**
 * 获取用户列表 (分页)
 * @param {number} page - 页码
 * @param {number} pageSize - 每页数量
 */
export const getUsers = (page = 1, pageSize = 10) => {
    return apiClient.get('/api/admin/users', {
        params: {
            page,
            pageSize
        }
    });
};

/**
 * 创建新用户
 * @param {object} userData - 用户数据
 */
export const createUser = (userData) => {
    return apiClient.post('/api/admin/users', userData);
};

/**
 * 更新用户信息
 * @param {number} id - 用户ID
 * @param {object} userData - 要更新的用户数据
 */
export const updateUser = (id, userData) => {
    return apiClient.put(`/api/admin/users/${id}`, userData);
};

/**
 * 删除用户
 * @param {number} id - 用户ID
 */
export const deleteUser = (id) => {
    return apiClient.delete(`/api/admin/users/${id}`);
};