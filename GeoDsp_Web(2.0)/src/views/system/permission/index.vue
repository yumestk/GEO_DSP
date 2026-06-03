<!--
  权限管理页面
  功能：权限的增删改查，树形表格展示权限层级结构
  接口：/api/system/permissions/*
  表：permissions (uuid, permission_name, permission_code, permission_desc, permission_type, parent_permission, component_path, status)
-->
<template>
  <PageLayout>
    <div class="permission-management">
      <!-- 标签页切换 -->
      <el-tabs v-model="activeTab" @tab-click="handleTabClick">
        <!-- ==================== 菜单管理 ==================== -->
        <el-tab-pane label="菜单管理" name="permissions">
          <!-- 搜索栏 -->
          <div class="search-bar">
            <div class="search-group">
              <el-input
                v-model="searchKeyword"
                placeholder="权限名称/编码"
                style="width: 200px"
                clearable
                @clear="handleSearch"
                @keyup.enter="handleSearch"
              />
              <el-select
                v-model="typeFilter"
                placeholder="权限类型"
                style="width: 120px"
                clearable
                @change="handleSearch"
              >
                <el-option label="目录" value="M" />
                <el-option label="菜单" value="C" />
                <el-option label="按钮" value="F" />
              </el-select>
            </div>
            <el-button type="primary" @click="handleSearch">查询</el-button>
          </div>

          <div class="card">
            <div class="card-header">
              <div class="header-buttons">
                <el-button v-permission="'permission:add'" type="primary" @click="handleAdd">
                  <el-icon><Plus /></el-icon>
                  新增权限
                </el-button>
                <el-button @click="expandAll">展开全部</el-button>
                <el-button @click="collapseAll">折叠全部</el-button>
              </div>
            </div>

            <div class="table-wrapper">
              <el-table
                :data="displayList"
                row-key="uuid"
                stripe
                v-loading="loading"
                :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
                ref="tableRef"
                style="width: 100%"
                :fit="true"
              >
                <el-table-column prop="permission_name" label="权限名称" min-width="180" />
                <el-table-column prop="permission_code" label="权限编码" min-width="180" />
                <el-table-column prop="permission_desc" label="描述" min-width="180" show-overflow-tooltip />
                <el-table-column prop="permission_type" label="类型" width="80">
                  <template #default="scope">
                    <el-tag :type="getTypeTag(scope.row.permission_type)" size="small">
                      {{ getTypeText(scope.row.permission_type) }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="component_path" label="组件路径" min-width="200" show-overflow-tooltip>
                  <template #default="scope">
                    <span>{{ scope.row.component_path || "-" }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="250" fixed="right">
                  <template #default="scope">
                    <div class="action-buttons">
                      <el-button v-permission="'permission:add'" size="small" @click="handleAddChild(scope.row)">新增子权限</el-button>
                      <el-button v-permission="'permission:edit'" size="small" @click="handleEdit(scope.row)">编辑</el-button>
                      <el-button v-permission="'permission:delete'" size="small" type="danger" @click="handleDelete(scope.row)">删除</el-button>
                    </div>
                  </template>
                </el-table-column>
                <el-table-column min-width="1" />
              </el-table>
            </div>
          </div>
        </el-tab-pane>

        <!-- ==================== 角色权限 ==================== -->
        <el-tab-pane label="角色权限" name="rolePermissions">
          <!-- 搜索栏 -->
          <div class="search-bar">
            <div class="search-group">
              <el-input
                v-model="roleSearchKeyword"
                placeholder="角色名称/编码"
                style="width: 200px"
                clearable
                @clear="handleRoleSearch"
                @keyup.enter="handleRoleSearch"
              />
            </div>
            <el-button type="primary" @click="handleRoleSearch">查询</el-button>
          </div>

          <div class="card">
            <div class="table-wrapper">
              <el-table
                :data="filteredRoleList"
                stripe
                v-loading="roleLoading"
                style="width: 100%"
              >
                <el-table-column prop="roleName" label="角色名称" min-width="150" />
                <el-table-column prop="roleCode" label="角色编码" min-width="150" />
                <el-table-column prop="roleDesc" label="角色描述" min-width="300" />
                <el-table-column label="操作" width="100" fixed="right">
                  <template #default="scope">
                    <div class="action-buttons">
                      <el-button v-permission="'permission:auth'" size="small" type="primary" @click="handleAuth(scope.row)">授权</el-button>
                    </div>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>

      <!-- 新增/编辑权限弹窗 -->
      <el-dialog
        v-model="dialogVisible"
        :title="dialogTitle"
        width="600px"
        @close="handleDialogClose"
      >
        <el-form ref="formRef" :model="formData" :rules="formRules" label-width="100px">
          <el-form-item label="上级菜单" prop="parent_permission">
            <el-tree-select
              v-model="formData.parent_permission"
              :data="permissionTreeOptions"
              :props="{ value: 'uuid', label: 'permission_name', children: 'children' }"
              placeholder="请选择上级菜单"
              check-strictly
              clearable
              filterable
              style="width: 100%"
            />
          </el-form-item>

          <el-form-item label="权限类型" prop="permission_type">
            <el-radio-group v-model="formData.permission_type">
              <el-radio value="M">目录</el-radio>
              <el-radio value="C">菜单</el-radio>
              <el-radio value="F">按钮</el-radio>
            </el-radio-group>
          </el-form-item>

          <el-form-item label="权限名称" prop="permission_name">
            <el-input v-model="formData.permission_name" placeholder="请输入权限名称" />
          </el-form-item>

          <el-form-item label="权限编码" prop="permission_code">
            <el-input v-model="formData.permission_code" placeholder="请输入权限编码" />
          </el-form-item>

          <el-form-item label="组件路径" prop="component_path" v-if="formData.permission_type !== 'F'">
            <el-input v-model="formData.component_path" placeholder="请输入组件路径，如: views/user/index.vue" />
            <div class="form-tip">菜单/目录类型需填写组件路径，按钮类型可不填</div>
          </el-form-item>

          <el-form-item label="权限描述" prop="permission_desc">
            <el-input v-model="formData.permission_desc" type="textarea" :rows="3" placeholder="请输入权限描述" />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">确定</el-button>
        </template>
      </el-dialog>

      <!-- 授权弹窗（扁平列表穿梭框） -->
      <el-dialog
        v-model="authDialogVisible"
        :title="`权限分配 - ${currentRole?.roleName || ''}`"
        width="800px"
        destroy-on-close
      >
        <div class="auth-transfer-container">
          <el-transfer
            v-model="checkedPermissionKeys"
            :data="transferData"
            :titles="['全部权限', '已分配权限']"
            :button-texts="['移除', '添加']"
            :format="{ noChecked: '${total}', noData: '暂无权限', checked: '已选 ${checked}' }"
            @change="handleTransferChange"
            style="width: 100%"
          >
            <template #default="{ option }">
              <div class="transfer-option">
                <el-tag :type="getTypeTag(option.permission_type)" size="small">
                  {{ getTypeText(option.permission_type) }}
                </el-tag>
                <span class="option-name">{{ option.permission_name }}</span>
              </div>
            </template>
          </el-transfer>
        </div>
        <template #footer>
          <el-button @click="authDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSavePermissions" :loading="authSaving">
            保存权限
          </el-button>
        </template>
      </el-dialog>
    </div>
  </PageLayout>
</template>

<script setup lang="ts">
/**
 * 权限管理页面 - 联调版本
 * 接口：/api/system/permissions/*
 */
