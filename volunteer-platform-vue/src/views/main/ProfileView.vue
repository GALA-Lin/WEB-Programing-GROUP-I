<template>
  <div class="profile-page">
    <div class="profile-card">
      <h1 class="card-title">用户中心</h1>
      <p class="card-subtitle">查看或更新你的个人信息</p>

      <div v-if="userStore.currentUser" class="profile-form">
        <div class="avatar-section">
          <img :src="userStore.currentUser.avatarUrl || defaultAvatar" alt="Avatar" class="avatar">
          <h2 class="username">{{ userStore.currentUser.username }}</h2>
        </div>

        <div class="form-group">
          <label for="realName">真实姓名</label>
          <input type="text" id="realName" v-model="editForm.realName" :disabled="!isEditing">
        </div>

        <div class="form-group">
          <label for="studentId">学号</label>
          <input type="text" id="studentId" :value="userStore.currentUser.studentId" disabled>
        </div>

        <div class="form-group">
          <label for="email">邮箱</label>
          <input type="email" id="email" v-model="editForm.email" :disabled="!isEditing">
        </div>

        <div class="form-group">
          <label for="phoneNumber">手机号</label>
          <input type="text" id="phoneNumber" v-model="editForm.phoneNumber" :disabled="!isEditing">
        </div>

        <div class="actions">
          <button v-if="!isEditing" @click="startEditing" class="button-primary">编辑信息</button>
          <template v-if="isEditing">
            <button @click="handleSave" class="button-success">保存更改</button>
            <button @click="cancelEditing" class="button-secondary">取消</button>
          </template>
        </div>

        <div class="logout-section">
          <button @click="handleLogout" class="logout-button">退出登录</button>
        </div>

      </div>

      <div v-else class="loading">
        正在加载用户信息...
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue';
import { useUserStore } from '@/stores/userStore.js';

const userStore = useUserStore();
const isEditing = ref(false);
const defaultAvatar = 'https://ui-avatars.com/api/?name=User&background=random'; // 默认头像

// 创建一个响应式对象用于表单编辑，避免在用户点“保存”前直接修改store中的数据
const editForm = ref({
  realName: '',
  email: '',
  phoneNumber: ''
});

// 侦听 store 中的 currentUser 变化，
// 这样即使用户刷新页面，只要localStorage中有数据，表单就能被正确填充。
watch(
    () => userStore.currentUser,
    (newProfile) => {
      if (newProfile) {
        editForm.value.realName = newProfile.realName;
        editForm.value.email = newProfile.email;
        editForm.value.phoneNumber = newProfile.phoneNumber;
      }
    },
    { immediate: true, deep: true }
);


// 组件挂载时，如果store中没有用户信息，则主动调用action从后端获取
onMounted(() => {
  if (!userStore.currentUser) {
    userStore.fetchCurrentUser();
  }
});

// 切换到编辑模式
const startEditing = () => {
  isEditing.value = true;
};

// 取消编辑，将表单数据重置为store中的原始数据
const cancelEditing = () => {
  isEditing.value = false;
  if (userStore.currentUser) {
    editForm.value.realName = userStore.currentUser.realName;
    editForm.value.email = userStore.currentUser.email;
    editForm.value.phoneNumber = userStore.currentUser.phoneNumber;
  }
};

// 保存更改
const handleSave = async () => {
  try {
    // 调用store中的action来提交更新
    await userStore.updateCurrentUser(editForm.value);
    isEditing.value = false; // 更新成功后，退出编辑模式
  } catch (error) {
    // store中的action已经处理了错误提示，这里可以留空
  }
};

// 退出登录
const handleLogout = () => {
  if (confirm('您确定要退出登录吗？')) {
    userStore.logout();
  }
};
</script>

<style scoped>
.profile-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f4f6f8;
  padding: 40px;
}
.profile-card {
  width: 100%;
  max-width: 600px;
  background-color: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  padding: 30px 40px;
}
.card-title {
  font-size: 28px;
  font-weight: 600;
  text-align: center;
  color: #333;
}
.card-subtitle {
  text-align: center;
  color: #777;
  margin-bottom: 30px;
}
.avatar-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 25px;
}
.avatar {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  object-fit: cover;
  border: 3px solid #eee;
  margin-bottom: 10px;
}
.username {
  font-size: 20px;
  font-weight: 500;
}
.form-group {
  margin-bottom: 20px;
}
.form-group label {
  display: block;
  font-weight: 500;
  margin-bottom: 8px;
}
.form-group input {
  width: 100%;
  padding: 12px;
  border: 1px solid #d9d9d9;
  border-radius: 6px;
  font-size: 14px;
}
.form-group input:disabled {
  background-color: #f5f5f5;
  cursor: not-allowed;
  color: #555;
}
.actions {
  margin-top: 30px;
  display: flex;
  justify-content: center;
  gap: 15px;
}
.button-primary, .button-success, .button-secondary {
  padding: 12px 25px;
  border: none;
  border-radius: 6px;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s;
}
.button-primary { background-color: #1890ff; color: white; }
.button-primary:hover { background-color: #40a9ff; }
.button-success { background-color: #52c41a; color: white; }
.button-success:hover { background-color: #73d13d; }
.button-secondary { background-color: #f0f0f0; color: #333; }
.button-secondary:hover { background-color: #e0e0e0; }
.logout-section {
  text-align: center;
  margin-top: 30px;
  padding-top: 20px;
  border-top: 1px solid #f0f0f0;
}
.logout-button {
  background-color: #ff4d4f;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 14px;
}
.logout-button:hover {
  background-color: #cf1322;
}
.loading {
  text-align: center;
  padding: 50px;
  color: #777;
}
</style>