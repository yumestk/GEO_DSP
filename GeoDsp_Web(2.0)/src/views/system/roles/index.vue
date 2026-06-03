<!--
  角色管理页面
  功能：角色的增删改查，支持角色名称/编码搜索、状态筛选
  接口：/api/system/roles/*
-->
<template>
  <PageLayout>
    <div class="role-management">
      <!-- Gfast 风格：一个白色盒子 -->
      <div class="g-card">
        <!-- 搜索栏 -->
        <div class="search-bar">
          <div class="search-group">
            <el-input
              v-model="searchKeyword"
              placeholder="角色名称/编码"
              style="width: 200px"
              clearable
              @clear="handleSearch"
              @keyup.enter="handleSearch"
            />
            <el-select
              v-model="statusFilter"
              placeholder="状态"
              style="width: 100px"
              clearable
              @change="handleSearch"
            >
              <el-option label="启用" :value="1" />
              <el-option label="禁用" :value="0" />
            </el-select>
          </div>
          <el-button type="primary" @click="handleSearch">查询</el-button>
        </div>

        <!-- 操作按钮栏 -->
        <div class="action-bar">
          <el-button v-permission="'role:add'" type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            添加角色
          </el-button>
        </div>

        <!-- 角色列表表格 -->
        <div class="table-wrapper">
          <!-- 注意：表格直接绑定 roleList，分页由后端控制 -->
          <el-table :data="roleList" stripe v-loading="loading">
            <el-table-column prop="roleName" label="角色名称" min-width="120" />
            <el-table-column prop="roleCode" label="角色编码" min-width="120" />
            <el-table-column prop="roleDesc" label="角色描述" min-width="180" />
            <el-table-column label="状态" width="80">
              <template #default="scope">
                <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
                  {{ scope.row.status === 1 ? "启用" : "禁用" }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="创建时间" width="170">
              <template #default="scope">
                <span class="nowrap">{{ formatDateTime(scope.row.createTime) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="150" fixed="right">
              <template #default="scope">
                <el-button v-permission="'role:edit'" size="small" @click="handleEdit(scope.row)">编辑</el-button>
                <el-button v-permission="'role:delete'" size="small" type="danger" @click="handleDelete(scope.row)">删除</el-button>
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

        <!-- 新增/编辑角色弹窗 -->
        <el-dialog
          v-model="dialogVisible"
          :title="dialogTitle"
          width="500px"
          @close="handleDialogClose"
        >
          <el-form
            ref="roleFormRef"
            :model="roleForm"
            :rules="roleRules"
            label-width="80px"
          >
            <el-form-item label="角色名称" prop="roleName">
              <el-input v-model="roleForm.roleName" placeholder="请输入角色名称" />
            </el-form-item>
            <el-form-item label="角色编码" prop="roleCode">
              <el-input v-model="roleForm.roleCode" placeholder="请输入角色编码" />
            </el-form-item>
            <el-form-item label="角色描述" prop="roleDesc">
              <el-input v-model="roleForm.roleDesc" type="textarea" :rows="3" placeholder="请输入角色描述" />
            </el-form-item>
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="roleForm.status">
                <el-radio :label="1">启用</el-radio>
                <el-radio :label="0">禁用</el-radio>
              </el-radio-group>
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
 * 角色管理页面 - 联调版本
 * 接口：api.role (getList / create / update / delete)
 * 分页由后端控制，前端直接展示后端返回的分页数据
 */
import { ref, reactive, computed, onMounted } from "vue";
import PageLayout from "@/components/PageLayout.vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { Plus } from "@element-plus/icons-vue";
import type { FormInstance, FormRules } from "element-plus";
import api from "@/api";

// ==================== 类型定义 ====================
interface Role {
  id: number;
  uuid: string;
  roleName: string;
  roleCode: string;
  roleDesc?: string;
  createTime: string;
  status: number;
}

// ==================== 响应式数据 ====================
const roleList = ref<Role[]>([]);
const loading = ref(false);
const searchKeyword = ref("");
const statusFilter = ref<number | null>(null);
const dialogVisible = ref(false);
const isEdit = ref(false);
const roleFormRef = ref<FormInstance>();

// 分页参数（与后端交互）
const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0,
});

// 表单数据
const roleForm = reactive({
  id: 0,
  uuid: "",
  roleName: "",
  roleCode: "",
  roleDesc: "",
  status: 1,
});

// 表单校验规则
const roleRules: FormRules = {
  roleName: [{ required: true, message: "请输入角色名称", trigger: "blur" }],
  roleCode: [{ required: true, message: "请输入角色编码", trigger: "blur" }],
};

// ==================== 计算属性 ====================
const dialogTitle = computed(() => (isEdit.value ? "编辑角色" : "添加角色"));

// ==================== 辅助方法 ====================