import { ref, reactive, computed, onMounted} from "vue";
import PageLayout from "@/components/PageLayout.vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { Plus } from "@element-plus/icons-vue";
import type { FormInstance, FormRules } from "element-plus";
import api from "@/api/index";

// ==================== 类型定义 ====================
interface Permission {
  id?: number;
  uuid: string;
  permission_name: string;
  permission_code: string;
  permission_desc?: string;
  permission_type: "M" | "C" | "F";
  parent_permission: string | null;
  component_path?: string;
  status: 0 | 1;
  create_time?: string;
  create_by?: number;
  update_by?: number;
  children?: Permission[];
  hasChildren?: boolean;
}

interface Role {
  id: number;
  uuid: string;
  roleName: string;
  roleCode: string;
  roleDesc?: string;
}

interface TransferOption {
  key: string;
  label: string;
  permission_name: string;
  permission_code: string;
  permission_type: "M" | "C" | "F";
}

// ==================== 响应式数据 ====================
const activeTab = ref("permissions");
const permissionList = ref<Permission[]>([]);
const loading = ref(false);
const searchKeyword = ref("");
const typeFilter = ref<string | null>(null);
const dialogVisible = ref(false);
const isEdit = ref(false);
const editUuid = ref("");
const formRef = ref<FormInstance>();
const tableRef = ref();

