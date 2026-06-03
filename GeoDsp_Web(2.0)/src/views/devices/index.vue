<template>
  <div class="device-management">
    <el-container>
      <!-- <el-aside width="200px" class="device-aside">
        <el-menu
          :default-active="$route.path"
          class="device-menu"
          router
        >
          <el-menu-item index="/device">
            <el-icon><Monitor /></el-icon>
            <span>设备概览</span>
          </el-menu-item>
          <el-menu-item index="/device/register">
            <el-icon><Plus /></el-icon>
            <span>设备登记</span>
          </el-menu-item>
          <el-menu-item index="/device/monitor">
            <el-icon><View /></el-icon>
            <span>设备监控</span>
          </el-menu-item>
          <el-menu-item index="/device/maintenance">
            <el-icon><Tools /></el-icon>
            <span>设备维护</span>
          </el-menu-item>
          <el-menu-item index="/device/analysis">
            <el-icon><DataAnalysis /></el-icon>
            <span>数据分析</span>
          </el-menu-item>
        </el-menu>
      </el-aside> -->
      <el-main class="device-main">
        <router-view />
      </el-main>
    </el-container>
  </div>
</template>

<script setup lang="ts">
// import {
//   Monitor,
//   Plus,
//   View,
//   Tools,
//   DataAnalysis,
// } from "@element-plus/icons-vue";
</script>

<style scoped>
.device-management {
  height: 100%;
}

.device-aside {
  background-color: #f5f7fa;
  border-right: 1px solid #e4e7ed;
}

.device-menu {
  border-right: none;
  height: 100%;
}

.device-main {
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
