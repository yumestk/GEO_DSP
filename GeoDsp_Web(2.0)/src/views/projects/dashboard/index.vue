<template>
  <PageLayout>
    <div class="project-dashboard">
      <div class="page-header">
        <h2>进度看板</h2>
      </div>
      <div class="page-content">
        <p>项目进度看板页面内容</p>
        <div class="dashboard-container">
          <el-row :gutter="20">
            <el-col :span="6">
              <div class="dashboard-card">
                <div class="card-title">项目总数</div>
                <div class="card-value">12</div>
                <div class="card-footer">+2 本月新增</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="dashboard-card">
                <div class="card-title">进行中项目</div>
                <div class="card-value">8</div>
                <div class="card-footer">3个项目延期</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="dashboard-card">
                <div class="card-title">任务总数</div>
                <div class="card-value">126</div>
                <div class="card-footer">+15 本周新增</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="dashboard-card">
                <div class="card-title">完成率</div>
                <div class="card-value">78%</div>
                <div class="card-footer">平均按时完成率</div>
              </div>
            </el-col>
          </el-row>

          <el-row :gutter="20" style="margin-top: 20px">
            <el-col :span="16">
              <div class="chart-box">
                <h3>项目进度趋势</h3>
                <div class="chart-placeholder">项目进度趋势图表</div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="chart-box">
                <h3>项目状态分布</h3>
                <div class="chart-placeholder">项目状态分布图表</div>
              </div>
            </el-col>
          </el-row>

          <el-row :gutter="20" style="margin-top: 20px">
            <el-col :span="24">
              <div class="recent-tasks">
                <h3>近期任务</h3>
                <el-table :data="recentTasks" style="width: 100%">
                  <el-table-column prop="name" label="任务名称" />
                  <el-table-column prop="project" label="项目" width="150" />
                  <el-table-column prop="assignee" label="负责人" width="100" />
                  <el-table-column
                    prop="deadline"
                    label="截止日期"
                    width="120"
                  />
                  <el-table-column prop="status" label="状态" width="100">
                    <template #default="scope">
                      <el-tag :type="getStatusType(scope.row.status)">
                        {{ scope.row.status }}
                      </el-tag>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </el-col>
          </el-row>
        </div>
      </div>
    </div>
  </PageLayout>
</template>

<script setup lang="ts">
import { ref } from "vue";
import PageLayout from "@/components/PageLayout.vue";
const recentTasks = ref([
  {
    id: 1,
    name: "数据库设计优化",
    project: "智慧城市系统",
    assignee: "张三",
    deadline: "2025-01-10",
    status: "进行中",
  },
  {
    id: 2,
    name: "API接口开发",
    project: "环境监测平台",
    assignee: "李四",
    deadline: "2025-01-12",
    status: "进行中",
  },
  {
    id: 3,
    name: "前端界面优化",
    project: "数据分析系统",
    assignee: "王五",
    deadline: "2025-01-08",
    status: "即将到期",
  },
  {
    id: 4,
    name: "系统集成测试",
    project: "物联网平台",
    assignee: "赵六",
    deadline: "2025-01-15",
    status: "未开始",
  },
  {
    id: 5,
    name: "性能优化",
    project: "智慧城市系统",
    assignee: "孙七",
    deadline: "2025-01-20",
    status: "未开始",
  },
]);

const getStatusType = (status: string) => {
  switch (status) {
    case "进行中":
      return "primary";
    case "已完成":
      return "success";
    case "未开始":
      return "info";
    case "即将到期":
      return "warning";
    case "已逾期":
      return "danger";
    default:
      return "info";
  }
};
</script>

<style scoped>
/* 项目看板主容器 */
.project-dashboard {
  padding: 20px;
}

/* 页面头部 */
.page-header {
  margin-bottom: 20px;
}

/* 页面内容区域 */
.page-content {
  background: #fff;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

/* 看板容器 */
.dashboard-container {
  width: 100%;
}

/* 看板卡片 */
.dashboard-card {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  text-align: center;
  height: 120px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

/* 卡片标题 */
.card-title {
  font-size: 14px;
  color: #606266;
  margin-bottom: 8px;
}

/* 卡片数值 */
.card-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 8px;
}

/* 卡片底部 */
.card-footer {
  font-size: 12px;
  color: #909399;
}

/* 图表盒子 */
.chart-box {
  background: #f8f9fa;
  padding: 20px;
  border-radius: 4px;
  height: 300px;
  border: 1px solid #ebeef5;
}

/* 图表占位区域 */
.chart-placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
  height: calc(100% - 30px);
  background: white;
  border-radius: 4px;
  border: 1px dashed #dcdfe6;
}

/* 近期任务区域 */
.recent-tasks {
  background: #f8f9fa;
  padding: 20px;
  border-radius: 4px;
  border: 1px solid #ebeef5;
}
</style>
