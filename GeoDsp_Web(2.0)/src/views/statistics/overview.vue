<!-- c:\Users\LENOVO\Desktop\GeoDsp_Web\src\views\statistics\overview.vue -->
<template>
  <PageLayout>
    <div class="statistics-overview">
      <h2>统计分析概览</h2>
      <p>对系统数据进行统计分析，包括分类统计和大屏展示。</p>

      <el-row :gutter="20" class="overview-cards">
        <el-col :span="8">
          <el-card
            class="overview-card"
            @click="$router.push('/statistics/classification')"
          >
            <el-icon size="48" color="#409EFF"><PieChart /></el-icon>
            <div class="card-content">
              <h3>分类统计</h3>
              <p>按类别进行数据统计</p>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card
            class="overview-card"
            @click="$router.push('/statistics/display')"
          >
            <el-icon size="48" color="#67C23A"><TrendCharts /></el-icon>
            <div class="card-content">
              <h3>大屏展示</h3>
              <p>数据可视化大屏展示</p>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="20" class="statistics-stats">
        <el-col :span="12">
          <el-card>
            <template #header>
              <div class="card-header">
                <span>快速操作</span>
              </div>
            </template>
            <div class="quick-actions">
              <el-button
                type="primary"
                @click="$router.push('/statistics/classification')"
              >
                <el-icon><PieChart /></el-icon> 分类统计
              </el-button>
              <el-button
                type="success"
                @click="$router.push('/statistics/display')"
              >
                <el-icon><TrendCharts /></el-icon> 大屏展示
              </el-button>
              <el-button type="warning" @click="exportReport">
                <el-icon><Download /></el-icon> 导出报告
              </el-button>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </PageLayout>
</template>

<script setup lang="ts">
import { PieChart, TrendCharts, Download } from "@element-plus/icons-vue";
import PageLayout from "@/components/PageLayout.vue";

const exportReport = () => {
  // 导出报告功能
  console.log("导出统计报告");
};
</script>

<style scoped>
/* 统计概览主容器 */
.statistics-overview {
  padding: 0;
}

/* 页面标题 */
.statistics-overview h2 {
  margin-bottom: 10px;
  color: #303133;
}

/* 页面描述文字 */
.statistics-overview p {
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

/* 统计统计区域 */
.statistics-stats {
  margin-top: 20px;
}

/* 卡片头部 */
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

/* 统计信息段落 */
.statistics-info p {
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
