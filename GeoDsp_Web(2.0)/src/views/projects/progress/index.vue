<!-- c:\Users\LENOVO\Desktop\GeoDsp_Web\src\views\projects\progress\index.vue -->
<template>
  <PageLayout>
    <div class="project-progress">
      <h2>进度看板</h2>
      <p>查看和管理项目进度情况。</p>

      <el-row :gutter="20">
        <el-col :span="8">
          <el-card>
            <template #header>
              <div class="card-header">
                <span>项目进度概览</span>
              </div>
            </template>
            <div class="progress-overview">
              <div class="progress-item">
                <span>总体进度</span>
                <el-progress :percentage="overallProgress" />
              </div>
              <div class="progress-item">
                <span>已完成任务</span>
                <el-progress :percentage="completedTasks" :show-text="false" />
                <span class="progress-text">{{ completedTasks }}%</span>
              </div>
              <div class="progress-item">
                <span>进行中任务</span>
                <el-progress
                  :percentage="inProgressTasks"
                  :show-text="false"
                  status="warning"
                />
                <span class="progress-text">{{ inProgressTasks }}%</span>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="16">
          <el-card>
            <template #header>
              <div class="card-header">
                <span>项目进度详情</span>
                <el-button type="primary" size="small">刷新</el-button>
              </div>
            </template>
            <el-table :data="projectProgress" style="width: 100%">
              <el-table-column prop="name" label="项目名称" />
              <el-table-column prop="manager" label="负责人" />
              <el-table-column prop="startDate" label="开始日期" width="120" />
              <el-table-column prop="endDate" label="结束日期" width="120" />
              <el-table-column prop="progress" label="进度" width="200">
                <template #default="scope">
                  <el-progress
                    :percentage="scope.row.progress"
                    :show-text="false"
                  />
                  <span>{{ scope.row.progress }}%</span>
                </template>
              </el-table-column>
              <el-table-column prop="status" label="状态" width="100">
                <template #default="scope">
                  <el-tag :type="getStatusType(scope.row.status)">
                    {{ scope.row.status }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="150">
                <template #default="scope">
                  <el-button size="small" type="primary">查看</el-button>
                  <el-button size="small" type="success">编辑</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </PageLayout>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import PageLayout from "@/components/PageLayout.vue";

const overallProgress = ref(65);
const completedTasks = ref(40);
const inProgressTasks = ref(35);

const projectProgress = ref([
  {
    name: "地理数据采集项目",
    manager: "张三",
    startDate: "2023-01-01",
    endDate: "2023-06-30",
    progress: 75,
    status: "进行中",
  },
  {
    name: "地图绘制项目",
    manager: "李四",
    startDate: "2023-02-15",
    endDate: "2023-08-15",
    progress: 50,
    status: "进行中",
  },
  {
    name: "数据分析项目",
    manager: "王五",
    startDate: "2023-03-01",
    endDate: "2023-09-01",
    progress: 90,
    status: "即将完成",
  },
  {
    name: "系统优化项目",
    manager: "赵六",
    startDate: "2023-04-01",
    endDate: "2023-10-01",
    progress: 30,
    status: "进行中",
  },
]);

const getStatusType = (status: string) => {
  switch (status) {
    case "已完成":
      return "success";
    case "进行中":
      return "primary";
    case "即将完成":
      return "warning";
    case "已延期":
      return "danger";
    default:
      return "info";
  }
};

onMounted(() => {});
</script>

<style scoped>
/* 项目进度主容器 */
.project-progress {
  padding: 20px;
}

/* 页面标题 */
.project-progress h2 {
  margin-bottom: 10px;
  color: #303133;
}

/* 页面描述文字 */
.project-progress p {
  margin-bottom: 30px;
  color: #909399;
}

/* 卡片头部 */
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

/* 进度概览区域 */
.progress-overview {
  padding: 10px 0;
}

/* 进度项 */
.progress-item {
  margin-bottom: 20px;
}

/* 进度项文字 */
.progress-item span {
  display: block;
  margin-bottom: 8px;
  font-weight: bold;
}

/* 进度文字 */
.progress-text {
  margin-left: 10px;
  font-weight: bold;
}
</style>
