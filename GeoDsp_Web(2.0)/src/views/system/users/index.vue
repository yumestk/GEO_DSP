<!--
  用户管理页面
  功能：用户的增删改查，支持用户名搜索、状态切换、手机号加密显示
  接口：/api/user/*
-->
<template>
  <PageLayout>
    <div class="user-management">
      <!-- Gfast 风格：一个白色盒子 -->
      <div class="g-card">
        <!-- 搜索栏 -->
        <div class="search-bar">
          <div class="search-group">
            <el-input
              v-model="searchKeyword"
              placeholder="用户名/账号/手机号"
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
              <el-option label="正常" :value="1" />
              <el-option label="禁用" :value="0" />
            </el-select>
            <el-select
              v-model="sexFilter"
              placeholder="性别"
              style="width: 100px"
              clearable
              @change="handleSearch"
            >
              <el-option label="男" :value="1" />
              <el-option label="女" :value="0" />
            </el-select>
            <el-select
              v-model="roleFilter"
              placeholder="角色"
              style="width: 140px"
              clearable
              @change="handleSearch"
            >
              <el-option
                v-for="item in roleOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </div>
          <el-button type="primary" @click="handleSearch">查询</el-button>
        </div>

        <!-- 操作按钮栏 -->
        <div class="action-bar">
          <el-button v-permission="'user:add'" type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            添加用户
          </el-button>
          <el-button
            v-permission="'user:delete'"
            type="danger"
            :disabled="selectedRows.length === 0"
            @click="handleBatchDelete"
          >
            <el-icon><Delete /></el-icon>
            批量删除
          </el-button>
        </div>

        <!-- 用户列表表格 -->
        <div class="table-wrapper">
          <el-table
            :data="displayList"
            stripe
            v-loading="loading"
            @selection-change="handleSelectionChange"
          >
            <el-table-column type="selection" width="55" />
            <el-table-column prop="userName" label="用户名" min-width="100" />
            <el-table-column prop="account" label="账号" min-width="120" />
            <el-table-column prop="sex" label="性别" width="70">
              <template #default="scope">
                {{ scope.row.sex === 1 ? "男" : scope.row.sex === 0 ? "女" : "-" }}
              </template>
            </el-table-column>
            <el-table-column prop="email" label="邮箱" min-width="160" />
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
            <el-table-column prop="roleName" label="角色名称" min-width="100" />
            <el-table-column prop="createTime" label="创建时间" width="170">
              <template #default="scope">
                <span class="time-text">{{ formatDateTime(scope.row.createTime) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="状态" width="80">
              <template #default="scope">
                <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
                  {{ scope.row.status === 1 ? "正常" : "禁用" }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="200" fixed="right">
              <template #default="scope">
                <el-button v-permission="'user:edit'" size="small" @click="handleEdit(scope.row)">编辑</el-button>
                <el-button
                  v-permission="'user:status'"
                  size="small"
                  :type="scope.row.status === 1 ? 'warning' : 'success'"
                  @click="handleToggleStatus(scope.row)"
                >
                  {{ scope.row.status === 1 ? "禁用" : "启用" }}
                </el-button>
                <el-button v-permission="'user:delete'" size="small" type="danger" @click="handleDelete(scope.row)">
                  删除
                </el-button>
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

        <!-- 新增/编辑用户弹窗 -->
        <el-dialog
          v-model="dialogVisible"
          :title="dialogTitle"
          width="500px"
          @close="handleDialogClose"
        >
          <el-form
            ref="userFormRef"
            :model="userForm"
            :rules="userRules"
            label-width="80px"
          >
            <el-form-item label="用户名" prop="userName">
              <el-input v-model="userForm.userName" placeholder="请输入用户名" :disabled="isEdit" />
            </el-form-item>
            <el-form-item label="账号" prop="account">
              <el-input v-model="userForm.account" placeholder="请输入账号" :disabled="isEdit" />
            </el-form-item>
            <el-form-item label="密码" prop="password" v-if="!isEdit">
              <el-input v-model="userForm.password" type="password" placeholder="请输入密码" show-password />
            </el-form-item>
            <el-form-item label="性别" prop="sex">
              <el-radio-group v-model="userForm.sex">
                <el-radio :label="1">男</el-radio>
                <el-radio :label="0">女</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="userForm.email" placeholder="请输入邮箱" />
            </el-form-item>
            <el-form-item label="手机号" prop="phoneNum">
              <el-input v-model="userForm.phoneNum" placeholder="请输入手机号" />
            </el-form-item>
            <el-form-item label="角色名称" prop="roleName">
              <el-input v-model="userForm.roleName" placeholder="请输入角色名称" />
            </el-form-item>
            <el-form-item label="角色代码" prop="roleCode">
              <el-input v-model="userForm.roleCode" placeholder="请输入角色代码" />
            </el-form-item>
            <el-form-item label="角色描述" prop="roleDesc">
              <el-input v-model="userForm.roleDesc" type="textarea" :rows="3" placeholder="请输入角色描述" />
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
 * 用户管理页面 - 联调版本
 * 接口对应：api.user (getList / create / update / delete / toggleStatus / batchOperation)
 * 手机号支持加密/明文切换
 */
