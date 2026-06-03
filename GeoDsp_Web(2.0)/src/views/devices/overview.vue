<template>
  <PageLayout>
    <div class="device-overview">
      <!-- <h2>设备概览</h2>
    <p>管理系统中的设备信息，包括设备登记、监控、维护和数据分析。</p> -->

      <!-- <el-row :gutter="20" class="overview-cards">
      <el-col :span="8">
        <el-card class="overview-card" @click="$router.push('/device/register')">
          <el-icon size="48" color="#409EFF"><Plus /></el-icon>
          <div class="card-content">
            <h3>设备登记</h3>
            <p>添加和登记新设备</p>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="overview-card" @click="$router.push('/device/monitor')">
          <el-icon size="48" color="#67C23A"><View /></el-icon>
          <div class="card-content">
            <h3>设备监控</h3>
            <p>实时监控设备状态</p>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="overview-card" @click="$router.push('/device/maintenance')">
          <el-icon size="48" color="#E6A23C"><Tools /></el-icon>
          <div class="card-content">
            <h3>设备维护</h3>
            <p>管理设备维护记录</p>
          </div>
        </el-card>
      </el-col>
    </el-row> -->

      <!-- <el-row :gutter="20" class="overview-cards">
      <el-col :span="8">
        <el-card
          class="overview-card"
          @click="$router.push('/device/analysis')"
        >
          <el-icon size="48" color="#909399"><DataAnalysis /></el-icon>
          <div class="card-content">
            <h3>数据分析</h3>
            <p>分析设备运行数据</p>
          </div>
        </el-card>
      </el-col>
    </el-row> -->

      <el-row :gutter="20" class="device-stats">
        <el-col :span="24">
          <el-card>
            <template #header>
              <div class="card-header">
                <span>设备统计</span>
              </div>
            </template>
            <div class="device-info">
              <p><strong>设备总数:</strong> 156台</p>
              <p><strong>在线设备:</strong> 142台</p>
              <p><strong>离线设备:</strong> 14台</p>
              <p><strong>故障设备:</strong> 3台</p>
            </div>
          </el-card>
        </el-col>
        <el-col :span="24">
          <el-card>
            <template #header>
              <div class="card-header">
                <span>快速操作</span>
              </div>
            </template>
            <div class="quick-actions">
              <el-button
                type="primary"
                @click="$router.push('/device/register')"
              >
                <el-icon><Plus /></el-icon> 设备登记
              </el-button>
              <el-button
                type="success"
                @click="$router.push('/device/monitor')"
              >
                <el-icon><View /></el-icon> 设备监控
              </el-button>
              <el-button
                type="warning"
                @click="$router.push('/device/maintenance')"
              >
                <el-icon><Tools /></el-icon> 设备维护
              </el-button>
              <el-button type="info" @click="$router.push('/device/analysis')">
                <el-icon><DataAnalysis /></el-icon> 数据分析
              </el-button>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </PageLayout>
</template>

<script setup lang="ts">
import { Plus, View, Tools, DataAnalysis } from "@element-plus/icons-vue";
import PageLayout from "@/components/PageLayout.vue";
</script>

<style scoped>
.device-overview {
  padding: 0;
  min-height: 81vh; /* 高度占满视口 */
  display: flex;
  flex-direction: column;
}

.device-overview h2 {
  margin-bottom: 10px;
  color: #303133;
}

.device-overview p {
  margin-bottom: 30px;
  color: #909399;
}

.overview-cards {
  margin-bottom: 20px;
}

.overview-card {
  display: flex;
  align-items: center;
  padding: 20px;
  transition: all 0.3s;
  border: 1px solid #e4e7ed;
  cursor: pointer;
}

.overview-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  border-color: #409eff;
}

.card-content {
  margin-left: 15px;
}

.card-content h3 {
  margin: 0 0 5px;
  color: #303133;
  font-size: 16px;
}

.card-content p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.device-stats {
  margin-top: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.device-info p {
  margin-bottom: 10px;
  line-height: 1.5;
}

.quick-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.quick-actions .el-button {
  margin: 0;
}

:deep(.el-icon) {
  margin-right: 5px;
}
</style>
