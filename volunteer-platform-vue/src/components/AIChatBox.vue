<template>
  <div class="chat-container" :class="{ 'chat-expanded': isExpanded }">
    <div class="chat-header" @click="toggleChat">
      <span>智能助手</span>
      <button class="toggle-btn">{{ isExpanded ? '−' : '+' }}</button>
    </div>

    <div v-if="isExpanded" class="chat-body">
      <div class="messages-container" ref="messagesContainer">
        <div v-for="(msg, index) in messages" :key="index"
             :class="['message', msg.isBot ? 'bot-message' : 'user-message']">
          <div class="message-bubble">{{ msg.text }}</div>
          <div class="message-time">{{ formatTime(msg.timestamp) }}</div>
        </div>
        <div v-if="loading" class="message bot-message">
          <div class="message-bubble loading-bubble">
            <span class="dot"></span>
            <span class="dot"></span>
            <span class="dot"></span>
          </div>
        </div>
      </div>

      <div class="chat-input">
        <input
            v-model="userInput"
            @keyup.enter="sendMessage"
            placeholder="输入你的问题..."
            :disabled="loading"
        />
        <button @click="sendMessage" :disabled="loading || !userInput.trim()">
          发送
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'AIChatBox',
  data() {
    return {
      isExpanded: false,
      userInput: '',
      messages: [],
      loading: false
    };
  },
  mounted() {
    // 添加默认欢迎消息
    this.messages.push({
      text: '你好！我是油炬智愿的AI助手，有关于志愿服务的问题都可以问我。',
      isBot: true,
      timestamp: new Date()
    });
  },
  methods: {
    toggleChat() {
      this.isExpanded = !this.isExpanded;
    },
    async sendMessage() {
      if (!this.userInput.trim() || this.loading) return;

      // 添加用户消息到聊天记录
      const userMessage = this.userInput.trim();
      this.messages.push({
        text: userMessage,
        isBot: false,
        timestamp: new Date()
      });

      this.userInput = '';
      this.loading = true;

      // 滚动到底部
      this.$nextTick(() => {
        this.scrollToBottom();
      });

      try {
        // 调用后端API
        const response = await axios.post('/api/chat/send', {
          message: userMessage
        });

        // 添加机器人回复
        if (response.data && response.data.data) {
          this.messages.push({
            text: response.data.data.message,
            isBot: true,
            timestamp: new Date()
          });
        } else {
          throw new Error('无效的响应格式');
        }
      } catch (error) {
        console.error('发送消息失败:', error);
        this.messages.push({
          text: '抱歉，我暂时无法回应。请稍后再试。',
          isBot: true,
          timestamp: new Date()
        });
      } finally {
        this.loading = false;
        // 滚动到底部
        this.$nextTick(() => {
          this.scrollToBottom();
        });
      }
    },
    scrollToBottom() {
      if (this.$refs.messagesContainer) {
        this.$refs.messagesContainer.scrollTop = this.$refs.messagesContainer.scrollHeight;
      }
    },
    formatTime(date) {
      if (!date) return '';
      const d = new Date(date);
      return `${d.getHours().toString().padStart(2, '0')}:${d.getMinutes().toString().padStart(2, '0')}`;
    }
  }
};
</script>

<style scoped>
.chat-container {
  position: fixed;
  bottom: 20px;
  right: 20px;
  width: 350px;
  background-color: #fff;
  border-radius: 10px;
  box-shadow: 0 5px 20px rgba(0, 0, 0, 0.2);
  overflow: hidden;
  z-index: 1000;
  transition: all 0.3s ease;
  max-height: 500px;
}

.chat-header {
  background-color: #1e88e5;
  color: white;
  padding: 15px 20px;
  font-weight: bold;
  display: flex;
  justify-content: space-between;
  align-items: center;
  cursor: pointer;
}

.toggle-btn {
  background: none;
  border: none;
  color: white;
  font-size: 20px;
  cursor: pointer;
}

.chat-body {
  display: flex;
  flex-direction: column;
  height: 400px;
}

.messages-container {
  flex: 1;
  overflow-y: auto;
  padding: 15px;
}

.message {
  margin-bottom: 10px;
  display: flex;
  flex-direction: column;
}

.user-message {
  align-items: flex-end;
}

.bot-message {
  align-items: flex-start;
}

.message-bubble {
  max-width: 80%;
  padding: 10px 15px;
  border-radius: 18px;
  word-wrap: break-word;
}

.user-message .message-bubble {
  background-color: #1e88e5;
  color: white;
  border-bottom-right-radius: 5px;
}

.bot-message .message-bubble {
  background-color: #f1f1f1;
  color: #333;
  border-bottom-left-radius: 5px;
}

.message-time {
  font-size: 12px;
  color: #888;
  margin-top: 5px;
}

.chat-input {
  display: flex;
  padding: 10px;
  background-color: #f8f8f8;
  border-top: 1px solid #eee;
}

.chat-input input {
  flex: 1;
  padding: 10px 15px;
  border: 1px solid #ddd;
  border-radius: 20px;
  outline: none;
}

.chat-input button {
  margin-left: 10px;
  padding: 10px 15px;
  background-color: #1e88e5;
  color: white;
  border: none;
  border-radius: 20px;
  cursor: pointer;
}

.chat-input button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.loading-bubble {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 15px;
}

.dot {
  display: inline-block;
  width: 8px;
  height: 8px;
  margin: 0 3px;
  background-color: #888;
  border-radius: 50%;
  animation: dot-pulse 1.5s infinite ease-in-out;
}

.dot:nth-child(2) {
  animation-delay: 0.2s;
}

.dot:nth-child(3) {
  animation-delay: 0.4s;
}

@keyframes dot-pulse {
  0%, 100% { transform: scale(0.7); opacity: 0.5; }
  50% { transform: scale(1); opacity: 1; }
}

@media (max-width: 500px) {
  .chat-container {
    width: 300px;
    right: 10px;
    bottom: 10px;
  }
}
</style>