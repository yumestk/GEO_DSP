<template>
  <PageLayout>
    <div class="task-management">
      <!-- Gfast 风格：一个白色盒子 -->
      <div class="g-card">
        <!-- 搜索栏 -->
        <div class="search-bar">
          <div class="search-group">
            <el-input
              v-model="searchForm.projectName"
              placeholder="项目名称"
              style="width: 160px"
              clearable
            />
            <el-input
              v-model="searchForm.taskName"
              placeholder="任务名称"
              style="width: 160px"
              clearable
            />
            <el-date-picker
              v-model="searchForm.dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              format="YYYY/MM/DD"
              value-format="YYYY-MM-DD"
              style="width: 260px"
            />
            <el-select
              v-model="searchForm.status"
              placeholder="任务状态"
              style="width: 120px"
              clearable
            >
              <el-option label="未开始" value="未开始" />
              <el-option label="进行中" value="进行中" />
              <el-option label="已暂停" value="已暂停" />
              <el-option label="已完成" value="已完成" />
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
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            新增任务
          </el-button>
          <el-button
            type="danger"
            :disabled="selectedTasks.length === 0"
            @click="handleBatchDelete"
          >
            <el-icon><Delete /></el-icon>
            批量删除
          </el-button>
          <el-button type="success" @click="handleImport">
            <el-icon><Upload /></el-icon>
            批量导入
          </el-button>
          <el-button type="info" @click="handleExport">
            <el-icon><Download /></el-icon>
            导出
          </el-button>
        </div>

        <!-- 任务列表表格 -->
        <div class="table-wrapper">
          <el-table
            :data="tasks"
            style="width: 100%"
            @selection-change="handleSelectionChange"
            v-loading="loading"
          >
            <el-table-column type="selection" width="45" />
            <el-table-column prop="taskCode" label="任务编号" min-width="120" show-overflow-tooltip />
            <el-table-column prop="taskName" label="任务名称" min-width="150" show-overflow-tooltip />
            <el-table-column prop="projectName" label="所属项目" min-width="150" show-overflow-tooltip />
            <el-table-column prop="method" label="作业方法" width="100" />
            <el-table-column prop="operator" label="操作员" width="100" />
            <el-table-column prop="planStartDate" label="计划开始时间" width="110">
              <template #default="scope">
                <span class="nowrap">{{ formatDate(scope.row.planStartDate) }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="planEndDate" label="计划结束时间" width="110">
              <template #default="scope">
                <span class="nowrap">{{ formatDate(scope.row.planEndDate) }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="status" label="任务状态" width="90">
              <template #default="scope">
                <el-tag :type="getStatusType(scope.row.status)" size="small">
                  {{ scope.row.status }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="180" fixed="right">
              <template #default="scope">
                <el-button size="small" type="primary" link @click="handleView(scope.row)">查看</el-button>
                <el-button size="small" type="primary" link @click="handleEdit(scope.row)">编辑</el-button>
                <el-button size="small" type="danger" link @click="handleDelete(scope.row)">删除</el-button>
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

        <!-- 新增/编辑任务弹窗 -->
        <el-dialog
          v-model="dialogVisible"
          :title="dialogTitle"
          width="600px"
          destroy-on-close
        >
          <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
            <el-form-item label="任务名称" prop="taskName">
              <el-input v-model="form.taskName" placeholder="请输入任务名称" />
            </el-form-item>
            <el-form-item label="所属项目" prop="projectName">
              <el-select
                v-model="form.projectName"
                placeholder="请选择所属项目"
                style="width: 100%"
                filterable
              >
                <el-option
                  v-for="project in projectOptions"
                  :key="project.id"
                  :label="project.name"
                  :value="project.name"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="作业方法" prop="method">
              <el-input v-model="form.method" placeholder="请输入作业方法" />
            </el-form-item>
            <el-form-item label="操作员" prop="operator">
              <el-input v-model="form.operator" placeholder="请输入操作员" />
            </el-form-item>
            <el-form-item label="计划开始时间" prop="planStartDate">
              <el-date-picker
                v-model="form.planStartDate"
                type="date"
                placeholder="请选择计划开始时间"
                format="YYYY/MM/DD"
                value-format="YYYY-MM-DD"
                style="width: 100%"
              />
            </el-form-item>
            <el-form-item label="计划结束时间" prop="planEndDate">
              <el-date-picker
                v-model="form.planEndDate"
                type="date"
                placeholder="请选择计划结束时间"
                format="YYYY/MM/DD"
                value-format="YYYY-MM-DD"
                style="width: 100%"
              />
            </el-form-item>
            <el-form-item label="任务状态" prop="status">
              <el-select v-model="form.status" placeholder="请选择任务状态" style="width: 100%">
                <el-option label="未开始" value="未开始" />
                <el-option label="进行中" value="进行中" />
                <el-option label="已暂停" value="已暂停" />
                <el-option label="已完成" value="已完成" />
                <el-option label="已取消" value="已取消" />
              </el-select>
            </el-form-item>
            <el-form-item label="任务备注" prop="remarks">
              <el-input
                v-model="form.remarks"
                type="textarea"
                :rows="3"
                placeholder="请输入任务备注"
              />
            </el-form-item>
          </el-form>
          <template #footer>
            <div class="dialog-footer">
              <el-button @click="dialogVisible = false">取消</el-button>
              <el-button type="primary" @click="handleSave" :loading="submitting">确定</el-button>
            </div>
          </template>
        </el-dialog>

        <!-- 任务详情弹窗 -->
        <el-dialog v-model="detailDialogVisible" title="任务详情" width="600px" destroy-on-close>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="任务编号">{{ currentTask?.taskCode }}</el-descriptions-item>
            <el-descriptions-item label="任务名称">{{ currentTask?.taskName }}</el-descriptions-item>
            <el-descriptions-item label="所属项目">{{ currentTask?.projectName }}</el-descriptions-item>
            <el-descriptions-item label="作业方法">{{ currentTask?.method }}</el-descriptions-item>
            <el-descriptions-item label="操作员">{{ currentTask?.operator }}</el-descriptions-item>
            <el-descriptions-item label="计划开始时间">{{ formatDate(currentTask?.planStartDate) }}</el-descriptions-item>
            <el-descriptions-item label="计划结束时间">{{ formatDate(currentTask?.planEndDate) }}</el-descriptions-item>
            <el-descriptions-item label="开始时间">{{ formatDate(currentTask?.startDate) || "未开始" }}</el-descriptions-item>
            <el-descriptions-item label="结束时间">{{ formatDate(currentTask?.endDate) || "未结束" }}</el-descriptions-item>
            <el-descriptions-item label="任务状态">
              <el-tag :type="getStatusType(currentTask?.status || '')" size="small">
                {{ currentTask?.status }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="任务备注" :span="2">{{ currentTask?.remarks || "无" }}</el-descriptions-item>
          </el-descriptions>
          <template #footer>
            <div class="dialog-footer">
              <el-button @click="detailDialogVisible = false">关闭</el-button>
            </div>
          </template>
        </el-dialog>

        <!-- 批量导入弹窗 -->
        <el-dialog v-model="importDialogVisible" title="批量导入任务" width="500px" destroy-on-close>
          <el-upload
            ref="uploadRef"
            class="upload-demo"
            drag
            action="#"
            :auto-upload="false"
            :on-change="handleFileChange"
            :limit="1"
            accept=".xlsx,.xls"
          >
            <el-icon class="el-icon--upload"><UploadFilled /></el-icon>
            <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
            <template #tip>
              <div class="el-upload__tip">只能上传xlsx/xls文件，且不超过10MB</div>
            </template>
          </el-upload>
          <template #footer>
            <div class="dialog-footer">
              <el-button @click="importDialogVisible = false">取消</el-button>
              <el-button type="primary" @click="handleImportSubmit" :loading="importing">确定</el-button>
            </div>
          </template>
        </el-dialog>
      </div>
    </div>
  </PageLayout>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { Plus, Delete, Download, Upload, UploadFilled } from "@element-plus/icons-vue";
import PageLayout from "@/components/PageLayout.vue";
import api from "@/api";

// ==================== 类型定义 ====================
interface Task {
  id: number;
  uuid: string;
  taskName: string;
  status: string;
  // 映射字段
  taskCode?: string;
  projectName?: string;
  method?: string;
  operator?: string;
  planStartDate?: string;
  planEndDate?: string;
  startDate?: string;
  endDate?: string;
  remarks?: string;
}

interface ProjectOption {
  id: string;
  name: string;
}

// ==================== 响应式数据 ====================
const searchForm = reactive({
  projectName: "",
  taskName: "",
  dateRange: [] as string[],
  status: "",
});

const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0,
});

const tasks = ref<Task[]>([]);
const selectedTasks = ref<Task[]>([]);
const loading = ref(false);
const dialogVisible = ref(false);
const detailDialogVisible = ref(false);
const importDialogVisible = ref(false);
const dialogTitle = ref("");
const submitting = ref(false);
const importing = ref(false);
const formRef = ref();
const uploadRef = ref();
const currentTask = ref<Task>();

const form = ref<Task>({
  id: "",
  taskCode: "",
  taskName: "",
  projectName: "",
  method: "",
  operator: "",
  planStartDate: "",
  planEndDate: "",
  startDate: "",
  endDate: "",
  status: "未开始",
  remarks: "",
});

const rules = {
  taskName: [{ required: true, message: "请输入任务名称", trigger: "blur" }],
  projectName: [{ required: true, message: "请选择所属项目", trigger: "change" }],
  method: [{ required: true, message: "请输入作业方法", trigger: "blur" }],
  operator: [{ required: true, message: "请输入操作员", trigger: "blur" }],
  planStartDate: [{ required: true, message: "请选择计划开始时间", trigger: "change" }],
  planEndDate: [{ required: true, message: "请选择计划结束时间", trigger: "change" }],
  status: [{ required: true, message: "请选择任务状态", trigger: "change" }],
};

// ==================== 辅助方法 ====================
const formatDate = (dateTime: string | undefined): string => {
  if (!dateTime) return "-";
  const dateStr = String(dateTime);
  const datePart = dateStr.split(" ")[0];
  if (!datePart) return "-";
  return datePart.replace(/-/g, "/");
};

const getStatusType = (status: string) => {
  switch (status) {
    case "进行中": return "primary";
    case "已完成": return "success";
    case "未开始": return "info";
    case "已暂停": return "warning";
    case "已取消": return "danger";
    default: return "info";
  }
};

// ==================== 数据 ====================
const projectOptions = ref<ProjectOption[]>([]);

const statusMap: Record<number, string> = { 0: "未开始", 1: "进行中", 2: "已完成", 3: "已暂停", 4: "已取消" };
const reverseStatusMap: Record<string, number> = { "未开始": 0, "进行中": 1, "已完成": 2, "已暂停": 3, "已取消": 4 };

// ==================== API 调用 ====================
const getTaskList = async () => {
  loading.value = true;
  try {
    const params: any = { pageNum: pagination.currentPage, pageSize: pagination.pageSize };
    if (searchForm.taskName) params.keyword = searchForm.taskName;
    const res = await api.collectTask.getList(params);
    const list = (res.data?.records ?? []).map((item: any) => ({
      id: item.id,
      uuid: item.uuid,
      taskName: item.taskName,
      taskCode: item.taskName?.substring(0, 2) + "-" + item.id,
      projectName: String(item.projectId ?? ""),
      method: String(item.methodId ?? ""),
      operator: String(item.executorId ?? ""),
      planStartDate: item.startTime,
      planEndDate: item.endTime,
      status: statusMap[item.status] || "未知",
      remarks: item.note,
    }));
    tasks.value = list;
    pagination.total = res.data?.total ?? 0;
  } catch (error) {
    console.error("获取任务列表失败:", error);
    ElMessage.error("获取任务列表失败");
  } finally {
    loading.value = false;
  }
};

// ==================== 页面交互 ====================
const handleSearch = () => {
  pagination.currentPage = 1;
  getTaskList();
};

const resetSearch = () => {
  searchForm.projectName = "";
  searchForm.taskName = "";
  searchForm.dateRange = [];
  searchForm.status = "";
  pagination.currentPage = 1;
  getTaskList();
};

const handleSizeChange = (size: number) => {
  pagination.pageSize = size;
  pagination.currentPage = 1;
  getTaskList();
};

const handleCurrentChange = (page: number) => {
  pagination.currentPage = page;
  getTaskList();
};

const handleSelectionChange = (selection: Task[]) => {
  selectedTasks.value = selection;
};

const handleAdd = () => {
  dialogTitle.value = "新增任务";
  dialogVisible.value = true;
  resetForm();
};

const handleView = (row: Task) => {
  currentTask.value = row;
  detailDialogVisible.value = true;
};

const handleEdit = (row: Task) => {
  dialogTitle.value = "编辑任务";
  dialogVisible.value = true;
  Object.assign(form.value, row);
};

const handleDelete = async (row: Task) => {
  try {
    await ElMessageBox.confirm(`确定要删除任务"${row.taskName}"吗？`, "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    });
    
    const res = await api.collectTask.delete(row.uuid);
    if (res.data) {
      ElMessage.success("删除成功");
      getTaskList();
    } else {
      ElMessage.error("删除失败");
    }
  } catch (error) {
    if (error !== "cancel") {
      console.error("删除任务失败:", error);
      ElMessage.error("删除失败");
    }
  }
};

const handleBatchDelete = async () => {
  if (selectedTasks.value.length === 0) {
    ElMessage.warning("请选择要删除的任务");
    return;
  }
  
  try {
    await ElMessageBox.confirm(`确定要删除选中的${selectedTasks.value.length}个任务吗？`, "警告", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    });
    
    for (const t of selectedTasks.value) {
      await api.collectTask.delete(t.uuid);
    }
    ElMessage.success("批量删除成功");
    getTaskList();
  } catch (error) {
    if (error !== "cancel") {
      console.error("批量删除失败:", error);
      ElMessage.error("批量删除失败");
    }
  }
};

const handleImport = () => {
  importDialogVisible.value = true;
};

const handleFileChange = (file: any) => {
  console.log("文件改变:", file);
};

const handleImportSubmit = () => {
  importing.value = true;
  setTimeout(() => {
    importing.value = false;
    importDialogVisible.value = false;
    ElMessage.success("导入成功");
    getTaskList();
  }, 1000);
};

const handleExport = () => {
  ElMessage.info("导出功能开发中");
};

const handleSave = async () => {
  if (!formRef.value) return;
  
  try {
    await formRef.value.validate();
    submitting.value = true;
    
    if (form.value.uuid) {
      const res = await api.collectTask.update(form.value.uuid, {
        taskName: form.value.taskName,
        status: reverseStatusMap[form.value.status] ?? 1,
        note: form.value.remarks,
      });
      if (res.data) {
        ElMessage.success("更新成功");
      } else {
        ElMessage.error("更新失败");
        return;
      }
    } else {
      const res = await api.collectTask.create({
        taskName: form.value.taskName,
        methodId: Number(form.value.method) || undefined,
        executorId: Number(form.value.operator) || undefined,
        startTime: form.value.planStartDate,
        endTime: form.value.planEndDate,
        status: reverseStatusMap[form.value.status] ?? 0,
        note: form.value.remarks,
      });
      if (res.data) {
        ElMessage.success("添加成功");
      } else {
        ElMessage.error("添加失败");
        return;
      }
    }
    
    dialogVisible.value = false;
    getTaskList();
  } catch (error) {
    console.error("保存失败:", error);
    ElMessage.error("保存失败");
  } finally {
    submitting.value = false;
  }
};

const resetForm = () => {
  if (formRef.value) formRef.value.resetFields();
  form.value = {
    id: 0,
    uuid: "",
    taskCode: "",
    taskName: "",
    projectName: "",
    method: "",
    operator: "",
    planStartDate: "",
    planEndDate: "",
    status: "未开始",
    remarks: "",
  };
};

onMounted(() => {
  getTaskList();
});
</script>

<style scoped>
.task-management {
  height: 100%;
}

/* 对话框底部 */
.dialog-footer {
  text-align: right;
}

/* 上传组件样式 */
.upload-demo {
  width: 100%;
}
</style>