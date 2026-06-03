<!-- c:\Users\LENOVO\Desktop\GeoDsp_Web\src\views\statistics\index.vue -->
<template>
  <div class="statistics-management">
    <el-container>
      <!-- <el-aside width="200px" class="statistics-aside">
        <el-menu :default-active="$route.path" class="statistics-menu" router>
          <el-menu-item index="/statistics">
            <el-icon><TrendCharts /></el-icon>
            <span>统计概览</span>
          </el-menu-item>
          <el-menu-item index="/statistics/classification">
            <el-icon><PieChart /></el-icon>
            <span>分类统计</span>
          </el-menu-item>
          <el-menu-item index="/statistics/display">
            <el-icon><TrendCharts /></el-icon>
            <span>大屏展示</span>
          </el-menu-item> -->
      <!-- </el-menu> -->
      <!-- </el-aside>  -->
      <el-main class="statistics-main">
        <router-view />
      </el-main>
    </el-container>
  </div>
</template>

<script setup lang="ts">
// import { TrendCharts, PieChart } from "@element-plus/icons-vue";
</script>

<style scoped>
.statistics-management {
  height: 100%;
}

.statistics-aside {
  background-color: #f5f7fa;
  border-right: 1px solid #e4e7ed;
}

.statistics-menu {
  border-right: none;
  height: 100%;
}

.statistics-main {
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
