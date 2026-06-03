<template>
  <PageLayout>
    <div class="methods-management">
      <!-- Gfast 风格：一个白色盒子 -->
      <div class="g-card">
        <!-- 搜索栏 -->
        <div class="search-bar">
          <div class="search-group">
            <el-input
              v-model="searchKeyword"
              placeholder="方法名称"
              style="width: 220px"
              clearable
              @clear="handleSearch"
              @keyup.enter="handleSearch"
            />
          </div>
          <el-button type="primary" @click="handleSearch">查询</el-button>
        </div>

        <!-- 操作按钮栏 -->
        <div class="action-bar">
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            添加方法
          </el-button>
        </div>

        <!-- 作业方法列表表格 -->
        <div class="table-wrapper">
          <el-table :data="displayList" stripe v-loading="loading" style="width: 100%">
            <el-table-column prop="methodName" label="方法名称" min-width="150" />
            <el-table-column prop="methodDesc" label="方法描述" min-width="250" show-overflow-tooltip />
            <el-table-column prop="createTime" label="创建时间" width="170">
              <template #default="scope">
                <span class="nowrap">{{ formatDateTime(scope.row.createTime) }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="updateTime" label="修改时间" width="170">
              <template #default="scope">
                <span class="nowrap">{{ formatDateTime(scope.row.updateTime) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="150" fixed="right">
              <template #default="scope">
                <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
                <el-button size="small" type="danger" @click="handleDelete(scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <!-- 分页组件 -->
        <div class="pagination-footer">
          <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :page-sizes="[10, 20, 50, 100]"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>

        <!-- 新增/编辑作业方法弹窗 -->
        <el-dialog
          v-model="dialogVisible"
          :title="dialogTitle"
          width="500px"
          @close="handleDialogClose"
        >
          <el-form
            ref="methodFormRef"
            :model="methodForm"
            :rules="methodRules"
            label-width="80px"
          >
            <el-form-item label="方法名称" prop="methodName">
              <el-input v-model="methodForm.methodName" placeholder="请输入方法名称" />
            </el-form-item>
            <el-form-item label="方法描述" prop="methodDesc">
              <el-input v-model="methodForm.methodDesc" type="textarea" :rows="4" placeholder="请输入方法描述" />
            </el-form-item>
            <el-form-item label="备注" prop="note">
              <el-input v-model="methodForm.note" type="textarea" :rows="3" placeholder="请输入备注" />
            </el-form-item>
          </el-form>
          <template #footer>
            <div class="dialog-footer">
              <el-button @click="dialogVisible = false">取消</el-button>
              <el-button type="primary" @click="handleSubmit">确定</el-button>
            </div>
          </template>
        </el-dialog>
      </div>
    </div>
  </PageLayout>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { Plus, Search } from "@element-plus/icons-vue";
import PageLayout from "@/components/PageLayout.vue";
import type { FormInstance, FormRules } from "element-plus";
import api from "@/api";

// ==================== 类型定义 ====================
interface Method {
  id: number;
  uuid: string;
  methodName: string;
  methodDesc?: string;
  createBy: number;
  createTime: string;
  updateBy?: number;
  updateTime?: string;
  isDel: boolean;
  note?: string;
}

// ==================== 响应式数据 ====================
const methodsList = ref<Method[]>([]);
const searchKeyword = ref("");
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(0);
const loading = ref(false);
const dialogVisible = ref(false);
const isEdit = ref(false);
const methodFormRef = ref<FormInstance>();

const methodForm = reactive<Method>({
  id: 0,
  uuid: "",
  methodName: "",
  methodDesc: "",
  createBy: 0,
  createTime: "",
  updateBy: 0,
  updateTime: "",
  isDel: false,
  note: "",
});

const methodRules: FormRules = {
  methodName: [
    { required: true, message: "请输入方法名称", trigger: "blur" },
    { min: 2, max: 100, message: "方法名称长度在 2 到 100 个字符", trigger: "blur" },
  ],
  methodDesc: [
    { required: true, message: "请输入方法描述", trigger: "blur" },
    { min: 5, max: 500, message: "方法描述长度在 5 到 500 个字符", trigger: "blur" },
  ],
};

// ==================== 计算属性 ====================
const dialogTitle = computed(() => (isEdit.value ? "编辑作业方法" : "添加作业方法"));

const displayList = computed(() => {
  let list = [...methodsList.value];
  if (searchKeyword.value) {
    const kw = searchKeyword.value.toLowerCase();
    list = list.filter((item) => item.methodName.toLowerCase().includes(kw));
  }
  total.value = list.length;
  const start = (currentPage.value - 1) * pageSize.value;
  return list.slice(start, start + pageSize.value);
});

// ==================== 辅助方法 ====================
const formatDateTime = (dateTime: string | undefined): string => {
  if (!dateTime) return "-";
  return dateTime.replace(/-/g, "/");
};

// ==================== Mock 数据 ====================
const mockMethods: Method[] = [
  {
    id: 1,
    uuid: "550e8400-e29b-41d4-a716-4466554601",
    methodName: "数据采集方法",
    methodDesc: "用于采集环境数据的标准方法，包括传感器部署、数据采集频率等",
    createBy: 1,
    createTime: "2024-01-15 10:30:00",
    updateBy: 1,
    updateTime: "2024-01-15 10:30:00",
    isDel: false,
    note: "基础数据采集方法",
  },
  {
    id: 2,
    uuid: "550e8400-e29b-41d4-a716-4466554602",
    methodName: "数据分析方法",
    methodDesc: "对采集到的数据进行统计分析，包括数据清洗、异常值处理、趋势分析等",
    createBy: 1,
    createTime: "2024-01-20 14:15:00",
    updateBy: 1,
    updateTime: "2024-01-20 14:15:00",
    isDel: false,
    note: "适用于大数据量分析",
  },
  {
    id: 3,
    uuid: "550e8400-e29b-41d4-a716-4466554603",
    methodName: "质量控制方法",
    methodDesc: "确保数据质量的控制方法，包括数据验证、完整性检查、一致性验证等",
    createBy: 1,
    createTime: "2024-02-05 09:45:00",
    updateBy: 1,
    updateTime: "2024-02-05 09:45:00",
    isDel: false,
    note: "质量控制标准符合ISO 9001要求",
  },
  {
    id: 4,
    uuid: "550e8400-e29b-41d4-a716-4466554604",
    methodName: "模型构建方法",
    methodDesc: "基于采集数据构建预测模型的方法，包括特征选择、模型训练、模型评估等",
    createBy: 1,
    createTime: "2024-02-10 16:20:00",
    updateBy: 1,
    updateTime: "2024-02-10 16:20:00",
    isDel: false,
    note: "支持机器学习和深度学习模型构建",
  },
  {
    id: 5,
    uuid: "550e8400-e29b-41d4-a716-4466554605",
    methodName: "结果可视化方法",
    methodDesc: "将分析结果进行可视化展示的方法，包括图表选择、颜色配置、交互设计等",
    createBy: 1,
    createTime: "2024-02-15 11:00:00",
    updateBy: 1,
    updateTime: "2024-02-15 11:00:00",
    isDel: false,
    note: "支持多种图表类型",
  },
];

// ==================== API 调用 ====================
const getMethodsList = async () => {
  loading.value = true;
  try {
    await new Promise((resolve) => setTimeout(resolve, 300));
    methodsList.value = [...mockMethods];
  } catch (error) {
    console.error("获取作业方法列表失败:", error);
    ElMessage.error("获取作业方法列表失败");
  } finally {
    loading.value = false;
  }
};

// ==================== 页面交互 ====================
const handleAdd = () => {
  isEdit.value = false;
  resetForm();
  dialogVisible.value = true;
};

const handleEdit = (row: Method) => {
  isEdit.value = true;
  Object.assign(methodForm, { ...row });
  dialogVisible.value = true;
};

const handleDelete = async (row: Method) => {
  try {
    await ElMessageBox.confirm(`确定要删除作业方法 "${row.methodName}" 吗？`, "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    });

    const index = mockMethods.findIndex((m) => m.id === row.id);
    if (index > -1) {
      mockMethods.splice(index, 1);
    }

    ElMessage.success("删除成功");
    getMethodsList();
  } catch (error) {
    if (error !== "cancel") {
      console.error("删除作业方法错误:", error);
      ElMessage.error("删除失败");
    }
  }
};

const handleSearch = () => {
  currentPage.value = 1;
};

const handleSizeChange = (size: number) => {
  pageSize.value = size;
  currentPage.value = 1;
};

const handleCurrentChange = (page: number) => {
  currentPage.value = page;
};

const handleSubmit = async () => {
  if (!methodFormRef.value) return;

  try {
    await methodFormRef.value.validate();

    if (isEdit.value) {
      const index = mockMethods.findIndex((m) => m.id === methodForm.id);
      if (index > -1) {
        mockMethods[index] = {
          ...methodForm,
          updateTime: new Date().toLocaleString(),
        };
      }
      ElMessage.success("修改成功");
    } else {
      const newMethod: Method = {
        ...methodForm,
        id: Math.max(...mockMethods.map(m => m.id), 0) + 1,
        uuid: `550e8400-e29b-41d4-a716-44665546${Math.max(...mockMethods.map(m => m.id), 0) + 100}`,
        createTime: new Date().toLocaleString(),
        updateTime: new Date().toLocaleString(),
        createBy: 1,
        isDel: false,
      };
      mockMethods.push(newMethod);
      ElMessage.success("添加成功");
    }
    dialogVisible.value = false;
    getMethodsList();
  } catch (error) {
    console.error("提交表单错误:", error);
    ElMessage.error("操作失败");
  }
};

const handleDialogClose = () => {
  resetForm();
};

const resetForm = () => {
  if (methodFormRef.value) {
    methodFormRef.value.resetFields();
  }
  Object.assign(methodForm, {
    id: 0,
    uuid: "",
    methodName: "",
    methodDesc: "",
    createBy: 0,
    createTime: "",
    updateBy: 0,
    updateTime: "",
    isDel: false,
    note: "",
  });
};

onMounted(() => {
  getMethodsList();
});
</script>

<style scoped>
.methods-management {
  height: 100%;
}

/* 对话框底部 */
.dialog-footer {
  text-align: right;
}
</style>