<template>
  <PageLayout>
    <div class="task-preview">
      <!-- 任务基础信息 -->
      <el-card class="task-info-card">
        <template #header>
          <div class="card-header">
            <span>任务基础信息</span>
            <el-button @click="goBack">返回</el-button>
          </div>
        </template>
        <el-descriptions :column="3" border>
          <el-descriptions-item label="任务名">{{
            taskInfo.taskName
          }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="getStatusType(taskInfo.status)">{{
              taskInfo.status
            }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="起止时间">{{
            taskInfo.startEndTime
          }}</el-descriptions-item>
        </el-descriptions>
      </el-card>

      <el-card class="data-visualization-card">
        <el-container>
          <el-aside width="250px" class="sampling-aside">
            <template #header>
              <div class="card-header">
                <span>采样点结构</span>
              </div>
            </template>
            <el-tree
              :data="samplingData"
              :props="defaultProps"
              accordion
              @node-click="handleNodeClick"
            />
          </el-aside>

          <el-main class="visualization-main">
            <div class="sampling-info-header">
              <span class="info-label">采样信息：</span>
              <span class="info-content">{{ selectedSamplingInfo }}</span>
            </div>

            <div class="debug-info" v-if="debugInfo">
              <span class="info-label">调试信息：</span>
              <span class="info-content">{{ debugInfo }}</span>
            </div>

            <div class="charts-row">
              <div
                class="chart-container"
                ref="locationChartRef"
                @click="openFullScreen('定位信息图')"
              ></div>
              <div
                class="chart-container"
                ref="decayChartRef"
                @click="openFullScreen('衰减曲线')"
              ></div>
              <div
                class="chart-container"
                ref="shutdownChartRef"
                @click="openFullScreen('关断时间监控图')"
              ></div>
              <div
                class="chart-container"
                ref="currentChartRef"
                @click="openFullScreen('电流监控图')"
              ></div>
            </div>
          </el-main>
        </el-container>
      </el-card>

      <el-dialog
        v-model="fullScreenVisible"
        :title="fullScreenTitle"
        width="95%"
        top="2.5vh"
        :before-close="closeFullScreen"
        class="fullscreen-dialog"
      >
        <div class="fullscreen-content">
          <div class="fullscreen-chart" ref="fullscreenChartRef"></div>
        </div>
      </el-dialog>
    </div>
  </PageLayout>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, nextTick } from "vue";
import { useRouter, useRoute } from "vue-router";
import { Location, TrendCharts, Clock, Monitor } from "@element-plus/icons-vue";
import * as echarts from "echarts";

import PageLayout from "@/components/PageLayout.vue";
const router = useRouter();
const route = useRoute();
const fullScreenVisible = ref(false);
const fullScreenTitle = ref("");
const chartInstances = ref<{
  [key: string]: echarts.ECharts & { resizeHandler?: () => void };
}>({});

const locationChartRef = ref<HTMLDivElement>();
const decayChartRef = ref<HTMLDivElement>();
const shutdownChartRef = ref<HTMLDivElement>();
const currentChartRef = ref<HTMLDivElement>();
const fullscreenChartRef = ref<HTMLDivElement>();

const debugInfo = ref("初始化中...");

interface SamplingNode {
  label: string;
  children?: SamplingNode[];
}

interface TaskData {
  taskName: string;
  status: string;
  startEndTime: string;
  samplingData: SamplingNode[];
}

const taskInfo = ref({
  taskName: "地质勘探任务1",
  status: "进行中",
  startEndTime: "2023-01-01 至 2023-12-31",
});
const samplingData = ref<SamplingNode[]>([]);
const defaultProps = {
  children: "children",
  label: "label",
};

const selectedSamplingInfo = ref("线路 1 → 点号 1.1 → 采样 1.1.1");

const getStatusType = (status: string) => {
  switch (status) {
    case "进行中":
      return "success";
    case "已完成":
      return "info";
    case "已暂停":
      return "warning";
    case "已取消":
      return "danger";
    default:
      return "info";
  }
};

const goBack = () => {
  router.push("/data/tasks");
};

const handleNodeClick = (data: SamplingNode) => {
  console.log("点击节点:", data);
  selectedSamplingInfo.value = data.label;

  if (!data.children || data.children.length === 0) {
    console.log("更新图表数据");
  }
};

const openFullScreen = (title: string) => {
  fullScreenTitle.value = title;
  fullScreenVisible.value = true;
  setTimeout(() => {
    initFullScreenChart(title);
  }, 500);
};

const closeFullScreen = () => {
  fullScreenVisible.value = false;
};

// 根据图表标题获取对应的图标组件
const getChartIcon = (title: string) => {
  switch (title) {
    case "定位信息图":
      return Location;
    case "衰减曲线":
      return TrendCharts;
    case "关断时间监控图":
      return Clock;
    case "电流监控图":
      return Monitor;
    default:
      return Location;
  }
};

// 根据任务ID获取任务数据
const getTaskDataById = (taskId: any): TaskData | null => {
  const taskDataMap: Record<string, TaskData> = {
    "1": {
      taskName: "地质勘探任务1",
      status: "进行中",
      startEndTime: "2023-01-01 至 2023-12-31",
      samplingData: [
        {
          label: "线路 1",
          children: [
            {
              label: "点号 1.1",
              children: [{ label: "采样 1.1.1" }, { label: "采样 1.1.2" }],
            },
            {
              label: "点号 1.2",
              children: [
                { label: "采样 1.2.1" },
                { label: "采样 1.2.2" },
                { label: "采样 1.2.3" },
              ],
            },
          ],
        },
        {
          label: "线路 2",
          children: [
            {
              label: "点号 2.1",
              children: [{ label: "采样 2.1.1" }, { label: "采样 2.1.2" }],
            },
          ],
        },
      ],
    },
    "2": {
      taskName: "地质勘探任务2",
      status: "进行中",
      startEndTime: "2025-02-01 至 2026-01-31",
      samplingData: [
        {
          label: "线路 3",
          children: [
            {
              label: "点号 3.1",
              children: [{ label: "采样 3.1.1" }, { label: "采样 3.1.2" }],
            },
            {
              label: "点号 3.2",
              children: [{ label: "采样 3.2.1" }, { label: "采样 3.2.2" }],
            },
          ],
        },
        {
          label: "线路 4",
          children: [
            {
              label: "点号 4.1",
              children: [
                { label: "采样 4.1.1" },
                { label: "采样 4.1.2" },
                { label: "采样 4.1.3" },
              ],
            },
          ],
        },
      ],
    },
    "3": {
      taskName: "地质勘探任务3",
      status: "已完成",
      startEndTime: "2023-03-01 至 2024-02-28",
      samplingData: [
        {
          label: "线路 5",
          children: [
            {
              label: "点号 5.1",
              children: [
                { label: "采样 5.1.1" },
                { label: "采样 5.1.2" },
                { label: "采样 5.1.3" },
              ],
            },
            {
              label: "点号 5.2",
              children: [
                { label: "采样 5.2.1" },
                { label: "采样 5.2.2" },
                { label: "采样 5.2.3" },
                { label: "采样 5.2.4" },
              ],
            },
          ],
        },
      ],
    },
  };

  return taskDataMap[taskId] || null;
};

const loadSamplingData = (taskId: any) => {
  const taskData = getTaskDataById(taskId);
  if (taskData) {
    taskInfo.value = {
      taskName: taskData.taskName,
      status: taskData.status,
      startEndTime: taskData.startEndTime,
    };
    samplingData.value = taskData.samplingData;
    if (
      samplingData.value &&
      samplingData.value.length > 0 &&
      samplingData.value[0] &&
      samplingData.value[0].children &&
      samplingData.value[0].children.length > 0 &&
      samplingData.value[0].children[0] &&
      samplingData.value[0].children[0].children &&
      samplingData.value[0].children[0].children.length > 0
    ) {
      const firstNode = samplingData.value[0] as SamplingNode;
      const firstChild = firstNode.children![0] as SamplingNode;
      const firstGrandChild = firstChild.children![0] as SamplingNode;
      selectedSamplingInfo.value = `${firstNode.label} → ${firstChild.label} → ${firstGrandChild.label}`;
    }
  }
};

const handleEscKey = (event: KeyboardEvent) => {
  if (event.key === "Escape" && fullScreenVisible.value) {
    closeFullScreen();
  }
};

const initCharts = () => {
  debugInfo.value = "开始初始化图表...";

  nextTick(() => {
    setTimeout(() => {
      debugInfo.value = "延迟初始化中...";

      const locationContainer = locationChartRef.value;
      const decayContainer = decayChartRef.value;
      const shutdownContainer = shutdownChartRef.value;
      const currentContainer = currentChartRef.value;

      debugInfo.value = `容器状态: location=${!!locationContainer}, decay=${!!decayContainer}, shutdown=${!!shutdownContainer}, current=${!!currentContainer}`;

      if (locationContainer) {
        debugInfo.value = "初始化定位信息图...";

        locationContainer.style.width = "100%";
        locationContainer.style.height = "100%";
        locationContainer.style.display = "block";

        requestAnimationFrame(() => {
          const locationChart = echarts.init(locationContainer);
          chartInstances.value["location"] = locationChart;

          const locationOption = {
            title: {
              text: "定位信息",
              textStyle: {
                fontSize: 14,
              },
            },
            tooltip: {
              trigger: "axis",
              axisPointer: {
                type: "cross",
              },
            },
            grid: {
              left: "10%",
              right: "10%",
              bottom: "15%",
              top: "20%",
            },
            xAxis: {
              type: "category",
              name: "X坐标 (m)",
              data: Array.from({ length: 20 }, (_, i) => (i * 10).toString()),
            },
            yAxis: {
              type: "value",
              name: "Y坐标 (m)",
            },
            dataZoom: [
              {
                type: "inside",
                xAxisIndex: 0,
                filterMode: "none",
              },
              {
                type: "inside",
                yAxisIndex: 0,
                filterMode: "none",
              },
            ],
            series: [
              {
                name: "采样点",
                type: "scatter",
                symbolSize: 8,
                data: Array.from({ length: 20 }, (_, i) => [
                  i * 10,
                  Math.sin(i * 0.5) * 50 + 100 + Math.random() * 20,
                ]),
              },
            ],
          };

          locationChart.setOption(locationOption);
          debugInfo.value = "定位信息图初始化完成";

          locationChart.resize();
        });
      } else {
        debugInfo.value += " | 定位信息图容器不存在";
      }

      if (decayContainer) {
        debugInfo.value = "初始化衰减曲线图...";

        decayContainer.style.width = "100%";
        decayContainer.style.height = "100%";
        decayContainer.style.display = "block";

        requestAnimationFrame(() => {
          const decayChart = echarts.init(decayContainer);
          chartInstances.value["decay"] = decayChart;

          const decayOption = {
            title: {
              text: "衰减曲线",
              textStyle: {
                fontSize: 14,
              },
            },
            tooltip: {
              trigger: "axis",
            },
            legend: {
              data: ["通道1", "通道2"],
              top: "15%",
            },
            grid: {
              left: "10%",
              right: "10%",
              bottom: "15%",
              top: "25%",
            },
            xAxis: {
              type: "category",
              name: "时间 (ms)",
              data: Array.from({ length: 50 }, (_, i) => (i * 2).toString()),
            },
            yAxis: {
              type: "value",
              name: "幅度 (mV)",
            },
            dataZoom: [
              {
                type: "inside",
                xAxisIndex: 0,
              },
              {
                type: "slider",
                xAxisIndex: 0,
                bottom: "5%",
              },
            ],
            series: [
              {
                name: "通道1",
                type: "line",
                smooth: true,
                data: Array.from(
                  { length: 50 },
                  (_, i) =>
                    Math.exp(-i * 0.05) * 100 * (1 + Math.random() * 0.1),
                ),
              },
              {
                name: "通道2",
                type: "line",
                smooth: true,
                data: Array.from(
                  { length: 50 },
                  (_, i) =>
                    Math.exp(-i * 0.08) * 80 * (1 + Math.random() * 0.1),
                ),
              },
            ],
          };

          decayChart.setOption(decayOption);
          debugInfo.value = "衰减曲线图初始化完成";
          decayChart.resize();
        });
      } else {
        debugInfo.value += " | 衰减曲线图容器不存在";
      }

      if (shutdownContainer) {
        debugInfo.value = "初始化关断时间监控图...";

        shutdownContainer.style.width = "100%";
        shutdownContainer.style.height = "100%";
        shutdownContainer.style.display = "block";

        requestAnimationFrame(() => {
          const shutdownChart = echarts.init(shutdownContainer);
          chartInstances.value["shutdown"] = shutdownChart;

          const shutdownOption = {
            title: {
              text: "关断时间监控",
              textStyle: {
                fontSize: 14,
              },
            },
            tooltip: {
              trigger: "axis",
            },
            grid: {
              left: "10%",
              right: "10%",
              bottom: "15%",
              top: "20%",
            },
            xAxis: {
              type: "category",
              name: "采样点",
              data: Array.from({ length: 30 }, (_, i) => `点${i + 1}`),
            },
            yAxis: {
              type: "value",
              name: "关断时间 (ms)",
            },
            dataZoom: [
              {
                type: "inside",
                xAxisIndex: 0,
              },
            ],
            series: [
              {
                name: "关断时间",
                type: "bar",
                data: Array.from(
                  { length: 30 },
                  () => 50 + Math.random() * 100,
                ),
              },
            ],
          };

          shutdownChart.setOption(shutdownOption);
          debugInfo.value = "关断时间监控图初始化完成";
          shutdownChart.resize();
        });
      } else {
        debugInfo.value += " | 关断时间监控图容器不存在";
      }

      if (currentContainer) {
        debugInfo.value = "初始化电流监控图...";

        currentContainer.style.width = "100%";
        currentContainer.style.height = "100%";
        currentContainer.style.display = "block";

        requestAnimationFrame(() => {
          const currentChart = echarts.init(currentContainer);
          chartInstances.value["current"] = currentChart;

          const currentOption = {
            title: {
              text: "电流监控",
              textStyle: {
                fontSize: 14,
              },
            },
            tooltip: {
              trigger: "axis",
            },
            grid: {
              left: "10%",
              right: "10%",
              bottom: "15%",
              top: "20%",
            },
            xAxis: {
              type: "category",
              name: "时间 (s)",
              data: Array.from({ length: 100 }, (_, i) => (i * 0.1).toFixed(1)),
            },
            yAxis: {
              type: "value",
              name: "电流 (A)",
            },
            dataZoom: [
              {
                type: "inside",
                xAxisIndex: 0,
              },
              {
                type: "slider",
                xAxisIndex: 0,
                bottom: "5%",
              },
            ],
            series: [
              {
                name: "电流",
                type: "line",
                smooth: true,
                data: Array.from(
                  { length: 100 },
                  (_, i) => 5 + 2 * Math.sin(i * 0.2) + Math.random() * 0.5,
                ),
              },
            ],
          };

          currentChart.setOption(currentOption);
          debugInfo.value = "电流监控图初始化完成";
          currentChart.resize();
        });
      } else {
        debugInfo.value += " | 电流监控图容器不存在";
      }

      const resizeHandler = () => {
        Object.values(chartInstances.value).forEach((chart) => {
          if (chart) {
            requestAnimationFrame(() => {
              chart.resize();
            });
          }
        });
      };

      window.addEventListener("resize", resizeHandler);

      debugInfo.value = "所有图表初始化完成";
    });
  });
};

const initFullScreenChart = (title: string) => {
  setTimeout(() => {
    const fullscreenContainer = fullscreenChartRef.value;
    if (fullscreenContainer) {
      if (chartInstances.value["fullscreen"]) {
        chartInstances.value["fullscreen"].dispose();
      }

      const fullScreenChart = echarts.init(fullscreenContainer);
      chartInstances.value["fullscreen"] = fullScreenChart;

      let option;

      switch (title) {
        case "定位信息图":
          option = {
            title: {
              text: "定位信息图",
              textStyle: {
                fontSize: 24,
              },
            },
            tooltip: {
              trigger: "axis",
              axisPointer: {
                type: "cross",
              },
            },
            grid: {
              left: "10%",
              right: "10%",
              bottom: "15%",
              top: "15%",
            },
            xAxis: {
              type: "category",
              name: "X坐标 (m)",
              data: Array.from({ length: 100 }, (_, i) => (i * 5).toString()),
            },
            yAxis: {
              type: "value",
              name: "Y坐标 (m)",
            },
            dataZoom: [
              {
                type: "inside",
                xAxisIndex: 0,
                filterMode: "none",
              },
              {
                type: "inside",
                yAxisIndex: 0,
                filterMode: "none",
              },
              {
                type: "slider",
                xAxisIndex: 0,
                bottom: "5%",
              },
              {
                type: "slider",
                yAxisIndex: 0,
                left: "5%",
              },
            ],
            series: [
              {
                name: "采样点",
                type: "scatter",
                symbolSize: 10,
                data: Array.from({ length: 100 }, (_, i) => [
                  i * 5,
                  Math.sin(i * 0.1) * 100 + 200 + Math.random() * 50,
                ]),
              },
            ],
          };
          break;

        case "衰减曲线":
          option = {
            title: {
              text: "衰减曲线",
              textStyle: {
                fontSize: 24,
              },
            },
            tooltip: {
              trigger: "axis",
            },
            legend: {
              data: ["通道1", "通道2", "通道3"],
              top: "10%",
            },
            grid: {
              left: "10%",
              right: "10%",
              bottom: "15%",
              top: "20%",
            },
            xAxis: {
              type: "category",
              name: "时间 (ms)",
              data: Array.from({ length: 200 }, (_, i) => i.toString()),
            },
            yAxis: {
              type: "value",
              name: "幅度 (mV)",
            },
            dataZoom: [
              {
                type: "inside",
                xAxisIndex: 0,
              },
              {
                type: "slider",
                xAxisIndex: 0,
                bottom: "5%",
              },
            ],
            series: [
              {
                name: "通道1",
                type: "line",
                smooth: true,
                data: Array.from(
                  { length: 200 },
                  (_, i) =>
                    Math.exp(-i * 0.02) * 200 * (1 + Math.random() * 0.05),
                ),
              },
              {
                name: "通道2",
                type: "line",
                smooth: true,
                data: Array.from(
                  { length: 200 },
                  (_, i) =>
                    Math.exp(-i * 0.03) * 150 * (1 + Math.random() * 0.05),
                ),
              },
              {
                name: "通道3",
                type: "line",
                smooth: true,
                data: Array.from(
                  { length: 200 },
                  (_, i) =>
                    Math.exp(-i * 0.025) * 180 * (1 + Math.random() * 0.05),
                ),
              },
            ],
          };
          break;

        case "关断时间监控图":
          option = {
            title: {
              text: "关断时间监控图",
              textStyle: {
                fontSize: 24,
              },
            },
            tooltip: {
              trigger: "axis",
            },
            grid: {
              left: "10%",
              right: "10%",
              bottom: "15%",
              top: "15%",
            },
            xAxis: {
              type: "category",
              name: "采样点",
              data: Array.from({ length: 100 }, (_, i) => `点${i + 1}`),
            },
            yAxis: {
              type: "value",
              name: "关断时间 (ms)",
            },
            dataZoom: [
              {
                type: "inside",
                xAxisIndex: 0,
              },
              {
                type: "slider",
                xAxisIndex: 0,
                bottom: "5%",
              },
            ],
            series: [
              {
                name: "关断时间",
                type: "bar",
                data: Array.from(
                  { length: 100 },
                  () => 50 + Math.random() * 150,
                ),
              },
            ],
          };
          break;

        case "电流监控图":
          option = {
            title: {
              text: "电流监控图",
              textStyle: {
                fontSize: 24,
              },
            },
            tooltip: {
              trigger: "axis",
            },
            grid: {
              left: "10%",
              right: "10%",
              bottom: "15%",
              top: "15%",
            },
            xAxis: {
              type: "category",
              name: "时间 (s)",
              data: Array.from({ length: 500 }, (_, i) => (i * 0.1).toFixed(1)),
            },
            yAxis: {
              type: "value",
              name: "电流 (A)",
            },
            dataZoom: [
              {
                type: "inside",
                xAxisIndex: 0,
              },
              {
                type: "slider",
                xAxisIndex: 0,
                bottom: "5%",
              },
            ],
            series: [
              {
                name: "电流",
                type: "line",
                smooth: true,
                data: Array.from(
                  { length: 500 },
                  (_, i) => 5 + 2 * Math.sin(i * 0.1) + Math.random() * 0.5,
                ),
              },
            ],
          };
          break;

        default:
          return;
      }

      fullScreenChart.setOption(option);

      const resizeHandler = () => {
        fullScreenChart.resize();
      };
      window.addEventListener("resize", resizeHandler);

      (chartInstances.value["fullscreen"] as any).resizeHandler = resizeHandler;
    }
  }, 300);
};

onMounted(() => {
  const taskId = route.query.taskId;
  console.log("任务ID:", taskId);

  loadSamplingData(taskId);

  debugInfo.value = "组件已挂载，准备初始化图表...";

  setTimeout(() => {
    debugInfo.value = "开始延迟初始化图表...";
    initCharts();
  }, 500);

  document.addEventListener("keydown", handleEscKey);
});

onUnmounted(() => {
  document.removeEventListener("keydown", handleEscKey);

  Object.values(chartInstances.value).forEach((chart) => {
    if (chart) {
      if ((chart as any).resizeHandler) {
        window.removeEventListener("resize", (chart as any).resizeHandler);
      }
      chart.dispose();
    }
  });

  window.removeEventListener("resize", () => {});
});
</script>

<style scoped>
.task-preview {
  padding: 0;
  height: calc(100vh - 55px);
  max-height: calc(100vh - 55px);
  display: flex;
  flex-direction: column;
  overflow: hidden;
  box-sizing: border-box;
  margin-top: 0;
}

/* 任务信息卡片 */
.task-info-card {
  margin: 0 15px 0 15px;
  flex-shrink: 0;
}

/* 任务信息卡片内容区域 */
.task-info-card :deep(.el-card__body) {
  padding: 2px;
}

/* Element Plus 描述列表组件 */
.task-info-card :deep(.el-descriptions) {
  font-size: 14px;
  margin: 0;
}

/* 描述列表标签 */
.task-info-card :deep(.el-descriptions__label) {
  font-weight: bold;
  padding: 2px 6px;
  margin: 0;
}

/* 描述列表内容 */
.task-info-card :deep(.el-descriptions__content) {
  padding: 2px 6px;
  margin: 0;
}

.data-visualization-card {
  margin: 2px 15px 0 15px;
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  min-height: 0;
}

/* 数据可视化卡片内容区域 */
.data-visualization-card :deep(.el-card__body) {
  padding: 2px;
  display: flex;
  flex-direction: column;
  height: 100%;
  overflow: hidden;
}

/* 卡片头部区域 */
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 18px;
  line-height: 1.2;
  padding: 0;
  margin: 0;
}