import { ref, reactive, computed, onMounted } from "vue";
import PageLayout from "@/components/PageLayout.vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { Plus, Delete, View, Hide } from "@element-plus/icons-vue";
import type { FormInstance, FormRules } from "element-plus";
import api from "@/api/index";
import { useUserStore } from "@/stores/modules/user";

// ==================== 类型定义 ====================
interface User {
  id: number;
  uuid: string;
  companyId?: number;
  userName: string;
  account: string;
  password?: string;
  sex?: number;
  phoneNum?: string;
  email?: string;
  status: number;
  imageId?: string;
  createTime: string;
  createBy?: number;
  updateBy?: number;
  updateTime?: string;
  isDel?: boolean;
  note?: string;
  roleName?: string;
  roleCode?: string;
  roleDesc?: string;
  showPhone?: boolean;
}

// ==================== 响应式数据 ====================
const userList = ref<User[]>([]);
const loading = ref(false);
const searchKeyword = ref("");
const statusFilter = ref<number | null>(null);
const sexFilter = ref<number | null>(null);
const roleFilter = ref<string | null>(null);
const dialogVisible = ref(false);
const isEdit = ref(false);
const userFormRef = ref<FormInstance>();
const selectedRows = ref<User[]>([]);

// 分页参数
const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0,
});

// 角色选项（从后端获取）
const roleOptions = ref<{ label: string; value: string }[]>([]);

// 表单数据
const userForm = reactive<User>({
  id: 0,
  uuid: "",
  userName: "",
  account: "",
  password: "",
  sex: 1,
  phoneNum: "",
  email: "",
  status: 1,
  imageId: "",
  createTime: "",
  createBy: 0,
  updateBy: 0,
  updateTime: "",
  isDel: false,
  note: "",
  companyId: 0,
  roleName: "",
  roleCode: "",
  roleDesc: "",
});

// 表单校验规则
const userRules: FormRules = {
  userName: [
    { required: true, message: "请输入用户名", trigger: "blur" },
    { min: 2, max: 20, message: "用户名长度 2-20 字符", trigger: "blur" },
  ],
  account: [
    { required: true, message: "请输入账号", trigger: "blur" },
    { min: 3, max: 20, message: "账号长度 3-20 字符", trigger: "blur" },
  ],
  password: [
    { required: true, message: "请输入密码", trigger: "blur" },
    { min: 6, max: 20, message: "密码长度 6-20 字符", trigger: "blur" },
  ],
  sex: [{ required: true, message: "请选择性别", trigger: "change" }],
  email: [
    { required: true, message: "请输入邮箱", trigger: "blur" },
    { type: "email", message: "邮箱格式不正确", trigger: "blur" },
  ],
  phoneNum: [
    { required: true, message: "请输入手机号", trigger: "blur" },
    { pattern: /^1[3456789]\d{9}$/, message: "手机号格式不正确", trigger: "blur" },
  ],
  roleName: [{ required: true, message: "请输入角色名称", trigger: "blur" }],
  roleCode: [{ required: true, message: "请输入角色代码", trigger: "blur" }],
};

