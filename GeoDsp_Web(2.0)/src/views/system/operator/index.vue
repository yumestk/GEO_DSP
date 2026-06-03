<!--
  操作员管理页面
  功能：操作员的增删改查，支持姓名/手机号搜索、日期范围筛选、状态筛选
  接口：/api/operator/*
-->
<template>
  <PageLayout>
    <div class="operator-management">
      <!-- Gfast 风格：一个白色盒子 -->
      <div class="g-card">
        <!-- 搜索栏 -->
        <div class="search-bar">
          <div class="search-group">
            <el-input
              v-model="searchKeyword"
              placeholder="操作员姓名/手机号"
              style="width: 200px"
              clearable
              @clear="handleSearch"
              @keyup.enter="handleSearch"
            />
            <div class="date-range">
              <el-date-picker
                v-model="startDate"
                type="date"
                placeholder="开始日期"
                style="width: 130px"
                value-format="YYYY-MM-DD"
                @change="handleSearch"
              />
              <span>至</span>
              <el-date-picker
                v-model="endDate"
                type="date"
                placeholder="结束日期"
                style="width: 130px"
                value-format="YYYY-MM-DD"
                @change="handleSearch"
              />
            </div>
            <el-select
              v-model="statusFilter"
              placeholder="状态"
              style="width: 100px"
              clearable
              @change="handleSearch"
            >
              <el-option label="工作中" :value="1" />
              <el-option label="未工作" :value="0" />
            </el-select>
          </div>
          <el-button type="primary" @click="handleSearch">查询</el-button>
        </div>

        <!-- 操作按钮栏 -->
        <div class="action-bar">
          <el-button v-permission="'operator:add'" type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            添加操作员
          </el-button>
          <el-button v-permission="'operator:import'" @click="handleImport">
            <el-icon><Upload /></el-icon>
            文件导入
          </el-button>
          <el-button v-permission="'operator:import'" @click="handleDownloadTemplate">
            <el-icon><Download /></el-icon>
            模板下载
          </el-button>
          <el-button v-permission="'operator:import'" @click="handleSystemImport">
            <el-icon><Refresh /></el-icon>
            系统导入
          </el-button>
          <el-button
            v-permission="'operator:delete'"
            type="danger"
            :disabled="selectedRows.length === 0"
            @click="handleBatchDelete"
          >
            <el-icon><Delete /></el-icon>
            批量删除
          </el-button>
        </div>

        <!-- 操作员列表表格 -->
        <div class="table-wrapper">
          <!-- 注意：表格直接绑定 operatorList，分页由后端控制 -->
          <el-table 
            :data="operatorList" 
            stripe 
            v-loading="loading"
            @selection-change="handleSelectionChange"
          >
            <el-table-column type="selection" width="55" />
            <el-table-column prop="operatorName" label="操作员姓名" min-width="120" />
            <el-table-column prop="phoneNum" label="手机号" width="150">
              <template #default="scope">
                <div class="phone-cell">
                  <span class="phone-text">{{ formatPhoneNumber(scope.row.phoneNum, scope.row.showPhone) }}</span>
                  <el-icon 
                    class="phone-icon"
                    @click="togglePhoneVisible(scope.row)"
                  >
                    <View v-if="!scope.row.showPhone" />
                    <Hide v-else />
                  </el-icon>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="状态" width="80">
              <template #default="scope">
                <el-tag :type="scope.row.status === 1 ? 'success' : 'info'">
                  {{ scope.row.status === 1 ? "工作中" : "未工作" }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="startTime" label="有效开始时间" width="170">
              <template #default="scope">
                <span class="nowrap">{{ formatDateTime(scope.row.startTime) }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="stopTime" label="有效结束时间" width="170">
              <template #default="scope">
                <span class="nowrap">{{ formatDateTime(scope.row.stopTime) }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="note" label="备注" min-width="120" />
            <el-table-column prop="createTime" label="创建时间" width="170">
              <template #default="scope">
                <span class="nowrap">{{ formatDateTime(scope.row.createTime) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="150" fixed="right">
              <template #default="scope">
                <el-button v-permission="'operator:edit'" size="small" @click="handleEdit(scope.row)">编辑</el-button>
                <el-button v-permission="'operator:delete'" size="small" type="danger" @click="handleDelete(scope.row)">删除</el-button>
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
            layout="total, prev, pager, next, sizes, jumper"
            :total="pagination.total"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>

        <!-- 新增/编辑操作员弹窗 -->
        <el-dialog
          v-model="dialogVisible"
          :title="dialogTitle"
          width="500px"
          @close="handleDialogClose"
        >
          <el-form
            ref="formRef"
            :model="formData"
            :rules="formRules"
            label-width="100px"
          >
            <el-form-item label="操作员姓名" prop="operatorName">
              <el-input v-model="formData.operatorName" placeholder="请输入操作员姓名" />
            </el-form-item>
            <el-form-item label="手机号" prop="phoneNum">
              <el-input v-model="formData.phoneNum" placeholder="请输入手机号码" />
            </el-form-item>
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="formData.status">
                <el-radio :label="1">工作中</el-radio>
                <el-radio :label="0">未工作</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="有效开始时间" prop="startTime">
              <el-date-picker
                v-model="formData.startTime"
                type="date"
                placeholder="选择开始时间"
                style="width: 100%"
                value-format="YYYY-MM-DD"
              />
            </el-form-item>
            <el-form-item label="有效结束时间" prop="stopTime">
              <el-date-picker
                v-model="formData.stopTime"
                type="date"
                placeholder="选择结束时间"
                style="width: 100%"
                value-format="YYYY-MM-DD"
              />
            </el-form-item>
            <el-form-item label="备注" prop="note">
              <el-input v-model="formData.note" type="textarea" :rows="3" placeholder="请输入备注" />
            </el-form-item>
          </el-form>
          <template #footer>
            <el-button @click="dialogVisible = false">取消</el-button>
            <el-button type="primary" @click="handleSubmit">确定</el-button>
          </template>
        </el-dialog>
      </div>
    </div>
  </PageLayout>
</template>

<script setup lang="ts">
/**
 * 操作员管理页面 - 联调版本
 * 接口对应：api.operator (getList / add / update / delete)
 * 分页由后端控制，前端直接展示后端返回的分页数据
 */
