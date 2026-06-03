<template>
  <PageLayout>
    <div class="data-tasks">
      <!-- Gfast 风格：一个白色盒子 -->
      <div class="g-card">
        <!-- 搜索栏 -->
        <div class="search-bar">
          <div class="search-group">
            <el-input
              v-model="searchForm.projectName"
              placeholder="项目名称"
              style="width: 180px"
              clearable
            />
            <el-select
              v-model="searchForm.status"
              placeholder="状态"
              style="width: 120px"
              clearable
            >
              <el-option label="进行中" value="进行中" />
              <el-option label="已完成" value="已完成" />
              <el-option label="已暂停" value="已暂停" />
              <el-option label="已取消" value="已取消" />
            </el-select>
          </div>
          <div class="search-actions">
            <el-button type="primary" @click="handleSearch">查询</el-button>
            <el-button @click="resetSearch">重置</el-button>
          </div>
        </div>

        <!-- 操作按钮栏 -->
        <div class="action-bar">
          <el-button type="primary" @click="handleAddTask">
            <el-icon><Plus /></el-icon>
            新增任务
          </el-button>
          <el-button 
            type="danger" 
            :disabled="selectedRows.length === 0"
            @click="handleBatchDelete"
          >
            <el-icon><Delete /></el-icon>
            批量删除
          </el-button>
        </div>

        <!-- 任务列表表格 -->
        <div class="table-wrapper">
          <el-table
            :data="taskList"
            style="width: 100%"
            stripe
            border
            v-loading="loading"
            @selection-change="handleSelectionChange"
          >
            <el-table-column type="selection" width="55" />
            <el-table-column prop="number" label="任务编号" width="100" />
            <el-table-column prop="projectName" label="项目名称" min-width="150" show-overflow-tooltip />
            <el-table-column prop="taskName" label="任务名称" min-width="180" show-overflow-tooltip />
            <el-table-column prop="belongProject" label="所属项目" min-width="150" show-overflow-tooltip />
            <el-table-column prop="contractUnit" label="委托单位" min-width="150" show-overflow-tooltip />
            <el-table-column prop="status" label="状态" width="90">
              <template #default="scope">
                <el-tag :type="getStatusType(scope.row.status)" size="small">
                  {{ scope.row.status }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="startEndTime" label="起止时间" width="200">
              <template #default="scope">
                <span class="nowrap">{{ formatDateRange(scope.row.startEndTime) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="100" fixed="right">
              <template #default="scope">
                <el-button size="small" type="primary" @click="handlePreview(scope.row)">预览</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <!-- 分页组件 -->
        <div class="pagination-footer">
          <el-pagination
            v-model:current-page="pagination.currentPage"
            v-model:page-size="pagination.pageSize"
            :page-sizes="[10, 20, 50, 100]"
            :total="pagination.total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </div>
    </div>
  </PageLayout>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from "vue";
import { useRouter } from "vue-router";
import { ElMessage, ElMessageBox } from "element-plus";
import { Plus, Delete } from "@element-plus/icons-vue";
import PageLayout from "@/components/PageLayout.vue";
// import api from "@/api"; // 联调时取消注释

const router = useRouter();

// ==================== 类型定义 ====================
interface TaskItem {
  id: number;
  number: string;
  projectName: string;
  taskName: string;
  belongProject: string;
  contractUnit: string;
  status: string;
  startEndTime: string;
}

// ==================== 响应式数据 ====================
const loading = ref(false);
const selectedRows = ref<TaskItem[]>([]);

const searchForm = reactive({
  projectName: "",
  status: "",
});

const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0,
});

// 任务列表数据
const taskList = ref<TaskItem[]>([
  {
    id: 1,
    number: "TSK-001",
    projectName: "智慧城市管理系统",
    taskName: "系统需求分析",
    belongProject: "智慧城市项目",
    contractUnit: "中铁某局",
    status: "进行中",
    startEndTime: "2024-01-01 至 2024-12-31",
  },
  {
    id: 2,
    number: "TSK-002",
    projectName: "环境监测平台",
    taskName: "环境数据采集",
    belongProject: "环保项目",
    contractUnit: "环保局",
    status: "已完成",
    startEndTime: "2024-02-01 至 2024-11-30",
  },
  {
    id: 3,
    number: "TSK-003",
    projectName: "数据分析系统",
    taskName: "数据分析模块开发",
    belongProject: "大数据项目",
    contractUnit: "数据局",
    status: "进行中",
    startEndTime: "2024-03-01 至 2025-02-28",
  },
  {
    id: 4,
    number: "TSK-004",
    projectName: "物联网平台",
    taskName: "物联网设备接入",
    belongProject: "物联网项目",
    contractUnit: "物联网公司",
    status: "已暂停",
    startEndTime: "2024-04-01 至 2025-03-31",
  },
  {
    id: 5,
    number: "TSK-005",
    projectName: "智能交通系统",
    taskName: "交通流量分析",
    belongProject: "交通项目",
    contractUnit: "交通局",
    status: "已完成",
    startEndTime: "2024-05-01 至 2025-04-30",
  },
  {
    id: 6,
    number: "TSK-006",
    projectName: "水资源监测系统",
    taskName: "水质监测模块开发",
    belongProject: "水利项目",
    contractUnit: "水利局",
    status: "进行中",
    startEndTime: "2024-06-01 至 2025-05-31",
  },
  {
    id: 7,
    number: "TSK-007",
    projectName: "公共安全平台",
    taskName: "安全监控系统开发",
    belongProject: "安防项目",
    contractUnit: "公安局",
    status: "进行中",
    startEndTime: "2024-07-01 至 2025-06-30",
  },
  {
    id: 8,
    number: "TSK-008",
    projectName: "智慧教育平台",
    taskName: "教育资源整合",
    belongProject: "教育项目",
    contractUnit: "教育局",
    status: "未开始",
    startEndTime: "2024-08-01 至 2025-07-31",
  },
]);

// ==================== 辅助方法 ====================
const formatDateRange = (dateRange: string): string => {
  if (!dateRange) return "-";
  // 将 "2024-01-01 至 2024-12-31" 转换为 "2024/01/01 至 2024/12/31"
  return dateRange.replace(/-/g, "/");
};

const getStatusType = (status: string) => {
  switch (status) {
    case "进行中":
      return "primary";
    case "已完成":
      return "success";
    case "未开始":
      return "info";
    case "已暂停":
      return "warning";
    case "已取消":
      return "danger";
    default:
      return "info";
  }
};

// 表格选中变化
const handleSelectionChange = (selection: TaskItem[]) => {
  selectedRows.value = selection;
};

// 批量删除
const handleBatchDelete = async () => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning("请选择要删除的任务");
    return;
  }

  try {
    await ElMessageBox.confirm(`确定要删除选中的 ${selectedRows.value.length} 个任务吗？`, "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    });

    // 模拟删除
    selectedRows.value.forEach(row => {
      const index = taskList.value.findIndex(t => t.id === row.id);
      if (index !== -1) {
        taskList.value.splice(index, 1);
      }
    });
    
    selectedRows.value = [];
    pagination.total = taskList.value.length;
    ElMessage.success("批量删除成功");
  } catch (error) {
    if (error !== "cancel") {
      console.error("批量删除失败:", error);
      ElMessage.error("批量删除失败");
    }
  }
};

