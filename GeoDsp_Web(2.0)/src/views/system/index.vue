<template>
  <div class="system-management">
    <el-container>
      <el-main class="system-main">
        <router-view />
      </el-main>
    </el-container>
  </div>
</template>

<script setup lang="ts">
// import {
//   User,
//   Avatar,
//   Key,
//   Document,
//   Setting,
//   Collection,
//   Monitor,
//   Operation,
// } from "@element-plus/icons-vue";
</script>

<style scoped>
/* 系统管理主容器 */
.system-management {
  height: 100%;
  width: 100%;
}

/* 系统侧边栏 */
.system-aside {
  background-color: #f5f7fa;
  border-right: 1px solid #e4e7ed;
}

/* 系统菜单 */
.system-menu {
  border-right: none;
  height: 100%;
}

/* 系统主内容区域 */
.system-main {
  padding: 20px;
  background-color: #fff;
  width: 100%;
}

/* Element Plus 菜单项 */
:deep(.el-menu-item) {
  height: 50px;
  line-height: 50px;
}

/* Element Plus 激活的菜单项 */
:deep(.el-menu-item.is-active) {
  background-color: #ecf5ff;
  color: #409eff;
}

/* Element Plus 菜单项悬停状态 */
:deep(.el-menu-item:hover) {
  background-color: #f5f7fa;
}

/* Element Plus 图标 */
:deep(.el-icon) {
  margin-right: 8px;
}
</style>
