<template>
  <div class="org-detail-view-container">
    <div v-if="loading" class="loading">加载中...</div>
    <div v-else-if="organization" class="org-detail-view">
      <el-card class="box-card">
        <template #header>
          <div class="card-header">
            <h1 class="org-title">{{ organization.name }}</h1>
            <div>
              <el-button v-if="!organization.isMember" @click="handleApply" type="success" :icon="Plus">申请加入</el-button>
              <el-button v-if="organization.isMember" @click="handleLeave" type="danger" :icon="Minus">退出组织</el-button>
            </div>
          </div>
        </template>
        <div class="org-info">
          <p><strong>负责人:</strong> {{ organization.leaderName }}</p>
          <p><strong>成员数:</strong> {{ organization.totalMembers }}</p>
          <p><strong>简介:</strong> {{ organization.description }}</p>
        </div>
      </el-card>

      <el-card class="box-card members-card">
        <template #header>
          <div class="card-header">
            <span>成员列表</span>
          </div>
        </template>
        <el-table :data="organization.members" stripe style="width: 100%">
          <el-table-column label="头像" width="80">
            <template #default="scope">
              <el-avatar :src="scope.row.avatarUrl || defaultAvatar" />
            </template>
          </el-table-column>
          <el-table-column prop="realName" label="姓名" />
          <el-table-column prop="memberRole" label="角色">
            <template #default="scope">
              <el-tag :type="scope.row.memberRole === 'officer' ? 'warning' : 'info'">
                {{ scope.row.memberRole === 'officer' ? '干部' : '成员' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="joinDate" label="加入日期" />
        </el-table>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { getOrganizationDetails, applyToOrganization, leaveOrganization } from '@/services/organizationApi.js';
import { ElMessage, ElMessageBox } from 'element-plus';
import { Plus, Minus } from '@element-plus/icons-vue';

const route = useRoute();
const organization = ref(null);
const loading = ref(true);
const defaultAvatar = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png';

const fetchDetails = async () => {
  try {
    loading.value = true;
    organization.value = await getOrganizationDetails(route.params.id);
  } catch (error) {
    ElMessage.error('加载组织详情失败');
  } finally {
    loading.value = false;
  }
};

const handleApply = async () => {
  try {
    await applyToOrganization(route.params.id);
    ElMessage.success('申请成功，请等待审批');
    // 这里可以不刷新页面，或者提示用户审批后状态会更新
  } catch (error) {
    ElMessage.error(error.message || '申请失败');
  }
};

const handleLeave = async () => {
  ElMessageBox.confirm('确定要退出该组织吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(async () => {
    try {
      await leaveOrganization(route.params.id);
      ElMessage.success('已退出组织');
      await fetchDetails(); // 刷新数据
    } catch (error) {
      ElMessage.error(error.message || '操作失败');
    }
  });
};

onMounted(fetchDetails);
</script>

<style scoped>
.org-detail-view-container { padding: 20px; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
.org-title { margin: 0; font-size: 24px; }
.org-info p { margin: 10px 0; }
.members-card { margin-top: 20px; }
</style>