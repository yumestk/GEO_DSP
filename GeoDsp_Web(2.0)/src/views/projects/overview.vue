<!-- c:\Users\LENOVO\Desktop\GeoDsp_Web\src\views\projects\overview.vue -->
<template>
  <PageLayout>
    <div class="project-overview">
      <h2>项目概览</h2>
      <p>管理项目信息、任务、进度、日志和统计报告。</p>

      <el-row :gutter="20" class="overview-cards">
        <el-col :span="8">
          <el-card class="overview-card" @click="$router.push('/project/info')">
            <el-icon size="48" color="#409EFF"><InfoFilled /></el-icon>
            <div class="card-content">
              <h3>项目信息</h3>
              <p>查看和管理项目基本信息</p>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card
            class="overview-card"
            @click="$router.push('/project/tasks')"
          >
            <el-icon size="48" color="#67C23A"><List /></el-icon>
            <div class="card-content">
              <h3>任务信息</h3>
              <p>查看和管理项目任务</p>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card
            class="overview-card"
            @click="$router.push('/project/progress')"
          >
            <el-icon size="48" color="#E6A23C"><TrendCharts /></el-icon>
            <div class="card-content">
              <h3>进度看板</h3>
              <p>查看项目进度情况</p>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="20" class="overview-cards">
        <el-col :span="8">
          <el-card class="overview-card" @click="$router.push('/project/logs')">
            <el-icon size="48" color="#F56C6C"><Document /></el-icon>
            <div class="card-content">
              <h3>项目日志</h3>
              <p>查看项目操作日志</p>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card
            class="overview-card"
            @click="$router.push('/project/reports')"
          >
            <el-icon size="48" color="#909399"><DataAnalysis /></el-icon>
            <div class="card-content">
              <h3>统计报告</h3>
              <p>查看项目统计报告</p>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="20" class="project-stats">
        <el-col :span="12">
          <el-card>
            <template #header>
              <div class="card-header">
                <span>项目统计</span>
              </div>
            </template>
            <div class="project-info">
              <p><strong>项目总数:</strong> 24个</p>
              <p><strong>进行中:</strong> 15个</p>
              <p><strong>已完成:</strong> 8个</p>
              <p><strong>已延期:</strong> 1个</p>
            </div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card>
            <template #header>
              <div class="card-header">
                <span>快速操作</span>
              </div>
            </template>
            <div class="quick-actions">
              <el-button type="primary" @click="$router.push('/project/info')">
                <el-icon><InfoFilled /></el-icon> 项目信息
              </el-button>
              <el-button type="success" @click="$router.push('/project/tasks')">
                <el-icon><List /></el-icon> 任务信息
              </el-button>
              <el-button
                type="warning"
                @click="$router.push('/project/progress')"
              >
                <el-icon><TrendCharts /></el-icon> 进度看板
              </el-button>
              <el-button type="info" @click="$router.push('/project/reports')">
                <el-icon><DataAnalysis /></el-icon> 统计报告
              </el-button>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </PageLayout>
</template>

<script setup lang="ts">
import {
  InfoFilled,
  List,
  TrendCharts,
  Document,
  DataAnalysis,
} from "@element-plus/icons-vue";
import PageLayout from "@/components/PageLayout.vue";
</script>

<style scoped>
/* 项目概览主容器 */
.project-overview {
  padding: 0;
}

/* 页面标题 */
.project-overview h2 {
  margin-bottom: 10px;
  color: #303133;
}

/* 页面描述文字 */
.project-overview p {
  margin-bottom: 30px;
  color: #909399;
}

/* 概览卡片容器 */
.overview-cards {
  margin-bottom: 20px;
}

/* 概览卡片 */
.overview-card {
  display: flex;
  align-items: center;
  padding: 20px;
  transition: all 0.3s;
  border: 1px solid #e4e7ed;
  cursor: pointer;
}

/* 概览卡片悬停状态 */
.overview-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  border-color: #409eff;
}

/* 卡片内容 */
.card-content {
  margin-left: 15px;
}

/* 卡片标题 */
.card-content h3 {
  margin: 0 0 5px;
  color: #303133;
  font-size: 16px;
}

/* 卡片描述文字 */
.card-content p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

/* 项目统计区域 */
.project-stats {
  margin-top: 20px;
}

/* 卡片头部 */
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

/* 项目信息段落 */
.project-info p {
  margin-bottom: 10px;
  line-height: 1.5;
}

/* 快速操作区域 */
.quick-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

/* 快速操作按钮 */
.quick-actions .el-button {
  margin: 0;
}

/* Element Plus 图标 */
:deep(.el-icon) {
  margin-right: 5px;
}
</style>
