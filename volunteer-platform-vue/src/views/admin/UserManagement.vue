<template>
  <div class="user-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>后台 - 用户管理</span>
          <div>
            <el-button type="info" :icon="Link" @click="goToFrontend">访问前台</el-button>
            <el-button type="primary" @click="handleOpenDialog()">
              <el-icon><Plus /></el-icon>
              <span>添加新用户</span>
            </el-button>
          </div>
        </div>
      </template>

      <el-table :data="tableData" v-loading="loading" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" width="150" />
        <el-table-column prop="realName" label="真实姓名" width="150" />
        <el-table-column prop="email" label="邮箱" />
        <el-table-column prop="role" label="角色" width="120">
          <template #default="scope">
            <el-tag :type="scope.row.role.includes('admin') ? 'warning' : 'primary'">
              {{ scope.row.role }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '正常' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column fixed="right" label="操作" width="200">
          <template #default="scope">
            <el-button link type="primary" size="small" @click="handleOpenDialog(scope.row)">编辑</el-button>
            <el-button link type="warning" size="small" @click="handleOpenPasswordDialog(scope.row)">改密</el-button>
            <el-button link type="danger" size="small" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
          background
          layout="prev, pager, next, total"
          :total="total"
          :page-size="pageSize"
          :current-page="currentPage"
          @current-change="handlePageChange"
          style="margin-top: 20px; justify-content: flex-end;"
      />
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px" @close="resetForm">
      <el-form ref="userFormRef" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入登录名" :disabled="!!form.id" />
        </el-form-item>
        <el-form-item label="密码" prop="password" v-if="!form.id">
          <el-input type="password" v-model="form.password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item label="真实姓名" prop="realName">
          <el-input v-model="form.realName" placeholder="请输入真实姓名" />
        </el-form-item>
        <el-form-item label="学号" prop="studentId" v-if="!form.id">
          <el-input v-model="form.studentId" placeholder="请输入学号" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱地址" />
        </el-form-item>
        <el-form-item label="角色" prop="role" v-if="userStore.currentUser?.role === 'super_admin'">
          <el-select v-model="form.role" placeholder="请选择角色">
            <el-option label="普通志愿者" value="volunteer"></el-option>
            <el-option label="管理员" value="admin"></el-option>
            <el-option label="超级管理员" value="super_admin"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status" v-if="form.id">
          <el-switch
              v-model="form.status"
              :active-value="1"
              :inactive-value="0"
              active-text="正常"
              inactive-text="禁用"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleSubmit">确 定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="passwordDialogVisible" title="修改密码" width="500px" @close="resetPasswordForm">
      <el-form ref="passwordFormRef" :model="passwordForm" :rules="passwordRules" label-width="100px">
        <el-form-item label="用户名">
          <el-input :value="currentUser.username" disabled />
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input type="password" v-model="passwordForm.newPassword" placeholder="请输入新密码" show-password />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="passwordDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handlePasswordSubmit">确 定</el-button>
      </template>
    </el-dialog>

  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { getUsers, createUser, updateUser, deleteUser, updateUserPassword } from '@/services/userAdminApi.js';
import { Plus, Link } from '@element-plus/icons-vue';
import { useUserStore } from '@/stores/userStore.js';

const userStore = useUserStore();

// --- 通用响应式数据 ---
const tableData = ref([]);
const loading = ref(true);
const total = ref(0);
const currentPage = ref(1);
const pageSize = ref(10);


// --- 编辑/创建用户对话框相关 ---
const dialogVisible = ref(false);
const dialogTitle = ref('');
const userFormRef = ref(null);

// --- 密码修改对话框相关 ---
const passwordDialogVisible = ref(false);
const passwordFormRef = ref(null);
const currentUser = ref({});

// --- 表单数据模型 ---
const getInitialForm = () => ({
  id: null,
  username: '',
  password: '',
  realName: '',
  studentId: '',
  email: '',
  role: 'volunteer',
  status: 1,
});
const form = reactive(getInitialForm());

const passwordForm = reactive({
  newPassword: '',
});

// --- 表单验证规则 ---
const rules = reactive({
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }, {min: 6, message: '密码长度不能少于6位', trigger: 'blur'}],
  realName: [{ required: true, message: '请输入真实姓名', trigger: 'blur' }],
  studentId: [{ required: true, message: '请输入学号', trigger: 'blur' }],
  email: [{ required: true, message: '请输入邮箱', trigger: 'blur' }, {type: 'email', message: '请输入正确的邮箱格式', trigger: ['blur', 'change']}],
  role: [{ required: true, message: '请选择角色', trigger: 'change' }],
});

const passwordRules = reactive({
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ]
});

// --- 主要方法 ---

// 跳转到前台
const goToFrontend = () => {
  window.open('/', '_blank');
};

// 获取用户列表
const fetchUsers = async () => {
  loading.value = true;
  try {
    const data = await getUsers(currentPage.value, pageSize.value);
    tableData.value = data.records;
    total.value = data.total;
  } catch (error) {
    ElMessage.error(error.message || '获取用户列表失败');
  } finally {
    loading.value = false;
  }
};

// 翻页
const handlePageChange = (page) => {
  currentPage.value = page;
  fetchUsers();
};

// 重置编辑/创建表单
const resetForm = () => {
  Object.assign(form, getInitialForm());
  if(userFormRef.value) {
    userFormRef.value.clearValidate();
  }
};

// 打开编辑/创建对话框
const handleOpenDialog = (row) => {
  resetForm();
  if (row && row.id) {
    dialogTitle.value = '编辑用户';
    Object.assign(form, row);
  } else {
    dialogTitle.value = '添加新用户';
  }
  dialogVisible.value = true;
};

// 提交编辑/创建
const handleSubmit = async () => {
  if (!userFormRef.value) return;
  await userFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (form.id) {
          const { id, ...updateData } = form;
          await updateUser(id, updateData);
          ElMessage.success('用户更新成功');
        } else {
          await createUser(form);
          ElMessage.success('用户创建成功');
        }
        dialogVisible.value = false;
        await fetchUsers();
      } catch (error) {
        ElMessage.error(error.message || '操作失败');
      }
    }
  });
};

// 删除用户
const handleDelete = (id) => {
  ElMessageBox.confirm('确定要删除这个用户吗？此操作无法撤销。', '警告', {
    confirmButtonText: '确定删除',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(async () => {
    try {
      await deleteUser(id);
      ElMessage.success('删除成功');
      await fetchUsers();
    } catch (error) {
      ElMessage.error(error.message || '删除失败');
    }
  }).catch(() => {});
};

// --- 密码修改相关方法 ---

// 重置密码表单
const resetPasswordForm = () => {
  passwordForm.newPassword = '';
  if(passwordFormRef.value) {
    passwordFormRef.value.clearValidate();
  }
};

// 打开密码修改对话框
const handleOpenPasswordDialog = (row) => {
  resetPasswordForm();
  currentUser.value = { ...row };
  passwordDialogVisible.value = true;
};

// 提交新密码
const handlePasswordSubmit = async () => {
  if (!passwordFormRef.value) return;
  await passwordFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await updateUserPassword(currentUser.value.id, passwordForm.newPassword);
        ElMessage.success('密码更新成功');
        passwordDialogVisible.value = false;
      } catch (error) {
        ElMessage.error(error.message || '密码更新失败');
      }
    }
  });
};

// --- 生命周期钩子 ---

onMounted(() => {
  fetchUsers();
});
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.user-management {
  padding: 20px;
}
.el-select {
  width: 100%;
}
</style>