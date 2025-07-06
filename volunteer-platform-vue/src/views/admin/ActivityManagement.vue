<template>
  <div class="activity-management-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>后台 - 活动管理</span>
          <div>
            <el-button type="primary" :icon="Plus" @click="handleOpenDialog()">
              发布新活动
            </el-button>
          </div>
        </div>
      </template>

      <el-table :data="tableData" v-loading="loading" style="width: 100%" class="management-table">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="活动标题" show-overflow-tooltip />
        <el-table-column prop="category" label="类别" width="120" />
        <el-table-column prop="location" label="地点" width="180" show-overflow-tooltip />
        <el-table-column prop="startTime" label="开始时间" width="180" />
        <el-table-column prop="recruitmentQuota" label="名额" width="100" align="center"/>
        <el-table-column fixed="right" label="操作" width="180" align="center">
          <template #default="scope">
            <el-button link type="primary" size="small" @click="handleOpenDialog(scope.row)">编辑</el-button>
            <el-button link type="success" size="small" @click="handleViewEnrollments(scope.row)">查看报名</el-button>
            <el-button link type="danger" size="small" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
            background
            :layout="'total, sizes, prev, pager, next'"
            :total="total"
            :page-sizes="[10, 20, 50, 100]"
            :page-size="pageSize"
            :current-page="currentPage"
            @size-change="handleSizeChange"
            @current-change="handlePageChange"
        />
      </div>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="60%" @close="resetForm" top="5vh" class="form-dialog">
      <el-form ref="activityFormRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="活动标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入活动标题" />
        </el-form-item>

        <el-form-item label="活动类别" prop="category">
          <el-select v-model="form.category" placeholder="请选择活动类别" style="width: 100%;">
            <el-option label="环境保护" value="环境保护"></el-option>
            <el-option label="体育活动" value="体育活动"></el-option>
            <el-option label="教育支持" value="教育支持"></el-option>
            <el-option label="关爱老人" value="关爱老人"></el-option>
            <el-option label="关爱儿童" value="关爱儿童"></el-option>
            <el-option label="校内服务" value="校内服务"></el-option>
            <el-option label="校外服务" value="校外服务"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="活动地点" prop="location">
          <el-input v-model="form.location" placeholder="请输入活动地点" />
        </el-form-item>
        <el-form-item label="招募名额" prop="recruitmentQuota">
          <el-input-number v-model="form.recruitmentQuota" :min="1" style="width:100%"/>
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-date-picker
              v-model="form.startTime"
              type="datetime"
              placeholder="选择开始日期时间"
              value-format="YYYY-MM-DD HH:mm:ss"
              style="width: 100%;"
          />
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker
              v-model="form.endTime"
              type="datetime"
              placeholder="选择结束日期时间"
              value-format="YYYY-MM-DD HH:mm:ss"
              style="width: 100%;"
          />
        </el-form-item>

        <el-form-item label="封面图URL" prop="coverImageUrl">
          <el-input v-model="form.coverImageUrl" placeholder="请输入活动封面图片的URL" />
        </el-form-item>
        <el-form-item label="活动描述" prop="description">
          <el-input type="textarea" :rows="5" v-model="form.description" placeholder="请输入活动详细描述" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="handleSubmit">确 定</el-button>
        </span>
      </template>
    </el-dialog>
    <el-dialog v-model="enrollmentDialogVisible" :title="`'${currentActivity.title}' - 报名详情`" width="700px">
      <el-button type="primary" :icon="Download" @click="handleExportEnrollments" style="margin-bottom: 15px;">
        导出为Excel
      </el-button>
      <el-table :data="enrollmentData" v-loading="enrollmentLoading">
        <el-table-column prop="realName" label="姓名" />
        <el-table-column prop="studentId" label="学号" />
        <el-table-column prop="phoneNumber" label="手机号" />
        <el-table-column prop="enrolledAt" label="报名时间" />
      </el-table>
    </el-dialog>

  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import {
  ElMessage, ElMessageBox, ElCard, ElTable, ElTableColumn, ElPagination,
  ElDialog, ElForm, ElFormItem, ElInput, ElButton, ElIcon, ElSelect, ElOption,
  ElInputNumber, ElDatePicker
} from 'element-plus';
import { getActivities, createActivity, updateActivity, deleteActivity, getEnrollments, exportEnrollments } from '@/services/activityApi.js';
import { Plus,Link, Download } from '@element-plus/icons-vue';
const tableData = ref([]);
const loading = ref(true);
const total = ref(0);
const currentPage = ref(1);
const pageSize = ref(10);
const dialogVisible = ref(false);
const dialogTitle = ref('');
const activityFormRef = ref(null);

