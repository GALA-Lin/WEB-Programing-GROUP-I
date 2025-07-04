<template>
  <div class="org-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>后台 - 组织管理</span>
          <el-button type="primary" @click="handleOpenDialog()" :icon="Plus">创建新组织</el-button>
        </div>
      </template>

      <el-table :data="tableData" v-loading="loading" style="width: 100%" @expand-change="handleExpandChange">
        <el-table-column type="expand">
          <template #default="props">
            <div class="member-management-panel">
              <h4>成员与审批管理 - {{ props.row.name }}</h4>
              <el-tabs v-model="activeTab">
                <el-tab-pane label="待审批" name="pending">
                  <el-table :data="pendingApprovals" size="small">
                    <el-table-column prop="realName" label="申请人" />
                    <el-table-column prop="studentId" label="学号" />
                    <el-table-column prop="applyDate" label="申请日期" />
                    <el-table-column label="操作">
                      <template #default="scope">
                        <el-button size="small" type="success" @click="handleApprove(scope.row.membershipId)">批准</el-button>
                        <el-button size="small" type="danger" @click="handleReject(scope.row.membershipId)">拒绝</el-button>
                      </template>
                    </el-table-column>
                  </el-table>
                </el-tab-pane>
              </el-tabs>
            </div>
          </template>
        </el-table-column>

        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="组织名称" />
        <el-table-column label="负责人" width="180">
          <template #default="scope">
            {{ scope.row.leaderName || '未指定' }} (ID: {{ scope.row.leaderId || 'N/A' }})
          </template>
        </el-table-column>
        <el-table-column prop="updatedAt" label="最后更新时间" width="200" />
        <el-table-column fixed="right" label="操作" width="150">
          <template #default="scope">
            <el-button link type="primary" size="small" @click="handleOpenDialog(scope.row)">编辑组织</el-button>
            <el-button link type="danger" size="small" @click="handleDelete(scope.row.id)">删除组织</el-button>
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
      <el-form ref="orgFormRef" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="组织名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入组织名称" />
        </el-form-item>
        <el-form-item label="组织简介" prop="description">
          <el-input type="textarea" v-model="form.description" placeholder="请输入组织简介" />
        </el-form-item>
        <el-form-item label="负责人用户ID" prop="leaderId">
          <el-input-number v-model="form.leaderId" :min="1" placeholder="请输入负责人ID" />
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
import { getAdminOrganizations, createOrganization, updateOrganization, deleteOrganization } from '@/services/organizationAdminApi.js';
// --- ↓↓↓ 引入前台API用于成员管理 ↓↓↓ ---
import { getPendingApprovals, approveApplication, rejectOrRemoveMember } from '@/services/organizationApi.js';
import { Plus, Link } from '@element-plus/icons-vue';

// --- 表格和分页数据 ---
const tableData = ref([]);
const loading = ref(true);
const total = ref(0);
const currentPage = ref(1);
const pageSize = ref(10);

// --- 编辑/创建对话框数据 ---
const dialogVisible = ref(false);
const dialogTitle = ref('');
const orgFormRef = ref(null);
const getInitialForm = () => ({ id: null, name: '', description: '', leaderId: null });
const form = reactive(getInitialForm());
const rules = { name: [{ required: true, message: '请输入组织名称', trigger: 'blur' }] };

// --- 成员管理数据 ---
const activeTab = ref('pending');
const pendingApprovals = ref([]);

// --- 主要方法 ---
const fetchOrganizations = async () => {
  loading.value = true;
  try {
    const data = await getAdminOrganizations(currentPage.value, pageSize.value);
    tableData.value = data.records;
    total.value = data.total;
  } catch (error) { ElMessage.error('获取组织列表失败'); }
  finally { loading.value = false; }
};

const handlePageChange = (page) => {
  currentPage.value = page;
  fetchOrganizations();
};

const resetForm = () => {
  Object.assign(form, getInitialForm());
  orgFormRef.value?.clearValidate();
};

const handleOpenDialog = (row) => {
  resetForm();
  dialogTitle.value = row?.id ? '编辑组织' : '创建新组织';
  if (row?.id) Object.assign(form, row);
  dialogVisible.value = true;
};

const handleSubmit = async () => {
  if (!orgFormRef.value) return;
  await orgFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const action = form.id ? updateOrganization(form.id, form) : createOrganization(form);
        await action;
        ElMessage.success(form.id ? '更新成功' : '创建成功');
        dialogVisible.value = false;
        await fetchOrganizations();
      } catch (error) { ElMessage.error(error.message || '操作失败'); }
    }
  });
};

const handleDelete = (id) => {
  ElMessageBox.confirm('确定要删除这个组织吗？此操作将无法撤销。', '警告', {
    confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning',
  }).then(async () => {
    try {
      await deleteOrganization(id);
      ElMessage.success('删除成功');
      await fetchOrganizations();
    } catch (error) { ElMessage.error(error.message || '删除失败'); }
  });
};

// --- ↓↓↓ 成员管理方法 ↓↓↓ ---
const handleExpandChange = async (row, expandedRows) => {
  const isOpening = expandedRows.some(r => r.id === row.id);
  if (isOpening) {
    try {
      pendingApprovals.value = await getPendingApprovals(row.id);
    } catch (error) {
      ElMessage.error(error.message || '获取待审批列表失败');
      pendingApprovals.value = [];
    }
  }
};

const handleApprove = async (membershipId) => {
  try {
    await approveApplication(membershipId);
    ElMessage.success('已批准');
    // 刷新待审批列表 (简单起见，可以重新触发展开事件，或直接从数组移除)
    pendingApprovals.value = pendingApprovals.value.filter(p => p.membershipId !== membershipId);
  } catch (error) { ElMessage.error(error.message || '操作失败'); }
};

const handleReject = async (membershipId) => {
  try {
    await rejectOrRemoveMember(membershipId);
    ElMessage.info('已拒绝');
    pendingApprovals.value = pendingApprovals.value.filter(p => p.membershipId !== membershipId);
  } catch (error) { ElMessage.error(error.message || '操作失败'); }
};

onMounted(fetchOrganizations);
</script>

<style scoped>
.card-header { display: flex; justify-content: space-between; align-items: center; }
.org-management { padding: 20px; }
.member-management-panel { padding: 15px; background-color: #f9f9f9; }
</style>