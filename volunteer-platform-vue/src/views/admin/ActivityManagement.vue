<template>
  <div class="activity-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>后台 - 活动管理</span>
          <el-button type="primary" @click="handleOpenDialog()">发布新活动</el-button>
        </div>
      </template>

      <el-table :data="tableData" v-loading="loading" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="活动标题" width="200" />
        <el-table-column prop="category" label="分类" width="120" />
        <el-table-column prop="location" label="地点" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 'recruiting' ? 'success' : 'info'">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="recruitmentQuota" label="招募名额" width="100" />
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
      <el-form ref="activityFormRef" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="活动标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入活动标题" />
        </el-form-item>
        <el-form-item label="活动描述" prop="description">
          <el-input type="textarea" v-model="form.description" placeholder="请输入活动描述" />
        </el-form-item>
        <el-form-item label="封面图URL" prop="coverImageUrl">
          <el-input v-model="form.coverImageUrl" placeholder="请输入图片URL" />
        </el-form-item>
        <el-form-item label="活动分类" prop="category">
          <el-input v-model="form.category" placeholder="例如：校内服务" />
        </el-form-item>
        <el-form-item label="活动地点" prop="location">
          <el-input v-model="form.location" />
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-date-picker v-model="form.startTime" type="datetime" placeholder="选择开始时间" format="YYYY-MM-DD HH:mm:ss" value-format="YYYY-MM-DDTHH:mm:ss"/>
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker v-model="form.endTime" type="datetime" placeholder="选择结束时间" format="YYYY-MM-DD HH:mm:ss" value-format="YYYY-MM-DDTHH:mm:ss"/>
        </el-form-item>
        <el-form-item label="招募名额" prop="recruitmentQuota">
          <el-input-number v-model="form.recruitmentQuota" :min="1" />
        </el-form-item>
        <el-form-item label="组织者ID" prop="organizerId">
          <el-input-number v-model="form.organizerId" :min="1" />
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
import { getActivities, createActivity, updateActivity, deleteActivity } from '@/services/activityApi.js';

// 响应式数据
const tableData = ref([]);
const loading = ref(true);
const total = ref(0);
const currentPage = ref(1);
const pageSize = ref(10);

const dialogVisible = ref(false);
const dialogTitle = ref('');
const activityFormRef = ref(null);

// 表单数据模型 (参考 ActivityCreateDTO.java)
const form = reactive({
  id: null,
  title: '',
  description: '',
  coverImageUrl: '',
  category: '',
  location: '',
  startTime: '',
  endTime: '',
  organizerId: 1, // 默认值
  recruitmentQuota: 20, // 默认值
});

// 表单验证规则
const rules = {
  title: [{ required: true, message: '请输入活动标题', trigger: 'blur' }],
  startTime: [{ required: true, message: '请选择开始时间', trigger: 'change' }],
  endTime: [{ required: true, message: '请选择结束时间', trigger: 'change' }],
  recruitmentQuota: [{ required: true, message: '请输入招募名额', trigger: 'blur' }],
  organizerId: [{ required: true, message: '请输入组织者ID', trigger: 'blur' }]
};

// 方法
const fetchActivities = async () => {
  loading.value = true;
  try {
    const data = await getActivities(currentPage.value, pageSize.value); //
    tableData.value = data.records; // MyBatis-Plus分页结果在records字段
    total.value = data.total;
  } catch (error) {
    ElMessage.error(error.message || '获取活动列表失败');
  } finally {
    loading.value = false;
  }
};

const handlePageChange = (page) => {
  currentPage.value = page;
  fetchActivities();
};

const resetForm = () => {
  if (activityFormRef.value) {
    activityFormRef.value.resetFields();
  }
  // 手动清空表单
  Object.assign(form, {
    id: null, title: '', description: '', coverImageUrl: '', category: '',
    location: '', startTime: '', endTime: '', organizerId: 1, recruitmentQuota: 20
  });
};

const handleOpenDialog = (row) => {
  resetForm();
  if (row && row.id) {
    dialogTitle.value = '编辑活动';
    // 将行数据填充到表单中
    Object.assign(form, row);
  } else {
    dialogTitle.value = '发布新活动';
  }
  dialogVisible.value = true;
};

const handleSubmit = async () => {
  if (!activityFormRef.value) return;
  await activityFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (form.id) {
          // 更新活动
          await updateActivity(form.id, form);
          ElMessage.success('活动更新成功');
        } else {
          // 创建活动
          await createActivity(form);
          ElMessage.success('活动发布成功');
        }
        dialogVisible.value = false;
        fetchActivities(); // 刷新列表
      } catch (error) {
        ElMessage.error(error.message || '操作失败');
      }
    }
  });
};

const handleDelete = (id) => {
  ElMessageBox.confirm('确定要删除这个活动吗？此操作无法撤销。', '警告', {
    confirmButtonText: '确定删除',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(async () => {
    try {
      await deleteActivity(id); //
      ElMessage.success('删除成功');
      fetchActivities(); // 刷新列表
    } catch (error) {
      ElMessage.error(error.message || '删除失败');
    }
  }).catch(() => {
    // 用户取消操作
  });
};


// 组件挂载后执行
onMounted(() => {
  fetchActivities();
});
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.activity-management {
  padding: 20px;
}
</style>