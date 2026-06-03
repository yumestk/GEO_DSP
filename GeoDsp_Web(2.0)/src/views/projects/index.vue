<!-- c:\Users\LENOVO\Desktop\GeoDsp_Web\src\views\projects\index.vue -->
<template>
  <div class="project-management">
    <el-container>
      <!-- <el-aside width="200px" class="project-aside">
        <el-menu
          :default-active="$route.path"
          class="project-menu"
          router
        >
          <el-menu-item index="/project">
            <el-icon><Document /></el-icon>
            <span>项目概览</span>
          </el-menu-item>
          <el-menu-item index="/project/info">
            <el-icon><InfoFilled /></el-icon>
            <span>项目信息</span>
          </el-menu-item>
          <el-menu-item index="/project/tasks">
            <el-icon><List /></el-icon>
            <span>任务信息</span>
          </el-menu-item>
          <el-menu-item index="/project/progress">
            <el-icon><TrendCharts /></el-icon>
            <span>进度看板</span>
          </el-menu-item>
          <el-menu-item index="/project/logs">
            <el-icon><Document /></el-icon>
            <span>项目日志</span>
          </el-menu-item>
          <el-menu-item index="/project/reports">
            <el-icon><DataAnalysis /></el-icon>
            <span>统计报告</span>
          </el-menu-item>
        </el-menu>
      </el-aside> -->
      <el-main class="project-main">
        <router-view />
      </el-main>
    </el-container>
  </div>
</template>

<script setup lang="ts">
// import {
//   Document,
//   InfoFilled,
//   List,
//   TrendCharts,
//   DataAnalysis,
// } from "@element-plus/icons-vue";
</script>

<style scoped>
.project-management {
  height: 100%;
}

.project-aside {
  background-color: #f5f7fa;
  border-right: 1px solid #e4e7ed;
}

.project-menu {
  border-right: none;
  height: 100%;
}

.project-main {
  padding: 20px;
  background-color: #fff;
}

:deep(.el-menu-item) {
  height: 50px;
  line-height: 50px;
}

:deep(.el-menu-item.is-active) {
  background-color: #ecf5ff;
  color: #409eff;
}

:deep(.el-menu-item:hover) {
  background-color: #f5f7fa;
}

:deep(.el-icon) {
  margin-right: 8px;
}
</style>
