<template>
  <PageLayout>
    <div class="project-management">
      <!-- Gfast 风格：一个白色盒子 -->
      <div class="g-card">
        <!-- 搜索区域 -->
        <div class="search-bar">
          <div class="search-group">
            <el-input
              v-model="searchForm.projectName"
              placeholder="项目名称"
              style="width: 180px"
              clearable
            />
            <el-input
              v-model="searchForm.companyId"
              placeholder="企业ID"
              style="width: 120px"
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
              placeholder="项目状态"
              style="width: 120px"
              clearable
            >
              <el-option label="未开始" :value="0" />
              <el-option label="进行中" :value="1" />
              <el-option label="已完成" :value="2" />
              <el-option label="终止" :value="3" />
            </el-select>
          </div>
          <div class="search-actions">
            <el-button type="primary" @click="handleSearch">查询</el-button>
            <el-button @click="resetSearch">重置</el-button>
          </div>
        </div>

        <!-- 操作按钮栏 -->
        <div class="action-bar">
          <el-button v-permission="'project:add'" type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            新增项目
          </el-button>
          <el-button
            v-permission="'project:delete'"
            type="danger"
            :disabled="selectedProjects.length === 0"
            @click="handleBatchDelete"
          >
            <el-icon><Delete /></el-icon>
            批量删除
          </el-button>
          <el-button v-permission="'project:export'" type="success" @click="handleExport">
            <el-icon><Download /></el-icon>
            导出
          </el-button>
        </div>

        <!-- 项目列表表格 -->
        <div class="table-wrapper">
          <el-table
            :data="projects"
            style="width: 100%"
            @selection-change="handleSelectionChange"
            v-loading="loading"
          >
            <el-table-column type="selection" width="45" />
            <el-table-column prop="uuid" label="项目编号" min-width="180" show-overflow-tooltip />
            <el-table-column prop="projectName" label="项目名称" min-width="150" show-overflow-tooltip />
            <el-table-column prop="companyId" label="企业ID" width="100" />
            <el-table-column prop="startTime" label="开始时间" width="110">
              <template #default="scope">
                <span class="nowrap">{{ formatDate(scope.row.startTime) }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="expectEndTime" label="预计结束时间" width="110">
              <template #default="scope">
                <span class="nowrap">{{ formatDate(scope.row.expectEndTime) }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="actualEndTime" label="实际结束时间" width="110">
              <template #default="scope">
                <span class="nowrap">{{ formatDate(scope.row.actualEndTime) }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="80">
              <template #default="scope">
                <el-tag :type="getStatusType(scope.row.status)" size="small">
                  {{ getStatusName(scope.row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="needAudit" label="需要审核" width="80">
              <template #default="scope">
                <el-tag :type="scope.row.needAudit ? 'warning' : 'success'" size="small">
                  {{ scope.row.needAudit ? "是" : "否" }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="principalId" label="负责人ID" width="100" />
            <el-table-column label="操作" width="150" fixed="right">
              <template #default="scope">
                <el-button size="small" type="primary" link @click="handleView(scope.row)">详情</el-button>
                <el-button v-permission="'project:edit'" size="small" type="primary" link @click="handleEdit(scope.row)">编辑</el-button>
                <el-button v-permission="'project:delete'" size="small" type="danger" link @click="handleDelete(scope.row)">删除</el-button>
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
            layout="total, sizes, prev, pager, next, jumper"
            :total="pagination.total"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>

        <!-- 新增/编辑项目弹窗 -->
        <el-dialog
          v-model="dialogVisible"
          :title="dialogTitle"
          width="600px"
          destroy-on-close
        >
          <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
            <el-form-item label="项目名称" prop="projectName">
              <el-input v-model="form.projectName" placeholder="请输入项目名称" />
            </el-form-item>
            <el-form-item label="企业ID" prop="companyId">
              <el-input v-model="form.companyId" placeholder="请输入企业ID" />
            </el-form-item>
            <el-form-item label="开始时间" prop="startTime">
              <el-date-picker
                v-model="form.startTime"
                type="date"
                placeholder="选择开始时间"
                format="YYYY/MM/DD"
                value-format="YYYY-MM-DD"
                style="width: 100%"
              />
            </el-form-item>
            <el-form-item label="预计结束时间" prop="expectEndTime">
              <el-date-picker
                v-model="form.expectEndTime"
                type="date"
                placeholder="选择预计结束时间"
                format="YYYY/MM/DD"
                value-format="YYYY-MM-DD"
                style="width: 100%"
              />
            </el-form-item>
            <el-form-item label="实际结束时间" prop="actualEndTime">
              <el-date-picker
                v-model="form.actualEndTime"
                type="date"
                placeholder="选择实际结束时间"
                format="YYYY/MM/DD"
                value-format="YYYY-MM-DD"
                style="width: 100%"
              />
            </el-form-item>
            <el-form-item label="项目状态" prop="status">
              <el-select v-model="form.status" placeholder="请选择状态" style="width: 100%">
                <el-option label="未开始" :value="0" />
                <el-option label="进行中" :value="1" />
                <el-option label="已完成" :value="2" />
                <el-option label="终止" :value="3" />
              </el-select>
            </el-form-item>
            <el-form-item label="需要审核" prop="needAudit">
              <el-switch v-model="form.needAudit" />
            </el-form-item>
            <el-form-item label="负责人ID" prop="principalId">
              <el-input v-model="form.principalId" placeholder="请输入负责人ID" />
            </el-form-item>
            <el-form-item label="备注" prop="note">
              <el-input v-model="form.note" type="textarea" :rows="3" placeholder="请输入备注" />
            </el-form-item>
          </el-form>
          <template #footer>
            <div class="dialog-footer">
              <el-button @click="dialogVisible = false">取消</el-button>
              <el-button type="primary" @click="handleSubmit" :loading="submitting">确定</el-button>
            </div>
          </template>
        </el-dialog>

        <!-- 项目详情对话框 -->
        <el-dialog v-model="detailDialogVisible" title="项目详情" width="600px" destroy-on-close>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="项目编号">{{ currentProject?.uuid }}</el-descriptions-item>
            <el-descriptions-item label="项目名称">{{ currentProject?.projectName }}</el-descriptions-item>
            <el-descriptions-item label="企业ID">{{ currentProject?.companyId }}</el-descriptions-item>
            <el-descriptions-item label="开始时间">{{ formatDate(currentProject?.startTime) }}</el-descriptions-item>
            <el-descriptions-item label="预计结束时间">{{ formatDate(currentProject?.expectEndTime) }}</el-descriptions-item>
            <el-descriptions-item label="实际结束时间">{{ formatDate(currentProject?.actualEndTime) || "未结束" }}</el-descriptions-item>
            <el-descriptions-item label="项目状态">
              <el-tag :type="getStatusType(currentProject?.status || 0)">
                {{ getStatusName(currentProject?.status || 0) }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="需要审核">
              <el-tag :type="currentProject?.needAudit ? 'warning' : 'success'">
                {{ currentProject?.needAudit ? "是" : "否" }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="负责人ID">{{ currentProject?.principalId }}</el-descriptions-item>
            <el-descriptions-item label="创建时间">{{ formatDate(currentProject?.createTime) }}</el-descriptions-item>
            <el-descriptions-item label="备注" :span="2">{{ currentProject?.note || "无" }}</el-descriptions-item>
          </el-descriptions>
          <template #footer>
            <div class="dialog-footer">
              <el-button @click="detailDialogVisible = false">关闭</el-button>
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
import { Plus, Delete, Download } from "@element-plus/icons-vue";
import PageLayout from "@/components/PageLayout.vue";
import api from "@/api";

// ==================== 类型定义 ====================
interface Project {
  id: number;
  uuid: string;
  companyId: number;
  projectName: string;
  startTime: string;
  endTime?: string;
  status: number;
  managerId: number;
  createTime: string;
  note?: string;
  // 前端展示用
  expectEndTime?: string;
  actualEndTime?: string;
  needAudit?: boolean;
  principalId?: string;
}

// ==================== 响应式数据 ====================
const searchForm = reactive({
  projectName: "",
  companyId: "",
  dateRange: [] as string[],
  status: null as number | null,
});

const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0,
});

const projects = ref<Project[]>([]);
const selectedProjects = ref<Project[]>([]);
const loading = ref(false);
const dialogVisible = ref(false);
const detailDialogVisible = ref(false);
const dialogTitle = ref("");
const submitting = ref(false);
const formRef = ref();
const currentProject = ref<Project | null>(null);

const form = reactive<Partial<Project>>({
  projectName: "",
  companyId: "",
  startTime: "",
  expectEndTime: "",
  actualEndTime: "",
  status: 0,
  needAudit: true,
  principalId: "",
  note: "",
});

const rules = {
  projectName: [{ required: true, message: "请输入项目名称", trigger: "blur" }],
  companyId: [{ required: true, message: "请输入企业ID", trigger: "blur" }],
  startTime: [{ required: true, message: "请选择开始时间", trigger: "change" }],
  expectEndTime: [{ required: true, message: "请选择预计结束时间", trigger: "change" }],
  status: [{ required: true, message: "请选择项目状态", trigger: "change" }],
  principalId: [{ required: true, message: "请输入负责人ID", trigger: "blur" }],
};

// ==================== 辅助方法 ====================
const formatDate = (dateTime: string | undefined): string => {
  if (!dateTime) return "-";
  const dateStr = String(dateTime);
  const datePart = dateStr.split(" ")[0];
  if (!datePart) return "-";
  return datePart.replace(/-/g, "/");
};

const getStatusType = (status: number) => {
  switch (status) {
    case 0: return "info";
    case 1: return "primary";
    case 2: return "success";
    case 3: return "danger";
    default: return "info";
  }
};

const getStatusName = (status: number) => {
  switch (status) {
    case 0: return "未开始";
    case 1: return "进行中";
    case 2: return "已完成";
    case 3: return "终止";
    default: return "未知";
  }
};

// ==================== API 调用 ====================
const getProjectList = async () => {
  loading.value = true;
  try {
    const params: any = { pageNum: pagination.currentPage, pageSize: pagination.pageSize };
    if (searchForm.projectName) params.keyword = searchForm.projectName;
    if (searchForm.companyId) params.companyId = Number(searchForm.companyId);
    if (searchForm.status !== null && searchForm.status !== undefined) params.status = searchForm.status;
    if (searchForm.dateRange && searchForm.dateRange.length === 2) {
      params.startDate = searchForm.dateRange[0];
      params.endDate = searchForm.dateRange[1];
    }
    const res = await api.project.getList(params);
    const list = (res.data?.records ?? []).map((item: any) => ({
      ...item,
      expectEndTime: item.endTime,
      principalId: item.managerId,
    }));
    projects.value = list;
    pagination.total = res.data?.total ?? 0;
  } catch (error) {
    console.error("获取项目列表失败:", error);
    ElMessage.error("获取项目列表失败");
  } finally {
    loading.value = false;
  }
};

// ==================== 页面交互 ====================
const handleSearch = () => {
  pagination.currentPage = 1;
  getProjectList();
};

const resetSearch = () => {
  searchForm.projectName = "";
  searchForm.companyId = "";
  searchForm.dateRange = [];
  searchForm.status = null;
  pagination.currentPage = 1;
  getProjectList();
};

const handleSizeChange = (size: number) => {
  pagination.pageSize = size;
  pagination.currentPage = 1;
  getProjectList();
};

const handleCurrentChange = (page: number) => {
  pagination.currentPage = page;
  getProjectList();
};

const handleSelectionChange = (selection: Project[]) => {
  selectedProjects.value = selection;
};

const handleAdd = () => {
  dialogTitle.value = "新增项目";
  dialogVisible.value = true;
  resetForm();
};

const handleView = (row: Project) => {
  currentProject.value = row;
  detailDialogVisible.value = true;
};

const handleEdit = (row: Project) => {
  dialogTitle.value = "编辑项目";
  dialogVisible.value = true;
  Object.assign(form, row);
};

const handleDelete = async (row: Project) => {
  try {
    await ElMessageBox.confirm(`确定要删除项目"${row.projectName}"吗？`, "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    });
    
    const res = await api.project.delete(row.uuid);
    if (res.data) {
      ElMessage.success("删除成功");
      getProjectList();
    } else {
      ElMessage.error("删除失败");
    }
  } catch (error) {
    if (error !== "cancel") {
      console.error("删除项目失败:", error);
      ElMessage.error("删除项目失败");
    }
  }
};

const handleBatchDelete = async () => {
  if (selectedProjects.value.length === 0) {
    ElMessage.warning("请选择要删除的项目");
    return;
  }
  
  try {
    await ElMessageBox.confirm(`确定要删除选中的${selectedProjects.value.length}个项目吗？`, "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    });
    
    for (const p of selectedProjects.value) {
      await api.project.delete(p.uuid);
    }
    ElMessage.success("批量删除成功");
    getProjectList();
  } catch (error) {
    if (error !== "cancel") {
      console.error("批量删除项目失败:", error);
      ElMessage.error("批量删除项目失败");
    }
  }
};

const handleExport = () => {
  ElMessage.info("导出功能开发中...");
};

const handleSubmit = async () => {
  if (!formRef.value) return;
  
  try {
    await formRef.value.validate();
    submitting.value = true;
    
    if (form.uuid) {
      const res = await api.project.update(form.uuid, {
        projectName: form.projectName,
        endTime: form.expectEndTime,
        status: form.status,
        note: form.note,
      });
      if (res.data) {
        ElMessage.success("更新成功");
      } else {
        ElMessage.error("更新失败");
        return;
      }
    } else {
      const res = await api.project.create({
        companyId: Number(form.companyId),
        projectName: form.projectName || "",
        startTime: form.startTime,
        endTime: form.expectEndTime,
        managerId: Number(form.principalId) || undefined,
        status: form.status || 0,
        note: form.note,
      });
      if (res.data) {
        ElMessage.success("添加成功");
      } else {
        ElMessage.error("添加失败");
        return;
      }
    }
    
    dialogVisible.value = false;
    getProjectList();
  } catch (error) {
    console.error("提交失败:", error);
    ElMessage.error("提交失败");
  } finally {
    submitting.value = false;
  }
};

const resetForm = () => {
  if (formRef.value) formRef.value.resetFields();
  form.id = undefined;
  form.uuid = undefined;
  form.companyId = "";
  form.projectName = "";
  form.startTime = "";
  form.expectEndTime = "";
  form.actualEndTime = "";
  form.status = 0;
  form.needAudit = true;
  form.principalId = "";
  form.note = "";
};

onMounted(() => {
  getProjectList();
});
</script>

<style scoped>
.project-management {
  height: 100%;
}

.dialog-footer {
  text-align: right;
}
</style>