// 角色权限相关
const roleList = ref<Role[]>([]);
const roleLoading = ref(false);
const roleSearchKeyword = ref("");
const authDialogVisible = ref(false);
const currentRole = ref<Role | null>(null);
const checkedPermissionKeys = ref<string[]>([]);
const transferData = ref<TransferOption[]>([]);
const authSaving = ref(false);

// 分页参数
const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0,
});

// 表单数据
const formData = reactive({
  uuid: "",
  permission_name: "",
  permission_code: "",
  permission_desc: "",
  permission_type: "M" as "M" | "C" | "F",
  parent_permission: null as string | null,
  component_path: "",
});

// 表单校验规则
const formRules: FormRules = {
  permission_name: [{ required: true, message: "请输入权限名称", trigger: "blur" }],
  permission_code: [{ required: true, message: "请输入权限编码", trigger: "blur" }],
  permission_type: [{ required: true, message: "请选择权限类型", trigger: "change" }],
};

// ==================== 计算属性 ====================
const dialogTitle = computed(() => (isEdit.value ? "编辑权限" : "新增权限"));

const displayList = computed(() => {
  let list = JSON.parse(JSON.stringify(permissionList.value));
  
  if (searchKeyword.value) {
    list = filterTreeByKeyword(list, searchKeyword.value.toLowerCase());
  }
  if (typeFilter.value) {
    list = filterTreeByType(list, typeFilter.value);
  }
  
  return list;
});

const permissionTreeOptions = computed(() => {
  const root = { uuid: null, permission_name: "顶级菜单", children: permissionList.value };
  return [root];
});

const filteredRoleList = computed(() => {
  let list = [...roleList.value];
  
  if (roleSearchKeyword.value) {
    const kw = roleSearchKeyword.value.toLowerCase();
    list = list.filter(item => 
      item.roleName.toLowerCase().includes(kw) ||
      item.roleCode.toLowerCase().includes(kw)
    );
  }
  
  return list;
});

// ==================== 辅助方法 ====================
const flattenPermissions = (nodes: Permission[]): TransferOption[] => {
  let result: TransferOption[] = [];
  const traverse = (items: Permission[]) => {
    items.forEach(item => {
      result.push({
        key: item.uuid,
        label: item.permission_name,
        permission_name: item.permission_name,
        permission_code: item.permission_code,
        permission_type: item.permission_type,
      });
      if (item.children && item.children.length) {
        traverse(item.children);
      }
    });
  };
  traverse(nodes);
  return result;
};

const filterTreeByKeyword = (nodes: Permission[], keyword: string): Permission[] => {
  return nodes.filter(node => {
    const match = node.permission_name.toLowerCase().includes(keyword) ||
                  node.permission_code.toLowerCase().includes(keyword);
    if (node.children && node.children.length) {
      node.children = filterTreeByKeyword(node.children, keyword);
      return match || (node.children && node.children.length > 0);
    }
    return match;
  });
};

const filterTreeByType = (nodes: Permission[], type: string): Permission[] => {
  return nodes.filter(node => {
    const match = node.permission_type === type;
    if (node.children && node.children.length) {
      node.children = filterTreeByType(node.children, type);
      return match || (node.children && node.children.length > 0);
    }
    return match;
  });
};

const getTypeText = (type: string): string => {
  switch (type) {
    case "M": return "目录";
    case "C": return "菜单";
    case "F": return "按钮";
    default: return "-";
  }
};

const getTypeTag = (type: string): string => {
  switch (type) {
    case "M": return "warning";
    case "C": return "primary";
    case "F": return "info";
    default: return "";
  }
};

const expandAll = () => {
  if (tableRef.value) {
    const toggleExpand = (rows: any[]) => {
      rows.forEach(row => {
        if (row.children && row.children.length) {
          tableRef.value.toggleRowExpansion(row, true);
          toggleExpand(row.children);
        }
      });
    };
    toggleExpand(displayList.value);
  }
};

