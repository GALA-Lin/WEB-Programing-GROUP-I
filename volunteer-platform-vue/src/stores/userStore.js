import { defineStore } from 'pinia';
import { computed, ref } from 'vue';
import { useRouter } from 'vue-router';
import apiClient from '@/api/axios.js';

export const useUserStore = defineStore('user', () => {
    const router = useRouter();

    const currentUser = ref(JSON.parse(localStorage.getItem('user')) || null);
    const token = ref(localStorage.getItem('token') || null);

    const isLoggedIn = computed(() => !!token.value);

    async function fetchCurrentUser() {
        if (!token.value) return;
        try {
            const userData = await apiClient.get('/api/users/me');
            if (userData) {
                // 使用扩展运算符合并新旧数据，确保响应式
                currentUser.value = { ...(currentUser.value || {}), ...userData };
                localStorage.setItem('user', JSON.stringify(currentUser.value));
            }
        } catch (error) {
            console.error('获取用户信息失败:', error);
            await logout();
        }
    }

    async function login(credentials) {
        const responseData = await apiClient.post('/api/auth/login', credentials);
        token.value = responseData.token;
        localStorage.setItem('token', responseData.token);
        apiClient.defaults.headers.common['Authorization'] = `Bearer ${responseData.token}`;

        await fetchCurrentUser();

        await router.push('/profile');
    }

    async function updateCurrentUser(profileUpdateDTO) {
        const updatedData = await apiClient.put('/api/users/me', profileUpdateDTO);
        if(updatedData) {
            currentUser.value = { ...currentUser.value, ...updatedData };
            localStorage.setItem('user', JSON.stringify(currentUser.value));
        }
    }

    async function logout() {
        try {
            if (token.value) {
                await apiClient.post('/api/auth/logout');
            }
        } catch (error) {
            console.error("调用登出接口失败:", error);
        } finally {
            token.value = null;
            currentUser.value = null;
            localStorage.removeItem('token');
            localStorage.removeItem('user');
            delete apiClient.defaults.headers.common['Authorization'];
            await router.push('/login');
        }
    }

    return {
        token,
        currentUser,
        isLoggedIn,
        login,
        logout,
        fetchCurrentUser,
        updateCurrentUser,
    };
});