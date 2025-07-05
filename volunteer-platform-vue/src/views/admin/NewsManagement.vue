<template>
  <div class="news-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>后台 - 新闻管理</span>
          <el-button type="primary" @click="handleOpenDialog()">
            <el-icon><Plus /></el-icon>
            <span>发布新新闻</span>
          </el-button>
        </div>
      </template>

      <el-table :data="tableData" v-loading="loading" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="新闻标题" width="300" />
        <el-table-column prop="summary" label="摘要" show-overflow-tooltip />
        <el-table-column prop="publishedAt" label="发布时间" width="180" />
        <el-table-column fixed="right" label="操作" width="150">
          <template #default="scope">
            <el-button link type="primary" size="small" @click="handleOpenDialog(scope.row)">编辑</el-button>
            <el-button link type="danger" size="small" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
          background
          :layout="'total, sizes, prev, pager, next'"
          :total="total"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pageSize"
          :current-page="currentPage"
          @size-change="handleSizeChange"
          @current-change="handlePageChange"
          style="margin-top: 20px; justify-content: flex-end;"
      />
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="80%" @close="resetForm" top="5vh">
      <el-form ref="newsFormRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="新闻标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入新闻标题" />
        </el-form-item>
        <el-form-item label="新闻摘要" prop="summary">
          <el-input type="textarea" :rows="3" v-model="form.summary" placeholder="请输入新闻摘要" />
        </el-form-item>
        <el-form-item label="新闻正文" prop="content">
          <v-md-editor v-model="form.content" height="400px"></v-md-editor>
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
import {ElMessage, ElMessageBox, ElPagination} from 'element-plus';
import { getNewsPage, createNews, updateNews, deleteNews } from '@/services/newsAdminApi.js';
import { Plus } from '@element-plus/icons-vue';

// 【核心修改】引入 v-md-editor 及其样式
import VMdEditor from '@kangc/v-md-editor';
import '@kangc/v-md-editor/lib/style/base-editor.css';
import githubTheme from '@kangc/v-md-editor/lib/theme/github.js';
import '@kangc/v-md-editor/lib/theme/style/github.css';
import hljs from 'highlight.js';

VMdEditor.use(githubTheme, {
  Hljs: hljs,
});

const tableData = ref([]);
const loading = ref(true);
const total = ref(0);
const currentPage = ref(1);
const pageSize = ref(10);

const dialogVisible = ref(false);
const dialogTitle = ref('');
const newsFormRef = ref(null);

const handleSizeChange = (newSize) => {
  pageSize.value = newSize;
  currentPage.value = 1;
  fetchNews();
};

const getInitialForm = () => ({
  id: null,
  title: '',
  summary: '',
  content: '',
});

const form = reactive(getInitialForm());

const rules = reactive({
  title: [{ required: true, message: '请输入新闻标题', trigger: 'blur' }],
  summary: [{ required: true, message: '请输入新闻摘要', trigger: 'blur' }],
  content: [{ required: true, message: '请输入新闻正文', trigger: 'blur' }],
});

const fetchNews = async () => {
  loading.value = true;
  try {
    const data = await getNewsPage(currentPage.value, pageSize.value);
    tableData.value = data.records;
    total.value = data.total;
  } catch (error) {
    ElMessage.error(error.message || '获取新闻列表失败');
  } finally {
    loading.value = false;
  }
};

const handlePageChange = (page) => {
  currentPage.value = page;
  fetchNews();
};

const resetForm = () => {
  Object.assign(form, getInitialForm());
  if (newsFormRef.value) {
    newsFormRef.value.clearValidate();
  }
};

const handleOpenDialog = (row) => {
  resetForm();
  if (row && row.id) {
    dialogTitle.value = '编辑新闻';
    // 模拟从API获取完整内容
    const fullContentRow = { ...row, content: row.content || '' };
    Object.assign(form, fullContentRow);
  } else {
    dialogTitle.value = '发布新新闻';
  }
  dialogVisible.value = true;
};

const handleSubmit = async () => {
  if (!newsFormRef.value) return;
  await newsFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (form.id) {
          await updateNews(form.id, form);
          ElMessage.success('新闻更新成功');
        } else {
          await createNews(form);
          ElMessage.success('新闻发布成功');
        }
        dialogVisible.value = false;
        await fetchNews();
      } catch (error) {
        ElMessage.error(error.message || '操作失败');
      }
    }
  });
};

const handleDelete = (id) => {
  ElMessageBox.confirm('确定要删除这篇新闻吗？此操作无法撤销。', '警告', {
    confirmButtonText: '确定删除',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(async () => {
    try {
      await deleteNews(id);
      ElMessage.success('删除成功');
      await fetchNews();
    } catch (error) {
      ElMessage.error(error.message || '删除失败');
    }
  }).catch(() => {});
};

onMounted(() => {
  fetchNews();
});
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.news-management {
  padding: 20px;
}
</style>