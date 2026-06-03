<!-- c:\Users\LENOVO\Desktop\GeoDsp_Web\src\views\system\overview.vue -->
<template>
  <PageLayout>
    <div class="system-overview">
      <h2>系统管理</h2>
      <p>管理系统配置和用户权限。</p>

      <el-row :gutter="20" class="overview-cards">
        <el-col :span="8">
          <el-card class="overview-card" @click="router.push('/system/users')">
            <el-icon size="48" color="#409EFF"><User /></el-icon>
            <div class="card-content">
              <h3>用户管理</h3>
              <p>管理系统用户信息</p>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card class="overview-card" @click="router.push('/system/roles')">
            <el-icon size="48" color="#67C23A"><Avatar /></el-icon>
            <div class="card-content">
              <h3>角色管理</h3>
              <p>管理系统角色权限</p>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card
            class="overview-card"
            @click="router.push('/system/permission')"
          >
            <el-icon size="48" color="#E6A23C"><Key /></el-icon>
            <div class="card-content">
              <h3>权限管理</h3>
              <p>管理系统权限配置</p>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="20" class="overview-cards">
        <el-col :span="8">
          <el-card
            class="overview-card"
            @click="router.push('/system/enterprise')"
          >
            <el-icon size="48" color="#606266"><OfficeBuilding /></el-icon>
            <div class="card-content">
              <h3>企业管理</h3>
              <p>管理系统企业信息</p>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card
            class="overview-card"
            @click="router.push('/system/dictionary')"
          >
            <el-icon size="48" color="#606266"><Collection /></el-icon>
            <div class="card-content">
              <h3>字典管理</h3>
              <p>管理系统数据字典</p>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card class="overview-card" @click="router.push('/system/logs')">
            <el-icon size="48" color="#606266"><Document /></el-icon>
            <div class="card-content">
              <h3>日志管理</h3>
              <p>管理系统操作日志</p>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </PageLayout>
</template>

<script setup lang="ts">
import {
  User,
  Avatar,
  Key,
  OfficeBuilding,
  Collection,
  Document,
} from "@element-plus/icons-vue";
import { useRouter } from "vue-router";
// 导入 PageLayout 组件
import PageLayout from "@/components/PageLayout.vue";

// 获取路由实例
const router = useRouter();
</script>

<style scoped>
/* 系统概览主容器 */
.system-overview {
  background-color: #fff;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

/* 页面标题 */
.system-overview h2 {
  margin-bottom: 10px;
  color: #303133;
}

/* 页面描述文字 */
.system-overview p {
  margin-bottom: 20px;
  color: #909399;
}

/* 概览卡片容器 */
.overview-cards {
  margin-bottom: 20px;
}

/* 概览卡片 */
.overview-card {
  text-align: center;
  cursor: pointer;
  transition: all 0.3s;
  margin-bottom: 20px;
}

/* 概览卡片悬停状态 */
.overview-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 16px 0 rgba(0, 0, 0, 0.1);
}

/* 卡片内容 */
.card-content {
  margin-top: 15px;
}

/* 卡片标题 */
.card-content h3 {
  margin-bottom: 5px;
  font-size: 18px;
}

/* 卡片描述文字 */
.card-content p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}
</style>
