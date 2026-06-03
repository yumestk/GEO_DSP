<template>
  <PageLayout>
    <div class="home-container">
      <!-- 页面标题 -->
      <div class="page-header">
        <h1>系统概览</h1>
        <p>欢迎使用 GeoDsp 地理数据处理系统</p>
      </div>

      <!-- 概览卡片 -->
      <div class="overview-cards">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-card class="overview-card">
              <div class="card-content">
                <div class="card-icon bg-blue">
                  <el-icon><Monitor /></el-icon>
                </div>
                <div class="card-info">
                  <h3>{{ deviceCount }}</h3>
                  <p>设备总数</p>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card class="overview-card">
              <div class="card-content">
                <div class="card-icon bg-green">
                  <el-icon><DataAnalysis /></el-icon>
                </div>
                <div class="card-info">
                  <h3>{{ dataCount }}</h3>
                  <p>数据条目</p>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card class="overview-card">
              <div class="card-content">
                <div class="card-icon bg-orange">
                  <el-icon><User /></el-icon>
                </div>
                <div class="card-info">
                  <h3>{{ userCount }}</h3>
                  <p>用户数</p>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card class="overview-card">
              <div class="card-content">
                <div class="card-icon bg-purple">
                  <el-icon><SuccessFilled /></el-icon>
                </div>
                <div class="card-info">
                  <h3>{{ systemUptime }}%</h3>
                  <p>系统可用率</p>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>

      <!-- 数据采集趋势图表 -->
      <div class="chart-section">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>数据采集趋势</span>
            </div>
          </template>
          <div id="trend-chart" ref="trendChartRef" style="height: 400px"></div>
        </el-card>
      </div>

      <!-- 系统状态和最近活动 -->
      <el-row :gutter="20" class="status-section">
        <el-col :span="12">
          <el-card>
            <template #header>
              <div class="card-header">
                <span>系统状态</span>
              </div>
            </template>
            <div class="status-list">
              <div
                class="status-item"
                v-for="item in systemStatus"
                :key="item.name"
              >
                <div class="status-icon">
                  <el-icon
                    :class="
                      item.status === '正常' ? 'status-success' : 'status-warning'
                    "
                  >
                    <SuccessFilled v-if="item.status === '正常'" />
                    <Warning v-else />
                  </el-icon>
                </div>
                <div class="status-name">{{ item.name }}</div>
                <div class="status-value">{{ item.status }}</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card>
            <template #header>
              <div class="card-header">
                <span>最近活动</span>
              </div>
            </template>
            <el-table :data="recentActivity" style="width: 100%">
              <el-table-column prop="user" label="用户" width="120" />
              <el-table-column prop="action" label="操作" width="150" />
              <el-table-column prop="time" label="时间" width="150" />
              <el-table-column prop="details" label="详情" />
            </el-table>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </PageLayout>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import * as echarts from "echarts";
import PageLayout from "@/components/PageLayout.vue";
import {
  Monitor,
  DataAnalysis,
  User,
  SuccessFilled,
  Warning,
} from "@element-plus/icons-vue";

const deviceCount = ref(128);
const dataCount = ref(12458);
const userCount = ref(32);
const systemUptime = ref(99.9);
const systemStatus = ref([
  { name: "数据库", status: "正常" },
  { name: "缓存服务", status: "正常" },
  { name: "消息队列", status: "正常" },
  { name: "文件存储", status: "维护中" },
]);

const recentActivity = ref([
  {
    user: "管理员",
    action: "登录系统",
    time: "10:24",
    details: "从IP 192.168.1.100登录",
  },
  {
    user: "张三",
    action: "上传数据",
    time: "09:45",
    details: "上传项目数据包",
  },
  {
    user: "李四",
    action: "创建报告",
    time: "09:12",
    details: "生成月度分析报告",
  },
  { user: "王五", action: "编辑配置", time: "08:30", details: "更新设备参数" },
  {
    user: "赵六",
    action: "删除数据",
    time: "07:20",
    details: "清理过期日志数据",
  },
]);

const trendChartRef = ref<HTMLDivElement>();

onMounted(() => {
  if (trendChartRef.value) {
    const chart = echarts.init(trendChartRef.value);

    const option = {
      title: {
        text: "数据采集趋势",
        subtext: "过去7天的数据变化情况",
      },
      tooltip: {
        trigger: "axis",
      },
      legend: {
        data: ["数据量", "设备在线数"],
      },
      grid: {
        left: "3%",
        right: "4%",
        bottom: "3%",
        containLabel: true,
      },
      xAxis: {
        type: "category",
        boundaryGap: false,
        data: ["周一", "周二", "周三", "周四", "周五", "周六", "周日"],
      },
      yAxis: {
        type: "value",
      },
      series: [
        {
          name: "数据量",
          type: "line",
          stack: "总量",
          data: [120, 132, 101, 134, 90, 230, 210],
        },
        {
          name: "设备在线数",
          type: "line",
          stack: "总量",
          data: [220, 182, 191, 234, 290, 330, 310],
        },
      ],
    };

    chart.setOption(option);

    window.addEventListener("resize", () => {
      chart.resize();
    });
  }
});
</script>

<style scoped>
/* 首页主容器 */
.home-container {
  padding: 20px;
}

/* 页面头部 */
.page-header {
  margin-bottom: 20px;
}

.page-header h1 {
  margin: 0;
  color: #303133;
}

.page-header p {
  margin: 5px 0 0 0;
  color: #909399;
}

/* 概览卡片区域 */
.overview-cards {
  margin-bottom: 20px;
}

.overview-card {
  height: 100px;
}

.card-content {
  display: flex;
  align-items: center;
}

.card-icon {
  width: 60px;
  height: 60px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  color: white;
}

.card-info h3 {
  margin: 0 0 5px 0;
  font-size: 24px;
  font-weight: bold;
}

.card-info p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.bg-blue { background-color: #409eff; }
.bg-green { background-color: #67c23a; }
.bg-orange { background-color: #e6a23c; }
.bg-purple { background-color: #9013fe; }

.chart-section {
  margin-bottom: 20px;
}

.status-section {
  margin-top: 20px;
}

.status-item {
  display: flex;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid #eee;
}

.status-item:last-child {
  border-bottom: none;
}

.status-icon {
  margin-right: 10px;
  font-size: 18px;
}

.status-success {
  color: #67c23a;
}

.status-warning {
  color: #e6a23c;
}

.status-name {
  flex: 1;
  color: #606266;
}

.status-value {
  font-weight: bold;
}

.card-header {
  font-weight: bold;
}
</style>