const collapseAll = () => {
  if (tableRef.value) {
    const toggleCollapse = (rows: any[]) => {
      rows.forEach(row => {
        if (row.children && row.children.length) {
          tableRef.value.toggleRowExpansion(row, false);
          toggleCollapse(row.children);
        }
      });
    };
    toggleCollapse(displayList.value);
  }
};

const resetForm = () => {
  if (formRef.value) formRef.value.resetFields();
  formData.uuid = "";
  formData.permission_name = "";
  formData.permission_code = "";
  formData.permission_desc = "";
  formData.permission_type = "M";
  formData.parent_permission = null;
  formData.component_path = "";
  isEdit.value = false;
  editUuid.value = "";
};

// 穿梭框变化事件
const handleTransferChange = (keys: string[]) => {
  checkedPermissionKeys.value = keys;
};

// 打开授权弹窗
const handleAuth = async (row: Role) => {
  currentRole.value = row;
  authDialogVisible.value = true;
  
  // 加载所有权限（扁平化）
  transferData.value = flattenPermissions(permissionList.value);
  
  // 加载该角色已分配的权限
  try {
    const response = await api.permission.getByRole(row.uuid) as any;
    if (response.code === 200) {
      // 假设后端返回的是权限编码数组
      const assignedCodes = response.data || [];
      // 根据编码找到对应的 uuid
      const assignedKeys = transferData.value
        .filter(opt => assignedCodes.includes(opt.permission_code))
        .map(opt => opt.key);
      checkedPermissionKeys.value = assignedKeys;
    } else {
      checkedPermissionKeys.value = [];
    }
  } catch (error) {
    console.error("获取角色权限失败:", error);
    checkedPermissionKeys.value = [];
  }
};

// 保存权限分配
const handleSavePermissions = async () => {
  if (!currentRole.value) return;
  
  authSaving.value = true;
  try {
    // 获取选中的权限编码
    const selectedCodes = transferData.value
      .filter(opt => checkedPermissionKeys.value.includes(opt.key))
      .map(opt => opt.permission_code);
    
    const response = await api.permission.saveRolePermissions(
      currentRole.value.uuid,
      selectedCodes
    ) as any;
    
    if (response.code === 200) {
      ElMessage.success(`成功为角色「${currentRole.value.roleName}」分配权限`);
      authDialogVisible.value = false;
    } else {
      ElMessage.error(response.msg || "保存失败");
    }
  } catch (error) {
    console.error("保存权限失败:", error);
    ElMessage.error("保存失败");
  } finally {
    authSaving.value = false;
  }
};

// 角色搜索
const handleRoleSearch = () => {
  // 触发计算属性刷新
};

// ==================== API 调用 ====================
/** 获取权限列表（树形数据） */
const fetchPermissionList = async () => {
  loading.value = true;
  try {
    const params: any = {
      pageNum: pagination.currentPage,
      pageSize: pagination.pageSize,
    };
    if (searchKeyword.value) params.keyword = searchKeyword.value;
    if (typeFilter.value) params.permissionType = typeFilter.value;

    const response = await api.permission.getList(params) as any;
    if (response.code === 200) {
      // 注意：后端返回的是扁平列表还是树形？这里假设是树形，如果是扁平需要 buildTree
      permissionList.value = response.data.records || [];
      pagination.total = response.data.total || 0;
    } else {
      ElMessage.error(response.msg || "获取权限列表失败");
    }
  } catch (error) {
    console.error("获取权限列表错误:", error);
    ElMessage.error("获取权限列表失败");
  } finally {
    loading.value = false;
  }
};

/** 获取角色列表（用于角色权限 Tab） */
const fetchRoleList = async () => {
  roleLoading.value = true;
  try {
    const response = await api.role.getAll() as any;
    if (response.code === 200) {
      roleList.value = response.data || [];
    } else {
      ElMessage.error(response.msg || "获取角色列表失败");
    }
  } catch (error) {
    console.error("获取角色列表错误:", error);
    ElMessage.error("获取角色列表失败");
  } finally {
    roleLoading.value = false;
  }
};

// ==================== 页面交互 ====================
const handleTabClick = (tab: any) => {
  if (tab.paneName === "rolePermissions") {
    fetchRoleList();
  }
};

