import axios from '../api/axios';

export const chatApi = {
    /**
     * 发送消息到AI聊天服务
     * @param {string} message - 用户输入的消息内容
     * @returns {Promise} - 返回服务器响应
     */
    sendMessage(message) {
        return axios.post('/api/chat/send', { message });
    }
};

export default chatApi;