const handleSizeChange = (newSize) => {
  // 1. 更新每页的条数
  pageSize.value = newSize;
  // 2. 将当前页码重置为第一页
  currentPage.value = 1;
  // 3. 重新获取数据
  fetchActivities();
};


const getInitialForm = () => ({
  id: null,
  title: '',
  description: '',
  coverImageUrl: '', // 确保初始表单数据中包含此字段
  category: '',
  location: '',
  startTime: '',
  endTime: '',
  recruitmentQuota: 10
});

const form = reactive(getInitialForm());

const rules = reactive({
  title: [{ required: true, message: '请输入活动标题', trigger: 'blur' }],
  category: [{ required: true, message: '请选择活动类别', trigger: 'change' }],
  location: [{ required: true, message: '请输入活动地点', trigger: 'blur' }],
  startTime: [{ required: true, message: '请选择开始时间', trigger: 'blur' }],
  endTime: [{ required: true, message: '请选择结束时间', trigger: 'blur' }],
  description: [{ required: true, message: '请输入活动描述', trigger: 'blur' }],
  // coverImageUrl 是选填项，可以不加规则
});

// --- 报名详情对话框 ---
const enrollmentDialogVisible = ref(false);
const enrollmentLoading = ref(false);
const currentActivity = ref({});
const enrollmentData = ref([]);


const fetchActivities = async () => {
  loading.value = true;
  try {
    const data = await getActivities(currentPage.value, pageSize.value);
    tableData.value = data.records;
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
  Object.assign(form, getInitialForm());
  if (activityFormRef.value) {
    activityFormRef.value.clearValidate();
  }
};

const handleOpenDialog = (row) => {
  resetForm();
  if (row && row.id) {
    dialogTitle.value = '编辑活动';
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
          await updateActivity(form.id, form);
          ElMessage.success('活动更新成功');
        } else {
          await createActivity(form);
          ElMessage.success('活动发布成功');
        }
        dialogVisible.value = false;
        await fetchActivities();
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
      await deleteActivity(id);
      ElMessage.success('删除成功');
      await fetchActivities();
    } catch (error) {
      ElMessage.error(error.message || '删除失败');
    }
  }).catch(() => {});
};
const handleViewEnrollments = async (activity) => {
  currentActivity.value = activity;
  enrollmentDialogVisible.value = true;
  enrollmentLoading.value = true;
  try {
    const data = await getEnrollments(activity.id);
    enrollmentData.value = data;
  } catch (error) {
    ElMessage.error('获取报名列表失败');
  } finally {
    enrollmentLoading.value = false;
  }
};

const handleExportEnrollments = async () => {
  try {
    const blob = await exportEnrollments(currentActivity.value.id);
    const link = document.createElement('a');
    link.href = URL.createObjectURL(blob);
    link.download = `${currentActivity.value.title}-报名表.xlsx`;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    URL.revokeObjectURL(link.href);
    ElMessage.success('导出成功！');
  } catch (error) {
    ElMessage.error('导出失败');
  }
};

onMounted(() => {
  fetchActivities();
});
</script>

<style scoped>
.activity-management-container {}
.box-card {
  border: none;
  box-shadow: none;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 1.1rem;
  font-weight: 600;
  color: var(--color-text-heading);
}
.management-table {
  border-radius: 8px;
  border: 1px solid #eef0f3;
}
:deep(.el-table th.el-table__cell) {
  background-color: #f7f8fa !important;
  color: #6b7280;
  font-weight: 600;
}
:deep(.el-table td.el-table__cell) {
  padding: 14px 0;
}
:deep(.el-table tr) {
  transition: background-color 0.2s ease;
}
:deep(.el-table--enable-row-hover .el-table__body tr:hover > td.el-table__cell) {
  background-color: var(--color-primary-soft);
}
.pagination-container {
  margin-top: 24px;
  display: flex;
  justify-content: flex-end;
}
:deep(.form-dialog .el-dialog__header) {
  padding: 20px 24px;
  margin-right: 0;
  border-bottom: 1px solid #eef0f3;
}
:deep(.form-dialog .el-dialog__title) {
  font-weight: 600;
  color: var(--color-text-heading);
}
:deep(.form-dialog .el-dialog__body) {
  padding: 24px;
}
:deep(.form-dialog .el-dialog__footer) {
  padding: 20px 24px;
  border-top: 1px solid #eef0f3;
}
</style>