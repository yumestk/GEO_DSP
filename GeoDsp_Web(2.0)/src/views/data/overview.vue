<!-- c:\Users\LENOVO\Desktop\GeoDsp_Web\src\views\data\overview.vue -->
<template>
  <PageLayout>
    <div class="data-overview">
      <h2>数据概览</h2>
      <p>管理系统中的各类数据信息</p>

      <!-- 顶部导航卡片区
      <el-row :gutter="20" class="data-nav-cards">
        <el-col :span="6">
          <el-card class="nav-card">
            <h3>系统管理</h3>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="nav-card">
            <h3>设备管理</h3>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="nav-card">
            <h3>项目管理</h3>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="nav-card">
            <h3>数据管理</h3>
          </el-card>
        </el-col>
      </el-row> -->

      <!-- 数据表格区 -->
      <el-card class="data-table-card">
        <template #header>
          <div class="card-header">
            <span>数据信息</span>
          </div>
        </template>

        <!-- 数据分页导航 -->
        <div class="pagination-nav">
          <el-button-group>
            <el-button
              type="primary"
              :disabled="currentPage === 1"
              @click="currentPage--"
              >上一页</el-button
            >
            <el-button>页号 {{ currentPage }}</el-button>
            <el-button
              type="primary"
              :disabled="currentPage === totalPages"
              @click="currentPage++"
              >下一页</el-button
            >
          </el-button-group>
        </div>

        <!-- 数据表格 -->
        <el-table :data="dataList" style="width: 100%" stripe border>
          <el-table-column prop="number" label="编号" width="80" />
          <el-table-column prop="projectName" label="项目名" width="150" />
          <el-table-column prop="status" label="状态" width="120">
            <template #default="scope">
              <el-tag
                :type="scope.row.status === '进行中' ? 'success' : 'info'"
              >
                {{ scope.row.status }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="contractUnit" label="委托单位" width="150" />
          <el-table-column prop="startEndTime" label="起止时间" width="180" />
          <el-table-column prop="dataSize" label="数据量" width="100" />
          <el-table-column prop="institution" label="承担单位" width="150" />
          <el-table-column prop="remarks" label="备注" />
        </el-table>

        <!-- 分页信息 -->
        <div class="pagination-info">
          <el-pagination
            background
            layout="prev, pager, next, jumper"
            :total="totalItems"
            :current-page="currentPage"
            :page-size="pageSize"
            @current-change="currentPage = $event"
          />
        </div>
      </el-card>

      <!-- 图表展示区 -->
      <el-row :gutter="20" class="charts-section">
        <el-col :span="12">
          <el-card class="chart-card">
            <template #header>
              <div class="card-header">
                <span>衰减曲线</span>
              </div>
            </template>
            <div class="chart-placeholder">
              <el-icon size="64" color="#909399"><TrendCharts /></el-icon>
              <p>衰减曲线图表区域</p>
            </div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card class="chart-card">
            <template #header>
              <div class="card-header">
                <span>其他信息图</span>
              </div>
            </template>
            <div class="chart-placeholder">
              <el-icon size="64" color="#909399"><DataAnalysis /></el-icon>
              <p>其他信息图表区域</p>
            </div>
          </el-card>
        </el-col>

        <el-col :span="12">
          <el-card class="chart-card">
            <template #header>
              <div class="card-header">
                <span>电流监控图</span>
              </div>
            </template>
            <div class="chart-placeholder">
              <el-icon size="64" color="#909399"><Monitor /></el-icon>
              <p>电流监控图表区域</p>
            </div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card class="chart-card">
            <template #header>
              <div class="card-header">
                <span>关联时间监控图</span>
              </div>
            </template>
            <div class="chart-placeholder">
              <el-icon size="64" color="#909399"><Clock /></el-icon>
              <p>关联时间监控图表区域</p>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </PageLayout>
</template>

<script setup lang="ts">
import {
  TrendCharts,
  DataAnalysis,
  Monitor,
  Clock,
} from "@element-plus/icons-vue";
import { ref } from "vue";
import PageLayout from "@/components/PageLayout.vue";

const currentPage = ref(1);
const pageSize = ref(10);
const totalItems = ref(50);
const totalPages = ref(Math.ceil(totalItems.value / pageSize.value));

// 模拟数据列表
const dataList = ref([
  {
    number: "编号1",
    projectName: "项目1",
    status: "进行中",
    contractUnit: "中铁某局",
    startEndTime: "2023-01-01 至 2023-12-31",
    dataSize: "100GB",
    institution: "研究院",
    remarks: "",
  },
  {
    number: "编号2",
    projectName: "项目2",
    status: "已完成",
    contractUnit: "中铁某局",
    startEndTime: "2023-02-01 至 2023-11-30",
    dataSize: "80GB",
    institution: "研究院",
    remarks: "",
  },
  {
    number: "编号3",
    projectName: "项目3",
    status: "进行中",
    contractUnit: "中铁某局",
    startEndTime: "2023-03-01 至 2024-02-28",
    dataSize: "120GB",
    institution: "研究院",
    remarks: "",
  },
]);
</script>

<style scoped>
/* 数据概览主容器 */
.data-overview {
  padding: 0;
}

/* 数据概览标题 */
.data-overview h2 {
  margin-bottom: 10px;
  color: #303133;
}

/* 数据概览描述文字 */
.data-overview p {
  margin-bottom: 30px;
  color: #909399;
}

/* 数据导航卡片区域 */
.data-nav-cards {
  margin-bottom: 20px;
}

/* 导航卡片 */
.nav-card {
  text-align: center;
  padding: 20px;
  cursor: pointer;
  transition: all 0.3s;
  background-color: #f0f5ff;
  border-color: #d6e4ff;
}

/* 鼠标悬停的导航卡片 */
.nav-card:hover {
  background-color: #e6f0ff;
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.15);
}

/* 导航卡片标题 */
.nav-card h3 {
  margin: 0;
  color: #409eff;
}

/* 数据表格卡片 */
.data-table-card {
  margin-bottom: 20px;
}

/* 卡片头部区域 */
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: bold;
  color: #303133;
}

/* 分页导航区域 */
.pagination-nav {
  margin-bottom: 20px;
  display: flex;
  justify-content: flex-start;
}

/* 分页信息区域 */
.pagination-info {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

/* 图表展示区域 */
.charts-section {
  margin-top: 20px;
}

/* 图表卡片 */
.chart-card {
  margin-bottom: 20px;
}

/* 图表占位区域 */
.chart-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 300px;
  color: #909399;
  background-color: #fafafa;
  border: 1px dashed #dcdfe6;
  border-radius: 4px;
}

/* 图表占位区域文字 */
.chart-placeholder p {
  margin-top: 10px;
  margin-bottom: 0;
}
</style>
