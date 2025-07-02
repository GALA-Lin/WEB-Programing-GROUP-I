// 文件路径: AAA/volunteer-platform-vue/src/stores/userStore.js
import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import apiClient from '@/api/axios.js';
import { ElMessage } from 'element-plus';

export const useUserStore = defineStore('user', () => {
    const router = useRouter();

    const token = ref(localStorage.getItem('token') || null);
    const currentUser = ref(JSON.parse(localStorage.getItem('user')) || null);

    const isLoggedIn = computed(() => !!token.value);
    const isAdmin = computed(() => currentUser.value?.role === 'admin' || currentUser.value?.role === 'super_admin');

    async function fetchCurrentUser() {
        if (!token.value) return;
        try {
            const userProfile = await apiClient.get('/api/users/me');
            currentUser.value = userProfile;
            localStorage.setItem('user', JSON.stringify(currentUser.value));
        } catch (error) {
            console.error('获取用户信息失败:', error);
            // 如果token失效导致获取失败，则登出
            logout();
        }
    }

    /**
     * 【已修复】登录逻辑
     */
    async function login(credentials) {
        // apiClient.post 现在直接返回后端 Result.data 的内容，即 { token: "..." }
        const responseData = await apiClient.post('/api/auth/login', credentials);

        // 直接从 responseData 中获取 token
        token.value = responseData.token;
        localStorage.setItem('token', responseData.token);
        apiClient.defaults.headers.common['Authorization'] = `Bearer ${responseData.token}`;

        await fetchCurrentUser();
        router.push('/'); // 登录成功后跳转到首页
    }

    /**
     * 【已修复】管理员登录逻辑
     */
    async function adminLogin(credentials) {
        const responseData = await apiClient.post('/api/admin/auth/login', credentials);

        token.value = responseData.token;
        localStorage.setItem('token', responseData.token);
        apiClient.defaults.headers.common['Authorization'] = `Bearer ${responseData.token}`;

        await fetchCurrentUser();
        // 跳转由 AuthView.vue 组件完成
    }

    async function updateCurrentUser(profileUpdateDTO) {
        try {
            const updatedUser = await apiClient.put('/api/users/me', profileUpdateDTO);
            currentUser.value = updatedUser;
            localStorage.setItem('user', JSON.stringify(currentUser.value));
            ElMessage.success('信息更新成功！');
        } catch (error) {
            // ElMessage.error 已经在拦截器中处理，这里可以只打印日志
            console.error('更新用户信息失败:', error);
            throw error;
        }
    }

    async function logout() {
        // 不再需要调用后端登出接口，因为JWT是无状态的，前端删除token即可
        token.value = null;
        currentUser.value = null;
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        delete apiClient.defaults.headers.common['Authorization'];
        router.push('/login');
    }

    return {
        token,
        currentUser,
        isLoggedIn,
        isAdmin,
        login,
        adminLogin,
        logout,
        fetchCurrentUser,
        updateCurrentUser,
    };
});