// ==================== 计算属性 ====================
const dialogTitle = computed(() => (isEdit.value ? "编辑用户" : "添加用户"));

// 前端展示列表（直接使用后端返回的分页数据）
const displayList = computed(() => userList.value);

// ==================== 辅助方法 ====================
const getCurrentUserId = (): number => {
  const userStore = useUserStore();
  return userStore.userState?.userInfo?.id || 1;
};

const formatDateTime = (dateTime: string | undefined): string => {
  if (!dateTime) return "-";
  return dateTime.replace(/-/g, "/");
};

const formatPhoneNumber = (phone: string | undefined, showFull: boolean = false): string => {
  if (!phone) return "-";
  if (showFull) return phone;
  return phone.replace(/(\d{3})\d{4}(\d{4})/, "$1****$2");
};

const togglePhoneVisible = (row: User) => {
  row.showPhone = !row.showPhone;
};

const resetForm = () => {
  if (userFormRef.value) userFormRef.value.resetFields();
  Object.assign(userForm, {
    id: 0, uuid: "", userName: "", account: "", password: "",
    sex: 1, phoneNum: "", email: "", status: 1, imageId: "", createTime: "",
    createBy: 0, updateBy: 0, updateTime: "", isDel: false, note: "", companyId: 0,
    roleName: "", roleCode: "", roleDesc: "",
  });
};

const handleSelectionChange = (rows: User[]) => {
  selectedRows.value = rows;
};

// ==================== API 调用 ====================

/** 获取角色选项（供下拉框使用） */
const fetchRoleOptions = async () => {
  try {
    const response = await api.role.getAll() as any;
    if (response.code === 200) {
      roleOptions.value = (response.data || []).map((item: any) => ({
        label: item.roleName,
        value: item.roleCode,
      }));
    }
  } catch (error) {
    console.error("获取角色选项失败:", error);
  }
};

/** 获取用户列表（后端分页） */
const fetchUserList = async () => {
  loading.value = true;
  try {
    const params: any = {
      pageNum: pagination.currentPage,
      pageSize: pagination.pageSize,
    };
    if (searchKeyword.value) params.userName = searchKeyword.value;
    if (statusFilter.value !== null) params.status = statusFilter.value;
    if (sexFilter.value !== null) params.sex = sexFilter.value;
    if (roleFilter.value) params.roleCode = roleFilter.value;

    const response = await api.user.getList(params) as any;
    if (response.code === 200) {
      userList.value = (response.data.records || []).map((item: User) => ({ ...item, showPhone: false }));
      pagination.total = response.data.total || 0;
    } else {
      ElMessage.error(response.msg || "获取用户列表失败");
      userList.value = [];
      pagination.total = 0;
    }
  } catch (error) {
    console.error("获取用户列表错误:", error);
    ElMessage.error("获取用户列表失败");
    userList.value = [];
    pagination.total = 0;
  } finally {
    loading.value = false;
  }
};

// ==================== 页面交互方法 ====================

/** 搜索 */
const handleSearch = () => {
  pagination.currentPage = 1;
  fetchUserList();
};

/** 每页条数变化 */
const handleSizeChange = (size: number) => {
  pagination.pageSize = size;
  pagination.currentPage = 1;
  fetchUserList();
};

/** 页码变化 */
const handleCurrentChange = (page: number) => {
  pagination.currentPage = page;
  fetchUserList();
};

/** 打开新增弹窗 */
const handleAdd = () => {
  isEdit.value = false;
  resetForm();
  dialogVisible.value = true;
};