/* 采样点侧边栏 */
.sampling-aside {
  background-color: #f5f7fa;
  border-right: 1px solid #e4e7ed;
  padding: 5px;
  overflow-y: auto;
}

/* 可视化主内容区域 */
.visualization-main {
  padding: 0;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  min-height: 0;
}

.sampling-info-header {
  display: flex;
  align-items: center;
  margin-bottom: 2px;
  padding: 1px 8px;
  background-color: #f5f7fa;
  border-radius: 4px;
  border: 1px solid #e4e7ed;
  flex-shrink: 0;
  height: 20px;
}

/* 信息标签 */
.info-label {
  font-weight: bold;
  color: #303133;
  font-size: 13px;
  margin-right: 5px;
  white-space: nowrap;
}

/* 信息内容 */
.info-content {
  color: #606266;
  font-size: 13px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  flex: 1;
}

/* 调试信息区域 */
.debug-info {
  display: flex;
  align-items: center;
  margin-bottom: 2px;
  padding: 1px 8px;
  background-color: #fff8e1;
  border-radius: 4px;
  border: 1px solid #ffecb3;
  flex-shrink: 0;
  height: 20px;
}

/* 修复图表容器布局 */
.charts-row {
  flex: 1;
  display: grid;
  grid-template-columns: 1fr 1fr;
  grid-template-rows: 1fr 1fr;
  gap: 10px;
  padding: 10px;
  overflow: hidden;
  height: 100%;
  width: 100%;
  box-sizing: border-box;
}