/** 获取当前操作用户ID（实际应从 store 获取）*/
const getCurrentUserId = (): number => {
  // TODO: 从 userStore 获取
  return 1;
};

/** 格式化日期：YYYY-MM-DD HH:mm:ss -> YYYY/MM/DD HH:mm:ss */
const formatDateTime = (dateTime: string | undefined): string => {
  if (!dateTime) return "-";
  return dateTime.replace(/-/g, "/");
};

/** 重置表单 */
const resetForm = () => {
  if (roleFormRef.value) roleFormRef.value.resetFields();
  roleForm.id = 0;
  roleForm.uuid = "";
  roleForm.roleName = "";
  roleForm.roleCode = "";
  roleForm.roleDesc = "";
  roleForm.status = 1;
  isEdit.value = false;
};

// ==================== API 调用 ====================

/** 获取角色列表（后端分页） */
const fetchRoleList = async () => {
  loading.value = true;
  try {
    // 构建查询参数
    const params: any = {
      pageNum: pagination.currentPage,
      pageSize: pagination.pageSize,
    };
    if (searchKeyword.value) params.keyword = searchKeyword.value;
    if (statusFilter.value !== null) params.status = statusFilter.value;

    const response = await api.role.getList(params) as any;
    if (response.code === 200) {
      roleList.value = response.data.records || [];
      pagination.total = response.data.total || 0;
    } else {
      ElMessage.error(response.msg || "获取角色列表失败");
      roleList.value = [];
      pagination.total = 0;
    }
  } catch (error) {
    console.error("获取角色列表错误:", error);
    ElMessage.error("获取角色列表失败");
    roleList.value = [];
    pagination.total = 0;
  } finally {
    loading.value = false;
  }
};

// ==================== 页面交互 ====================

/** 搜索 */
const handleSearch = () => {
  pagination.currentPage = 1;
  fetchRoleList();
};

/** 每页条数变化 */
const handleSizeChange = (size: number) => {
  pagination.pageSize = size;
  pagination.currentPage = 1;
  fetchRoleList();
};

/** 页码变化 */
const handleCurrentChange = (page: number) => {
  pagination.currentPage = page;
  fetchRoleList();
};

/** 打开新增弹窗 */
const handleAdd = () => {
  isEdit.value = false;
  resetForm();
  dialogVisible.value = true;
};

/** 打开编辑弹窗 */
const handleEdit = (row: Role) => {
  isEdit.value = true;
  // 注意：使用 Object.assign 进行浅拷贝，不影响原数据
  Object.assign(roleForm, { ...row });
  dialogVisible.value = true;
};

/** 删除角色（单条） */
const handleDelete = async (row: Role) => {
  try {
    await ElMessageBox.confirm(`确定删除角色 "${row.roleName}" 吗？`, "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    });
    
    const response = await api.role.delete(row.uuid) as any;
    if (response.code === 200) {
      ElMessage.success(response.msg || "删除成功");
      // 删除后刷新列表
      await fetchRoleList();
    } else {
      ElMessage.error(response.msg || "删除失败");
    }
  } catch (error) {
    if (error !== "cancel") {
      console.error("删除角色错误:", error);
      ElMessage.error("删除失败");
    }
  }
};

/** 提交表单（新增/编辑） */
const handleSubmit = async () => {
  if (!roleFormRef.value) return;
  
  try {
    await roleFormRef.value.validate();
    const currentUserId = getCurrentUserId();
    
    if (isEdit.value) {
      // 编辑角色
      const response = await api.role.update({
        uuid: roleForm.uuid,
        roleName: roleForm.roleName,
        roleCode: roleForm.roleCode,
        roleDesc: roleForm.roleDesc,
        status: roleForm.status,
      }, currentUserId) as any;
      
      if (response.code === 200) {
        ElMessage.success(response.msg || "修改成功");
        dialogVisible.value = false;
        await fetchRoleList();
      } else {
        ElMessage.error(response.msg || "修改失败");
      }
    } else {
      // 新增角色
      const response = await api.role.create({
        roleName: roleForm.roleName,
        roleCode: roleForm.roleCode,
        roleDesc: roleForm.roleDesc,
        status: roleForm.status,
      }, currentUserId) as any;
      
      if (response.code === 200) {
        ElMessage.success(response.msg || "添加成功");
        dialogVisible.value = false;
        await fetchRoleList();
      } else {
        ElMessage.error(response.msg || "添加失败");
      }
    }
  } catch (error) {
    console.error("提交表单错误:", error);
    ElMessage.error("操作失败");
  }
};

/** 弹窗关闭时重置表单 */
const handleDialogClose = () => {
  resetForm();
};

// ==================== 生命周期 ====================
onMounted(() => {
  fetchRoleList();
});
</script>

<style scoped>
.role-management {
  height: 100%;
}

/* 时间不换行 */
.nowrap {
  white-space: nowrap;
}
</style>