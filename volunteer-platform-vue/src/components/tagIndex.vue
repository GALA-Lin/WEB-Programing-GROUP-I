<template>
  <div id="tags-view-container" class="tags-view-container">
    <router-link
        v-for="tag in visitedViews"
        :key="tag.path"
        :to="{ path: tag.path, query: tag.query, fullPath: tag.fullPath }"
        class="tags-view-item"
        :class="isActive(tag) ? 'active' : ''"
        @click.middle="!isAffix(tag) ? closeSelectedTag(tag) : ''"
        @contextmenu.prevent="openMenu(tag, $event)"
    >
      {{ tag.title }}
      <span v-if="!isAffix(tag)" class="el-icon-close" @click.prevent.stop="closeSelectedTag(tag)" >
        <el-icon><Close /></el-icon>
      </span>
    </router-link>
  </div>
</template>

<script setup>
import { computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useTagsViewStore } from '@/stores/tagsView.js';
import { Close } from '@element-plus/icons-vue';

const route = useRoute();
const router = useRouter();
const tagsViewStore = useTagsViewStore();

const visitedViews = computed(() => tagsViewStore.visitedViews);

const isActive = (tag) => {
  return tag.path === route.path;
};

const isAffix = (tag) => {
  return tag.meta && tag.meta.affix;
};

const closeSelectedTag = (view) => {
  tagsViewStore.delView(view).then((visitedViews) => {
    if (isActive(view)) {
      toLastView(visitedViews, view);
    }
  });
};

const toLastView = (visitedViews, view) => {
  const latestView = visitedViews.slice(-1)[0];
  if (latestView) {
    router.push(latestView.fullPath);
  } else {
    router.push('/admin/dashboard');
  }
};
</script>

<style scoped>
.tags-view-container {
  height: 34px;
  width: 100%;
  background: #fff;
  border-bottom: 1px solid #d8dce5;
  box-shadow: 0 1px 3px 0 rgba(0, 0, 0, .12), 0 0 3px 0 rgba(0, 0, 0, .04);
  padding: 0 10px;
}
.tags-view-item {
  display: inline-flex;
  align-items: center;
  position: relative;
  cursor: pointer;
  height: 26px;
  line-height: 26px;
  border: 1px solid #d8dce5;
  color: #495060;
  background: #fff;
  padding: 0 8px;
  font-size: 12px;
  margin-left: 5px;
  margin-top: 4px;
  text-decoration: none;
}
.tags-view-item.active {
  background-color: #409eff;
  color: #fff;
  border-color: #409eff;
}
.tags-view-item .el-icon-close {
  margin-left: 5px;
  width: 16px;
  height: 16px;
  border-radius: 50%;
  text-align: center;
  transition: all .3s cubic-bezier(.645, .045, .355, 1);
}
.tags-view-item .el-icon-close:hover {
  background-color: #b4bccc;
  color: #fff;
}
</style>