import { ref, reactive, computed, onMounted } from "vue";
import PageLayout from "@/components/PageLayout.vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { Plus, Upload, Download, Refresh, Delete, View, Hide } from "@element-plus/icons-vue";
import type { FormInstance, FormRules } from "element-plus";
import api from "@/api";

// ==================== 类型定义 ====================
interface Operator {
  id: number;
  uuid: string;
  companyId: number;
  userId?: number;
  operatorName: string;
  phoneNum?: string;
  status: number;
  startTime: string;
  stopTime: string;
  note?: string;
  createBy?: number;
  createTime: string;
  updateBy?: number;
  updateTime?: string;
  isDel?: boolean;
  showPhone?: boolean;
}

// ==================== 响应式数据 ====================
const operatorList = ref<Operator[]>([]);
const loading = ref(false);
const searchKeyword = ref("");
const statusFilter = ref<number | null>(null);
const startDate = ref("");
const endDate = ref("");
const dialogVisible = ref(false);
const isEdit = ref(false);
const editUuid = ref("");
const formRef = ref<FormInstance>();
const selectedRows = ref<Operator[]>([]);

// 分页参数（与后端交互）
const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0,
});

const formData = reactive({
  operatorName: "",
  phoneNum: "",
  status: 1,
  startTime: "",
  stopTime: "",
  note: "",
});

const formRules: FormRules = {
  operatorName: [{ required: true, message: "请输入操作员姓名", trigger: "blur" }],
  phoneNum: [
    { required: true, message: "请输入手机号码", trigger: "blur" },
    { pattern: /^1[3-9]\d{9}$/, message: "请输入正确的手机号码", trigger: "blur" }
  ],
  startTime: [{ required: true, message: "请选择有效开始时间", trigger: "change" }],
  stopTime: [{ required: true, message: "请选择有效结束时间", trigger: "change" }],
};

// ==================== 计算属性 ====================
const dialogTitle = computed(() => (isEdit.value ? "编辑操作员" : "添加操作员"));

// ==================== 辅助方法 ====================
const getCurrentUserId = (): number => 1;

const formatDateTime = (dateTime: string | undefined): string => {
  if (!dateTime) return "-";
  return dateTime.replace(/-/g, "/");
};

const formatPhoneNumber = (phone: string | undefined, showFull: boolean = false): string => {
  if (!phone) return "-";
  if (showFull) return phone;
  return phone.replace(/(\d{3})\d{4}(\d{4})/, "$1****$2");
};

const togglePhoneVisible = (row: Operator) => {
  row.showPhone = !row.showPhone;
};

const resetForm = () => {
  if (formRef.value) formRef.value.resetFields();
  formData.operatorName = "";
  formData.phoneNum = "";
  formData.status = 1;
  formData.startTime = "";
  formData.stopTime = "";
  formData.note = "";
  isEdit.value = false;
  editUuid.value = "";
};

const handleSelectionChange = (rows: Operator[]) => {
  selectedRows.value = rows;
};

// ==================== API 调用 ====================

/** 获取操作员列表（后端分页） */
const fetchOperatorList = async () => {
  loading.value = true;
  try {
    const params: any = {
      pageNum: pagination.currentPage,
      pageSize: pagination.pageSize,
    };
    if (searchKeyword.value) {
      params.operatorName = searchKeyword.value;
      params.phoneNum = searchKeyword.value;
    }
    if (statusFilter.value !== null) params.status = statusFilter.value;
    if (startDate.value) params.startDate = startDate.value;
    if (endDate.value) params.endDate = endDate.value;

    const response = await api.operator.getList(params) as any;
    if (response.code === 200) {
      operatorList.value = response.data.records.map((item: Operator) => ({ ...item, showPhone: false }));
      pagination.total = response.data.total || 0;
    } else {
      ElMessage.error(response.msg || "获取操作员列表失败");
      operatorList.value = [];
      pagination.total = 0;
    }
  } catch (error) {
    console.error("获取操作员列表错误:", error);
    ElMessage.error("获取操作员列表失败");
    operatorList.value = [];
    pagination.total = 0;
  } finally {
    loading.value = false;
  }
};