// ==================== 页面交互 ====================
const handleSearch = () => {
  pagination.currentPage = 1;
  ElMessage.success("搜索完成");
  // 这里应该调用API获取搜索结果
};

const resetSearch = () => {
  searchForm.projectName = "";
  searchForm.status = "";
  pagination.currentPage = 1;
  ElMessage.info("搜索条件已重置");
};

const handleAddTask = () => {
  ElMessage.info("新增任务功能待实现");
};

const handlePreview = (row: TaskItem) => {
  console.log("预览任务:", row);
  router.push({
    path: "/data/preview",
    query: { taskId: String(row.id) },
  });
};

const handleSizeChange = (val: number) => {
  pagination.pageSize = val;
  pagination.currentPage = 1;
  // 这里应该调用API获取数据
};

const handleCurrentChange = (val: number) => {
  pagination.currentPage = val;
  // 这里应该调用API获取数据
};

// ==================== 模拟获取数据 ====================
const fetchTaskList = async () => {
  loading.value = true;
  try {
    await new Promise((resolve) => setTimeout(resolve, 300));
    pagination.total = taskList.value.length;
  } catch (error) {
    console.error("获取任务列表失败:", error);
    ElMessage.error("获取任务列表失败");
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  fetchTaskList();
});
</script>

<style scoped>
.data-tasks {
  height: 100%;
}

/* 搜索栏右侧按钮 */
.search-actions {
  display: flex;
  gap: 10px;
  align-items: center;
}

/* 时间不换行 */
.nowrap {
  white-space: nowrap;
}
</style>