// 文件路径: src/stores/userStore.js
import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import apiClient from '@/api/axios.js'; // 导入你的axios实例

export const useUserStore = defineStore('user', () => {
    const router = useRouter();

    // --- State (状态) ---
    // 尝试从 localStorage 读取初始值，确保刷新页面后登录状态不丢失
    const token = ref(localStorage.getItem('token') || null);
    const currentUser = ref(JSON.parse(localStorage.getItem('user')) || null);

    // --- Getters (计算属性) ---
    const isLoggedIn = computed(() => !!token.value);
    const isAdmin = computed(() => currentUser.value?.role === 'admin' || currentUser.value?.role === 'super_admin');

    // --- Actions (方法) ---

    /**
     * 设置Token, 并更新axios请求头
     * @param {string} newToken - 后端返回的JWT
     */
    function setToken(newToken) {
        token.value = newToken;
        localStorage.setItem('token', newToken);
        // 【重要】为后续所有API请求设置认证头，并添加标准的 'Bearer ' 前缀
        apiClient.defaults.headers.common['Authorization'] = `Bearer ${newToken}`;
    }

    /**
     * 获取当前用户的完整信息
     * 在登录成功后或需要刷新用户信息时调用
     */
    async function fetchCurrentUser() {
        if (!token.value) return; // 如果没有token，则不执行
        try {
            // 请求后端 /api/users/me 接口获取当前登录用户的详细数据
            const response = await apiClient.get('/api/users/me');

            currentUser.value = response.data.data;
            localStorage.setItem('user', JSON.stringify(currentUser.value));
        } catch (error) {
            console.error('获取用户信息失败，可能是Token已过期:', error);
            // 获取失败（如token失效），则执行登出操作，清理无效状态
            logout();
        }
    }

    /**
     * 普通用户登录
     * @param {object} credentials - 包含用户名和密码的对象
     */
    async function login(credentials) {
        const response = await apiClient.post('/api/auth/login', credentials);
        const responseData = response.data.data;

        setToken(responseData.token); // 调用函数设置Token
        await fetchCurrentUser(); // 获取完整的用户信息

        // 登录成功后跳转到首页
        await router.push('/');
    }

    /**
     * 管理员登录
     * @param {object} credentials - 包含用户名和密码的对象
     */
    async function adminLogin(credentials) {
        const response = await apiClient.post('/api/admin/auth/login', credentials);
        const responseData = response.data.data;

        setToken(responseData.token); // 同样调用函数设置Token
        await fetchCurrentUser(); // 管理员登录后也获取完整信息

        // 管理员登录成功后跳转到后台页面
        await router.push('/admin/activities');
    }

    /**
     * 更新当前用户信息
     */
    async function updateCurrentUser(profileUpdateDTO) {
        try {
            const response = await apiClient.put('/api/users/me', profileUpdateDTO);
            currentUser.value = response.data.data; // 使用后端返回的最新信息更新本地状态
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

    // --- 初始化检查 ---
    // 当store实例创建时（例如刷新页面），如果localStorage中存在token，
    // 则立即尝试获取一次用户信息，以恢复用户的登录状态。
    if (token.value) {
        apiClient.defaults.headers.common['Authorization'] = `Bearer ${token.value}`;
        fetchCurrentUser();
    }

    // --- 导出所有需要暴露给组件的状态和方法 ---
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