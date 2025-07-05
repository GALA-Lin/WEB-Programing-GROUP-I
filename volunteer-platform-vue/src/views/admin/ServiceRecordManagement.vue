<template>
  <div class="record-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>后台 - 志愿时长管理</span>
          <el-button type="primary" :icon="Plus" @click="handleOpenDialog()">登记新时长</el-button>
        </div>
      </template>

      <el-table :data="tableData" v-loading="loading" style="width: 100%">
        <el-table-column prop="id" label="记录ID" width="80" />
        <el-table-column prop="realName" label="志愿者姓名" width="150" />
        <el-table-column prop="activityTitle" label="所属活动" />
        <el-table-column prop="serviceHours" label="服务时长(小时)" width="150" />
        <el-table-column prop="remarks" label="备注" show-overflow-tooltip />
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

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px" @close="resetForm">
      <el-form ref="recordFormRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="用户ID" prop="userId">
          <el-input-number v-model="form.userId" :min="1" placeholder="请输入用户ID" style="width: 100%;"/>
        </el-form-item>
        <el-form-item label="活动ID" prop="activityId">
          <el-input-number v-model="form.activityId" :min="1" placeholder="请输入活动ID" style="width: 100%;"/>
        </el-form-item>
        <el-form-item label="服务时长" prop="serviceHours">
          <el-input-number v-model="form.serviceHours" :precision="2" :step="0.5" :min="0" style="width: 100%;"/>
        </el-form-item>
        <el-form-item label="备注" prop="remarks">
          <el-input type="textarea" v-model="form.remarks" placeholder="请输入备注信息" />
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
import { ref, reactive, computed, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { getServiceRecords, createServiceRecord, updateServiceRecord, deleteServiceRecord } from '@/services/serviceRecordAdminApi.js';
import { Plus } from '@element-plus/icons-vue';

// 表格和分页的真实数据状态
const tableData = ref([]);
const total = ref(0);
const currentPage = ref(1);
const pageSize = ref(10);
const loading = ref(true);

// 对话框状态
const dialogVisible = ref(false);
const recordFormRef = ref(null);

const getInitialForm = () => ({
  id: null,
  userId: null,
  activityId: null,
  serviceHours: 1.0,
  remarks: '',
});

const form = reactive(getInitialForm());

const rules = {
  userId: [{ required: true, message: '请输入用户ID', trigger: 'blur' }],
  activityId: [{ required: true, message: '请输入活动ID', trigger: 'blur' }],
  serviceHours: [{ required: true, message: '请输入服务时长', trigger: 'blur' }],
};

const dialogTitle = computed(() => (form.id ? '编辑时长记录' : '登记新时长'));

/**
 * 获取时长记录列表
 */
const fetchRecords = async () => {
  loading.value = true;
  try {
    const data = await getServiceRecords(currentPage.value, pageSize.value);
    tableData.value = data.records;
    total.value = data.total;
  } catch (error) {
    ElMessage.error('获取时长列表失败');
  } finally {
    loading.value = false;
  }
};

/**
 * 处理翻页事件
 */
const handlePageChange = (page) => {
  currentPage.value = page;
  fetchRecords();
};

const resetForm = () => {
  Object.assign(form, getInitialForm());
  recordFormRef.value?.clearValidate();
};

const handleOpenDialog = (row) => {
  resetForm();
  if (row?.id) {
    // 过滤掉不属于表单的数据
    const { realName, activityTitle, ...coreData } = row;
    Object.assign(form, coreData);
  }
  dialogVisible.value = true;
};

const handleSubmit = async () => {
  if (!recordFormRef.value) return;
  await recordFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const action = form.id
            ? updateServiceRecord(form.id, form)
            : createServiceRecord(form);
        await action;
        ElMessage.success(form.id ? '更新成功' : '创建成功');
        dialogVisible.value = false;
        await fetchRecords(); // 刷新列表
      } catch (error) {
        ElMessage.error(error.message || '操作失败');
      }
    }
  });
};

const handleDelete = (id) => {
  ElMessageBox.confirm('确定要删除这条时长记录吗？用户的总时长也会相应扣除。', '警告', {
    confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning',
  }).then(async () => {
    try {
      await deleteServiceRecord(id);
      ElMessage.success('删除成功');
      await fetchRecords(); // 刷新列表
    } catch (error) {
      ElMessage.error(error.message || '删除失败');
    }
  });
};

// 组件加载完成后，自动获取第一页数据
onMounted(() => {
  fetchRecords();
});
</script>

<style scoped>
.record-management {
  padding: 20px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>