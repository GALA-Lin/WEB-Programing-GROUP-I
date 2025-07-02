// 文件路径: src/stores/userStore.js
import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import apiClient from '@/api/axios.js'; // 导入你的axios实例

export const useUserStore = defineStore('user', () => {
    const router = useRouter();

    // --- State (状态) ---
    const token = ref(localStorage.getItem('token') || null);
    // 【修改】将 currentUser 重命名为 profile 以和后端接口对应，或者保持原样也可以
    const currentUser = ref(JSON.parse(localStorage.getItem('user')) || null);

    // --- Getters (计算属性) ---
    const isLoggedIn = computed(() => !!token.value);
    const isAdmin = computed(() => currentUser.value?.role === 'admin'); // 根据你的角色设定调整

    // --- Actions (方法) ---

    /**
     * 【新增】获取当前用户完整信息的方法
     * 这个方法将在登录成功后，或者在需要刷新用户信息时调用
     */
    async function fetchCurrentUser() {
        if (!token.value) return;
        try {
            const response = await apiClient.get('/api/users/me');
            currentUser.value = response.data.data;
            localStorage.setItem('user', JSON.stringify(currentUser.value));
        } catch (error) {
            console.error('获取用户信息失败:', error);
            // 如果获取失败（例如token失效），则执行登出操作
            logout();
        }
    }

    /**
     * 登录
     */
    async function login(credentials) {
        const response = await apiClient.post('/api/auth/login', credentials);
        const responseData = response.data.data;

        // 1. 保存 Token
        token.value = responseData.token;
        localStorage.setItem('token', responseData.token);

        // 2. 【重要修改】更新axios的默认请求头，必须加上 'Bearer ' 前缀
        apiClient.defaults.headers.common['Authorization'] = `Bearer ${responseData.token}`;

        // 3. 【修改】登录成功后，调用fetchCurrentUser获取完整的用户信息
        await fetchCurrentUser();

        // 4. 跳转到用户中心或首页
        router.push('/profile');
    }

    /**
     * 【新增】更新当前用户信息的方法
     */
    async function updateCurrentUser(profileUpdateDTO) {
        try {
            const response = await apiClient.put('/api/users/me', profileUpdateDTO);
            // 使用后端返回的最新信息更新本地状态
            currentUser.value = response.data.data;
            localStorage.setItem('user', JSON.stringify(currentUser.value));
            alert('信息更新成功！');
        } catch (error) {
            console.error('更新用户信息失败:', error);
            alert('更新失败，请重试。');
            throw error; // 抛出错误，方便组件捕获
        }
    }


    /**
     * 退出登录
     */
    async function logout() {
        try {
            // 【新增】通知后端执行登出（即使是无状态，这也是一个好习惯）
            if (token.value) {
                await apiClient.post('/api/auth/logout');
            }
        } catch (error) {
            console.error("调用登出接口失败，但将继续执行本地登出:", error);
        } finally {
            // 清空 Pinia 中的状态
            token.value = null;
            currentUser.value = null;

            // 清空 localStorage
            localStorage.removeItem('token');
            localStorage.removeItem('user');

            // 移除 axios 的默认请求头
            delete apiClient.defaults.headers.common['Authorization'];

            // 跳转到登录页
            router.push('/login'); // 根据你的路由配置，路径可能是/auth或/login
        }
    }

    // 返回所有需要暴露给组件的状态和方法
    return {
        token,
        currentUser,
        isLoggedIn,
        isAdmin,
        login,
        logout,
        fetchCurrentUser,     // 【新增】导出
        updateCurrentUser,  // 【新增】导出
    };
});