const handleSearch = () => {
  pagination.currentPage = 1;
  fetchPermissionList();
};

const handleAdd = () => {
  resetForm();
  dialogVisible.value = true;
};

const handleAddChild = (row: Permission) => {
  resetForm();
  formData.parent_permission = row.uuid;
  dialogVisible.value = true;
};

const handleEdit = (row: Permission) => {
  isEdit.value = true;
  editUuid.value = row.uuid;
  formData.uuid = row.uuid;
  formData.permission_name = row.permission_name;
  formData.permission_code = row.permission_code;
  formData.permission_desc = row.permission_desc || "";
  formData.permission_type = row.permission_type;
  formData.parent_permission = row.parent_permission;
  formData.component_path = row.component_path || "";
  dialogVisible.value = true;
};

const handleDelete = async (row: Permission) => {
  try {
    await ElMessageBox.confirm(`确定删除权限 "${row.permission_name}" 吗？`, "提示", {
      confirmButtonText: "确定", cancelButtonText: "取消", type: "warning",
    });
    
    const response = await api.permission.delete(row.uuid) as any;
    if (response.code === 200) {
      ElMessage.success(response.msg || "删除成功");
      await fetchPermissionList();
    } else {
      ElMessage.error(response.msg || "删除失败");
    }
  } catch (error) {
    if (error !== "cancel") {
      console.error("删除权限错误:", error);
      ElMessage.error("删除失败");
    }
  }
};

const handleSubmit = async () => {
  if (!formRef.value) return;
  try {
    await formRef.value.validate();
    const currentUserId = 1; // 从 store 获取
    
    if (isEdit.value) {
      const response = await api.permission.update({
        uuid: formData.uuid,
        permissionName: formData.permission_name,
        permissionCode: formData.permission_code,
        permissionDesc: formData.permission_desc,
        permissionType: formData.permission_type,
        parentPermission: formData.parent_permission,
        componentPath: formData.component_path,
      }, currentUserId) as any;
      if (response.code === 200) {
        ElMessage.success(response.msg || "修改成功");
        dialogVisible.value = false;
        await fetchPermissionList();
      } else {
        ElMessage.error(response.msg || "修改失败");
      }
    } else {
      const response = await api.permission.create({
        permissionName: formData.permission_name,
        permissionCode: formData.permission_code,
        permissionDesc: formData.permission_desc,
        permissionType: formData.permission_type,
        parentPermission: formData.parent_permission,
        componentPath: formData.component_path,
      }, currentUserId) as any;
      if (response.code === 200) {
        ElMessage.success(response.msg || "添加成功");
        dialogVisible.value = false;
        await fetchPermissionList();
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
  fetchPermissionList();
});
</script>

<style scoped>
.permission-management {
  min-height: 100%;
  display: flex;
  flex-direction: column;
  background-color: #ffffff;
}

.search-group {
  display: flex;
  gap: 12px;
  align-items: center;
  flex-wrap: wrap;
}

.card-header {
  padding: 12px 20px;
  border-bottom: 1px solid #ebeef5;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-buttons {
  display: flex;
  gap: 12px;
  align-items: center;
  flex-wrap: wrap;
}

.table-wrapper {
  width: 100%;
  overflow-x: auto;
}

.action-buttons {
  display: flex;
  gap: 8px;
  flex-wrap: nowrap;
}

/* 授权弹窗穿梭框样式 */
.auth-transfer-container {
  padding: 8px 0;
}

:deep(.el-transfer) {
  display: flex !important;
  flex-direction: row !important;
  flex-wrap: nowrap !important;
  justify-content: center !important;
  gap: 16px;
}

:deep(.el-transfer-panel) {
  width: 320px;
  flex-shrink: 0;
}

:deep(.el-transfer-panel__body) {
  height: 420px;
}

:deep(.el-transfer__buttons) {
  display: flex !important;
  flex-direction: column !important;
  justify-content: center !important;
  gap: 12px;
  padding: 0 8px;
}

:deep(.el-transfer__buttons .el-button) {
  display: block;
  margin: 0 !important;
}

.transfer-option {
  display: flex;
  align-items: center;
  gap: 8px;
}

.option-name {
  font-size: 14px;
  color: #303133;
}

/* 表单提示 */
.form-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
}
</style>