.chart-container {
  height: 100%;
  width: 100%;
  min-height: 200px;
  cursor: pointer;
  transition: all 0.3s ease;
  overflow: hidden;
  position: relative;
  margin: 0;
  padding: 0;
  display: block;
  box-sizing: border-box;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  background-color: #fff;
}

.chart-container:hover {
  transform: scale(1.02);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

/* 全屏图表容器 */
.fullscreen-chart {
  width: 100%;
  height: 100%;
  min-height: 400px;
}

/* 全屏对话框 */
.fullscreen-dialog :deep(.el-dialog) {
  margin: 2.5vh auto;
  height: 95vh;
  display: flex;
  flex-direction: column;
}

/* 全屏对话框内容区域 */
.fullscreen-dialog :deep(.el-dialog__body) {
  padding: 0;
  height: 100%;
  overflow: hidden;
}

/* 全屏内容区域 */
.fullscreen-content {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #fafafa;
}

/* 全屏图表 */
.fullscreen-chart {
  width: 100%;
  height: 100%;
}

/* 响应式调整 - 最大高度800px */
@media (max-height: 800px) {
  .chart-container {
    min-height: 150px;
  }
}

/* 响应式调整 - 最大高度700px */
@media (max-height: 700px) {
  .chart-container {
    min-height: 120px;
  }
}

/* Element Plus 描述列表组件 */
:deep(.el-descriptions) {
  font-size: 14px;
}

/* 描述列表标签 */
:deep(.el-descriptions__label) {
  font-weight: bold;
}

/* 描述列表内容 */
:deep(.el-descriptions__content) {
  padding: 8px 12px;
}
</style>
