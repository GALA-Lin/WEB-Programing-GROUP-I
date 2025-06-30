// 文件路径: src/stores/userStore.js
import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import apiClient from '@/api/axios.js'; // 导入你的axios实例

export const useUserStore = defineStore('user', () => {
    const router = useRouter();

    // --- State (状态) ---
    // 尝试从 localStorage 读取初始值，这样刷新页面后状态不会丢失
    const token = ref(localStorage.getItem('token') || null);
    const currentUser = ref(JSON.parse(localStorage.getItem('user')) || null);

    // --- Getters (计算属性) ---
    // 判断用户是否已登录
    const isLoggedIn = computed(() => !!token.value);
    // 判断用户是否是管理员
    const isAdmin = computed(() => currentUser.value?.role === 'admin' || currentUser.value?.role === 'super_admin');

    // --- Actions (方法) ---

    /**
     * 登录并保存状态
     * @param {object} credentials - 包含用户名和密码的对象
     */
    async function login(credentials) {
        const response = await apiClient.post('/api/auth/login', credentials);
        const responseData = response.data.data; // 假设后端返回的数据在 data.data 中

        // 1. 保存 Token
        token.value = responseData.token;
        localStorage.setItem('token', responseData.token);

        // 2. 保存用户信息
        // (最佳实践是再请求一个 /api/users/me 接口获取，这里我们先用登录返回的信息)
        currentUser.value = {
            username: responseData.username,
            role: responseData.role
        };
        localStorage.setItem('user', JSON.stringify(currentUser.value));

        // 3. 更新axios的默认请求头，让后续所有请求都自动带上Token
        // 注意：你队友实现的JWT可能不需要 'Bearer ' 前缀，根据实际情况调整
        apiClient.defaults.headers.common['Authorization'] = `${responseData.token}`;
    }

    /**
     * 退出登录
     */
    function logout() {
        // 清空 Pinia 中的状态
        token.value = null;
        currentUser.value = null;

        // 清空 localStorage
        localStorage.removeItem('token');
        localStorage.removeItem('user');

        // 移除 axios 的默认请求头
        delete apiClient.defaults.headers.common['Authorization'];

        // 跳转到登录页
        router.push('/login');
    }

    // 返回所有需要暴露给组件的状态和方法
    return {
        token,
        currentUser,
        isLoggedIn,
        isAdmin,
        login,
        logout
    };
});