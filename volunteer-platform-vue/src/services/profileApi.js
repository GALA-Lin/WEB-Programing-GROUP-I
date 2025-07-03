import apiClient from '@/api/axios.js';

/**
 * 获取当前用户的志愿服务时长明细
 */
export const getServiceRecords = () => {
    return apiClient.get('/api/profile/service-records');
};

/**
 * 获取当前用户已报名的活动列表
 */
export const getEnrolledActivities = () => {
    return apiClient.get('/api/profile/enrollments');
};