/** 批量删除（调用通用批量操作接口） */
const handleBatchDelete = async () => {
  if (selectedRows.value.length === 0) return;
  
  try {
    await ElMessageBox.confirm(`确定删除选中的 ${selectedRows.value.length} 个操作员吗？`, "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    });
    
    const uuids = selectedRows.value.map(row => row.uuid);
    const currentUserId = getCurrentUserId();
    const response = await api.operator.batchOperation(uuids, 'delete', currentUserId) as any;
    if (response.code === 200) {
      ElMessage.success(response.msg || "批量删除成功");
      selectedRows.value = [];
      await fetchOperatorList();
    } else {
      ElMessage.error(response.msg || "批量删除失败");
    }
  } catch (error) {
    if (error !== "cancel") {
      console.error("批量删除错误:", error);
      ElMessage.error("批量删除失败，请检查网络连接");
    }
  }
};

// ==================== 页面交互 ====================
const handleSearch = () => { 
  pagination.currentPage = 1;
  fetchOperatorList();
};

const handleSizeChange = (size: number) => { 
  pagination.pageSize = size;
  pagination.currentPage = 1;
  fetchOperatorList();
};

const handleCurrentChange = (page: number) => { 
  pagination.currentPage = page;
  fetchOperatorList();
};

const handleAdd = () => {
  resetForm();
  dialogVisible.value = true;
};

const handleEdit = (row: Operator) => {
  resetForm();
  isEdit.value = true;
  editUuid.value = row.uuid;
  formData.operatorName = row.operatorName ;
  formData.phoneNum = row.phoneNum || "";
  formData.status = row.status;
  // 只取日期部分
  formData.startTime = row.startTime.split(" ")[0];
  formData.stopTime = row.stopTime.split(" ")[0];
  formData.note = row.note || "";
  dialogVisible.value = true;
};

const handleDelete = async (row: Operator) => {
  try {
    await ElMessageBox.confirm(`确定删除操作员 "${row.operatorName}" 吗？`, "提示", {
      confirmButtonText: "确定", cancelButtonText: "取消", type: "warning",
    });
    
    const currentUserId = getCurrentUserId();
    const response = await api.operator.delete(row.uuid, currentUserId) as any;
    if (response.code === 200) {
      ElMessage.success(response.msg || "删除成功");
      await fetchOperatorList();
    } else {
      ElMessage.error(response.msg || "删除失败");
    }
  } catch (error) {
    if (error !== "cancel") {
      console.error("删除操作员错误:", error);
      ElMessage.error("删除失败，请检查网络连接");
    }
  }
};

const handleSubmit = async () => {
  if (!formRef.value) return;
  try {
    await formRef.value.validate();
    const currentUserId = getCurrentUserId();
    
    if (isEdit.value) {
      const response = await api.operator.update({
        uuid: editUuid.value,
        operatorName: formData.operatorName,
        phoneNum: formData.phoneNum,
        status: formData.status,
        startTime: formData.startTime,
        stopTime: formData.stopTime,
        note: formData.note,
      }, currentUserId) as any;
      
      if (response.code === 200) {
        ElMessage.success(response.msg || "修改成功");
        dialogVisible.value = false;
        await fetchOperatorList();
      } else {
        ElMessage.error(response.msg || "修改失败");
      }
    } else {
      const response = await api.operator.add({
        operatorName: formData.operatorName,
        phoneNum: formData.phoneNum,
        status: formData.status,
        startTime: formData.startTime,
        stopTime: formData.stopTime,
        note: formData.note,
      }, currentUserId) as any;
      
      if (response.code === 200) {
        ElMessage.success(response.msg || "添加成功");
        dialogVisible.value = false;
        await fetchOperatorList();
      } else {
        ElMessage.error(response.msg || "添加失败");
      }
    }
  } catch (error) {
    console.error("提交表单错误:", error);
    ElMessage.error("操作失败");
  }
};

const handleDialogClose = () => { 
  resetForm(); 
};

// 导入功能（待开发）
const handleImport = () => { 
  ElMessage.info("文件导入功能开发中"); 
};

const handleDownloadTemplate = () => { 
  ElMessage.info("模板下载功能开发中"); 
};

const handleSystemImport = () => { 
  ElMessage.info("系统导入功能开发中"); 
};

// ==================== 生命周期 ====================
onMounted(() => {
  fetchOperatorList();
});
</script>

<style scoped>
.operator-management {
  height: 100%;
}

/* 手机号单元格：文字和图标同一行，不换行 */
.phone-cell {
  display: flex;
  align-items: center;
  white-space: nowrap;
}

.phone-text {
  flex-shrink: 0;
}

.phone-icon {
  margin-left: 6px;
  cursor: pointer;
  flex-shrink: 0;
  font-size: 14px;
  color: #909399;
  transition: color 0.2s;
}

.phone-icon:hover {
  color: #409eff;
}

/* 时间不换行 */
.nowrap {
  white-space: nowrap;
}
</style>