/** 打开编辑弹窗 */
const handleEdit = (row: User) => {
  isEdit.value = true;
  Object.assign(userForm, { ...row });
  dialogVisible.value = true;
};

/** 删除用户（单条） */
const handleDelete = async (row: User) => {
  try {
    await ElMessageBox.confirm(`确定删除用户 "${row.userName}" 吗？`, "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    });
    const currentUserId = getCurrentUserId();
    const response = await api.user.delete(row.uuid, currentUserId) as any;
    if (response.code === 200) {
      ElMessage.success(response.msg || "删除成功");
      await fetchUserList();
    } else {
      ElMessage.error(response.msg || "删除失败");
    }
  } catch (error) {
    if (error !== "cancel") console.error("删除用户错误:", error);
  }
};

/** 批量删除（调用通用批量操作接口） */
const handleBatchDelete = async () => {
  if (selectedRows.value.length === 0) return;
  try {
    await ElMessageBox.confirm(`确定删除选中的 ${selectedRows.value.length} 个用户吗？`, "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    });
    const uuids = selectedRows.value.map(row => row.uuid);
    const currentUserId = getCurrentUserId();
    const response = await api.user.batchOperation(uuids, 'delete', currentUserId) as any;
    if (response.code === 200) {
      ElMessage.success(response.msg || "批量删除成功");
      selectedRows.value = [];
      await fetchUserList();
    } else {
      ElMessage.error(response.msg || "批量删除失败");
    }
  } catch (error) {
    if (error !== "cancel") console.error("批量删除错误:", error);
  }
};

/** 切换用户状态 */
const handleToggleStatus = async (row: User) => {
  try {
    const action = row.status === 1 ? "禁用" : "启用";
    await ElMessageBox.confirm(`确定${action}用户 "${row.userName}" 吗？`, "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    });
    const newStatus = row.status === 1 ? 0 : 1;
    const currentUserId = getCurrentUserId();
    const response = await api.user.toggleStatus(row.uuid, newStatus, currentUserId) as any;
    if (response.code === 200) {
      ElMessage.success(response.msg || `${action}成功`);
      await fetchUserList();
    } else {
      ElMessage.error(response.msg || `${action}失败`);
    }
  } catch (error) {
    if (error !== "cancel") console.error("切换用户状态错误:", error);
  }
};

/** 提交表单（新增/编辑） */
const handleSubmit = async () => {
  if (!userFormRef.value) return;
  try {
    await userFormRef.value.validate();
    const currentUserId = getCurrentUserId();
    if (isEdit.value) {
      const response = await api.user.update({
        uuid: userForm.uuid,
        userName: userForm.userName,
        account: userForm.account,
        sex: userForm.sex,
        email: userForm.email,
        phoneNum: userForm.phoneNum,
        status: userForm.status,
        roleName: userForm.roleName,
        roleCode: userForm.roleCode,
        roleDesc: userForm.roleDesc,
      }, currentUserId) as any;
      if (response.code === 200) {
        ElMessage.success(response.msg || "修改成功");
        dialogVisible.value = false;
        await fetchUserList();
      } else {
        ElMessage.error(response.msg || "修改失败");
      }
    } else {
      const response = await api.user.create({
        userName: userForm.userName,
        account: userForm.account,
        password: userForm.password || "",
        sex: userForm.sex,
        email: userForm.email || "",
        phoneNum: userForm.phoneNum || "",
        roleName: userForm.roleName,
        roleCode: userForm.roleCode,
        roleDesc: userForm.roleDesc,
      }, currentUserId) as any;
      if (response.code === 200) {
        ElMessage.success(response.msg || "添加成功");
        dialogVisible.value = false;
        await fetchUserList();
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

// ==================== 生命周期 ====================
onMounted(() => {
  fetchRoleOptions();
  fetchUserList();
});
</script>

<style scoped>
.user-management {
  height: 100%;
}
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
.time-text {
  white-space: nowrap;
}
</style>