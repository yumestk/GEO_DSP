<!-- c:\Users\LENOVO\Desktop\GeoDsp_Web\src\views\projects\reports\index.vue -->
<template>
  <PageLayout>
    <div class="project-reports">
      <h2>统计报告</h2>
      <p>查看项目统计报告和数据分析。</p>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-card>
            <template #header>
              <div class="card-header">
                <span>项目状态分布</span>
              </div>
            </template>
            <div class="chart-container" ref="statusChartRef"></div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card>
            <template #header>
              <div class="card-header">
                <span>项目进度分布</span>
              </div>
            </template>
            <div class="chart-container" ref="progressChartRef"></div>
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="20" style="margin-top: 20px">
        <el-col :span="24">
          <el-card>
            <template #header>
              <div class="card-header">
                <span>项目月度趋势</span>
                <div class="header-actions">
                  <el-radio-group
                    v-model="timeRange"
                    @change="handleTimeRangeChange"
                  >
                    <el-radio-button label="3个月">3个月</el-radio-button>
                    <el-radio-button label="6个月">6个月</el-radio-button>
                    <el-radio-button label="1年">1年</el-radio-button>
                  </el-radio-group>
                </div>
              </div>
            </template>
            <div class="chart-container-large" ref="trendChartRef"></div>
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="20" style="margin-top: 20px">
        <el-col :span="24">
          <el-card>
            <template #header>
              <div class="card-header">
                <span>项目统计表</span>
                <el-button type="primary" @click="handleExport"
                  >导出报告</el-button
                >
              </div>
            </template>
            <el-table :data="projectStats" style="width: 100%">
              <el-table-column prop="month" label="月份" />
              <el-table-column prop="newProjects" label="新增项目" />
              <el-table-column prop="completedProjects" label="完成项目" />
              <el-table-column prop="delayedProjects" label="延期项目" />
              <el-table-column prop="avgProgress" label="平均进度" />
              <el-table-column prop="totalTasks" label="总任务数" />
              <el-table-column prop="completedTasks" label="完成任务数" />
            </el-table>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </PageLayout>
</template>

<script setup lang="ts">
import { ref, onMounted, nextTick } from "vue";
import { ElMessage } from "element-plus";
import * as echarts from "echarts";
import PageLayout from "@/components/PageLayout.vue";

const statusChartRef = ref<HTMLDivElement>();
const progressChartRef = ref<HTMLDivElement>();
const trendChartRef = ref<HTMLDivElement>();

const timeRange = ref("3个月");

const projectStats = ref([
  {
    month: "2023年3月",
    newProjects: 5,
    completedProjects: 3,
    delayedProjects: 1,
    avgProgress: "65%",
    totalTasks: 45,
    completedTasks: 28,
  },
  {
    month: "2023年4月",
    newProjects: 7,
    completedProjects: 4,
    delayedProjects: 2,
    avgProgress: "72%",
    totalTasks: 58,
    completedTasks: 35,
  },
  {
    month: "2023年5月",
    newProjects: 6,
    completedProjects: 5,
    delayedProjects: 1,
    avgProgress: "78%",
    totalTasks: 52,
    completedTasks: 38,
  },
]);

const initCharts = () => {
  if (statusChartRef.value) {
    const statusChart = echarts.init(statusChartRef.value);
    const statusOption = {
      tooltip: {
        trigger: "item",
      },
      legend: {
        orient: "vertical",
        left: "left",
      },
      series: [
        {
          name: "项目状态",
          type: "pie",
          radius: "50%",
          data: [
            { value: 15, name: "进行中" },
            { value: 8, name: "已完成" },
            { value: 1, name: "已延期" },
          ],
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: "rgba(0, 0, 0, 0.5)",
            },
          },
        },
      ],
    };
    statusChart.setOption(statusOption);
  }

  if (progressChartRef.value) {
    const progressChart = echarts.init(progressChartRef.value);
    const progressOption = {
      tooltip: {
        trigger: "axis",
      },
      xAxis: {
        type: "category",
        data: ["0-20%", "21-40%", "41-60%", "61-80%", "81-100%"],
      },
      yAxis: {
        type: "value",
      },
      series: [
        {
          data: [2, 5, 8, 6, 3],
          type: "bar",
          showBackground: true,
          backgroundStyle: {
            color: "rgba(180, 180, 180, 0.2)",
          },
        },
      ],
    };
    progressChart.setOption(progressOption);
  }

  if (trendChartRef.value) {
    const trendChart = echarts.init(trendChartRef.value);
    const trendOption = {
      tooltip: {
        trigger: "axis",
      },
      legend: {
        data: ["新增项目", "完成项目", "延期项目"],
      },
      xAxis: {
        type: "category",
        data: ["1月", "2月", "3月", "4月", "5月"],
      },
      yAxis: {
        type: "value",
      },
      series: [
        {
          name: "新增项目",
          type: "line",
          data: [3, 5, 5, 7, 6],
        },
        {
          name: "完成项目",
          type: "line",
          data: [2, 3, 3, 4, 5],
        },
        {
          name: "延期项目",
          type: "line",
          data: [0, 1, 1, 2, 1],
        },
      ],
    };
    trendChart.setOption(trendOption);
  }
};

const handleTimeRangeChange = () => {
  ElMessage.info(`切换到${timeRange.value}的数据`);
};

const handleExport = () => {
  ElMessage.success("报告导出成功");
};

onMounted(() => {
  nextTick(() => {
    initCharts();
  });
});
</script>

<style scoped>
/* 项目报告主容器 */
.project-reports {
  padding: 20px;
}

/* 页面标题 */
.project-reports h2 {
  margin-bottom: 10px;
  color: #303133;
}

/* 页面描述文字 */
.project-reports p {
  margin-bottom: 30px;
  color: #909399;
}

/* 卡片头部 */
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

/* 头部操作区域 */
.header-actions {
  display: flex;
  align-items: center;
}

/* 图表容器 */
.chart-container {
  height: 300px;
  width: 100%;
}

/* 大型图表容器 */
.chart-container-large {
  height: 400px;
  width: 100%;
}
</style>
