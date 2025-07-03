<template>
  <div class="user-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>后台 - 用户管理</span>
          <el-button type="primary" @click="handleOpenDialog()">
            <el-icon><Plus /></el-icon>
            <span>添加新用户</span>
          </el-button>
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
        <el-table-column fixed="right" label="操作" width="150">
          <template #default="scope">
            <el-button link type="primary" size="small" @click="handleOpenDialog(scope.row)">编辑</el-button>
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
        <el-form-item label="角色" prop="role">
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
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { getUsers, createUser, updateUser, deleteUser } from '@/services/userAdminApi.js';
import { Plus } from '@element-plus/icons-vue'

// 响应式数据
const tableData = ref([]);
const loading = ref(true);
const total = ref(0);
const currentPage = ref(1);
const pageSize = ref(10);

const dialogVisible = ref(false);
const dialogTitle = ref('');
const userFormRef = ref(null);

// 表单数据模型
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

// 表单验证规则
const rules = reactive({
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }, {min: 6, message: '密码长度不能少于6位', trigger: 'blur'}],
  realName: [{ required: true, message: '请输入真实姓名', trigger: 'blur' }],
  studentId: [{ required: true, message: '请输入学号', trigger: 'blur' }],
  email: [{ required: true, message: '请输入邮箱', trigger: 'blur' }, {type: 'email', message: '请输入正确的邮箱格式', trigger: ['blur', 'change']}],
  role: [{ required: true, message: '请选择角色', trigger: 'change' }],
});

// 方法
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

const handlePageChange = (page) => {
  currentPage.value = page;
  fetchUsers();
};

const resetForm = () => {
  Object.assign(form, getInitialForm());
  if(userFormRef.value) {
    userFormRef.value.clearValidate();
  }
};

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

const handleSubmit = async () => {
  if (!userFormRef.value) return;
  await userFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (form.id) {
          // 更新用户
          const { id, ...updateData } = form;
          await updateUser(id, updateData);
          ElMessage.success('用户更新成功');
        } else {
          // 创建用户
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
  }).catch(() => {
    // 用户取消操作
  });
};

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