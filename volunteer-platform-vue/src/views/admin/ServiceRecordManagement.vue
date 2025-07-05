<template>
  <div class="service-record-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>时长管理 - {{ activityTitle }}</span>
          <el-button type="primary" @click="handleOpenDialog()" :icon="Plus">
            登记新时长
          </el-button>
        </div>
      </template>

      <el-table :data="tableData" v-loading="loading" style="width: 100%">
        <el-table-column prop="recordId" label="记录ID" width="80" />
        <el-table-column prop="realName" label="志愿者姓名" width="150" />
        <el-table-column prop="studentId" label="学号" width="150" />
        <el-table-column prop="serviceHours" label="服务时长(小时)" width="150" />
        <el-table-column prop="remarks" label="备注" show-overflow-tooltip />
        <el-table-column prop="recordedAt" label="登记时间" width="180" />
        <el-table-column fixed="right" label="操作" width="150">
          <template #default="scope">
            <el-button link type="primary" size="small" @click="handleOpenDialog(scope.row)">编辑</el-button>
            <el-button link type="danger" size="small" @click="handleDelete(scope.row.recordId)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
          background layout="prev, pager, next, total"
          :total="total" :page-size="pageSize" :current-page="currentPage"
          @current-change="handlePageChange"
          style="margin-top: 20px; justify-content: flex-end;"
      />
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px" @close="resetForm">
      <el-form ref="recordFormRef" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="用户ID" prop="userId">
          <el-input-number v-model="form.userId" :min="1" placeholder="请输入要登记的用户的ID" :disabled="!!form.recordId" style="width:100%"/>
        </el-form-item>
        <el-form-item label="服务时长" prop="serviceHours">
          <el-input-number v-model="form.serviceHours" :precision="2" :step="0.5" :min="0" placeholder="请输入服务时长" style="width:100%"/>
        </el-form-item>
        <el-form-item label="备注" prop="remarks">
          <el-input type="textarea" v-model="form.remarks" placeholder="例如：该同学表现优异" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue';
import { useRoute } from 'vue-router';
import { ElMessage, ElMessageBox } from 'element-plus';
import { getServiceRecordsByActivity, createServiceRecord, updateServiceRecord, deleteServiceRecord } from '@/services/serviceRecordAdminApi.js';
import { Plus } from '@element-plus/icons-vue';

const route = useRoute();
const activityId = computed(() => Number(route.params.id));
const activityTitle = computed(() => route.query.title || '活动');

const tableData = ref([]);
const loading = ref(true);
const total = ref(0);
const currentPage = ref(1);
const pageSize = ref(10);

const dialogVisible = ref(false);
const dialogTitle = ref('');
const recordFormRef = ref(null);

const getInitialForm = () => ({
  recordId: null,
  userId: null,
  serviceHours: 0,
  remarks: '',
});
const form = reactive(getInitialForm());

const rules = {
  userId: [{ required: true, message: '请输入用户ID', trigger: 'blur' }],
  serviceHours: [{ required: true, message: '请输入服务时长', trigger: 'blur' }],
};

const fetchRecords = async () => {
  loading.value = true;
  try {
    const data = await getServiceRecordsByActivity(activityId.value, currentPage.value, pageSize.value);
    tableData.value = data.records;
    total.value = data.total;
  } catch (error) {
    ElMessage.error(error.message || '获取时长列表失败');
  } finally {
    loading.value = false;
  }
};

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
  dialogTitle.value = row?.recordId ? '编辑时长记录' : '登记新时长';
  if (row?.recordId) {
    form.recordId = row.recordId;
    form.userId = row.userId; // 编辑时，userId也应填充，但设为不可编辑
    form.serviceHours = row.serviceHours;
    form.remarks = row.remarks;
  }
  dialogVisible.value = true;
};

const handleSubmit = async () => {
  await recordFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (form.recordId) {
          await updateServiceRecord(form.recordId, { serviceHours: form.serviceHours, remarks: form.remarks });
          ElMessage.success('更新成功');
        } else {
          await createServiceRecord({ ...form, activityId: activityId.value });
          ElMessage.success('登记成功');
        }
        dialogVisible.value = false;
        fetchRecords();
      } catch (error) {
        ElMessage.error(error.message || '操作失败');
      }
    }
  });
};

const handleDelete = (recordId) => {
  ElMessageBox.confirm('确定要删除这条时长记录吗？此操作会影响用户的总时长。', '警告', {
    confirmButtonText: '确定删除',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(async () => {
    try {
      await deleteServiceRecord(recordId);
      ElMessage.success('删除成功');
      fetchRecords();
    } catch (error) {
      ElMessage.error(error.message || '删除失败');
    }
  });
};

onMounted(fetchRecords);
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.service-record-management {
  padding: 20px;
}
</style>