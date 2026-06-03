<!-- c:\Users\LENOVO\Desktop\GeoDsp_Web\src\views\data\index.vue -->
<template>
  <div class="data-management">
    <el-container>
      <!-- <el-aside width="200px" class="data-aside">
        <el-menu :default-active="$route.path" class="data-menu" router>
          <el-menu-item index="/data">
            <el-icon><DataAnalysis /></el-icon>
            <span>数据概览</span>
          </el-menu-item>
          <el-menu-item index="/data/tasks">
            <el-icon><List /></el-icon>
            <span>任务列表</span>
          </el-menu-item>
          <el-menu-item index="/data/quality">
            <el-icon><Select /></el-icon>
            <span>数据质量</span>
          </el-menu-item>
        </el-menu>
      </el-aside> -->
      <el-main class="data-main">
        <router-view />
      </el-main>
    </el-container>
  </div>
</template>

<script setup lang="ts">
// import { DataAnalysis, List, Select } from "@element-plus/icons-vue";
</script>

<style scoped>
/* 数据管理主容器 */
.data-management {
  height: 100%;
}

/* 数据侧边栏 */
.data-aside {
  background-color: #f5f7fa;
  border-right: 1px solid #e4e7ed;
  height: 100%;
}

/* 数据菜单 */
.data-menu {
  border-right: none;
  height: 100%;
}

/* 数据主内容区域 */
.data-main {
  padding: 20px;
  background-color: #fff;
}

/* Element Plus 菜单项 */
:deep(.el-menu-item) {
  height: 50px;
  line-height: 50px;
}

/* 激活状态的菜单项 */
:deep(.el-menu-item.is-active) {
  background-color: #ecf5ff;
  color: #409eff;
}

/* 鼠标悬停的菜单项 */
:deep(.el-menu-item:hover) {
  background-color: #f5f7fa;
}

/* Element Plus 图标 */
:deep(.el-icon) {
  margin-right: 8px;
}
</style>
