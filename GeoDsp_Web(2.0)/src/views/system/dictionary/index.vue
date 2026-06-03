<!--
  字典类型管理页面
  功能：字典类型的增删改查，支持搜索、启用/禁用状态切换
  接口：/api/system/dict/type/*
-->
<template>
  <PageLayout>
    <div class="dictionary-management">
      <!-- 操作栏：字典数据管理入口 + 新增按钮 + 搜索框 -->
      <div class="operation-area">
        <!-- 跳转到字典数据管理页面 -->
        <el-button type="primary" @click="navigateToDictionaryData">
          <el-icon><List /></el-icon>
          字典数据管理
        </el-button>
        <el-button v-permission="'dict:add'" type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>
          添加字典类型
        </el-button>
        <div class="search-area">
          <!-- 本地搜索：按字典名称/编码过滤 -->
          <el-input
            v-model="searchKeyword"
            placeholder="请输入字典名称或编码搜索"
            style="width: 250px"
            clearable
            @clear="handleSearch"
            @keyup.enter="handleSearch"
          >
            <template #append>
              <el-button @click="handleSearch">
                <el-icon><Search /></el-icon>
              </el-button>
            </template>
          </el-input>
        </div>
      </div>

      <!-- 字典类型列表表格 -->
      <el-table :data="filteredList" stripe style="width: 100%">
        <el-table-column prop="id" label="序号" width="80" />
        <el-table-column prop="dictName" label="字典名称" />
        <el-table-column prop="dictCode" label="字典编码" />
        <!-- 状态标签：enabled=启用(绿)，disabled=禁用(红) -->
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag
              :type="scope.row.status === 'enabled' ? 'success' : 'danger'"
            >
              {{ scope.row.status === "enabled" ? "启用" : "禁用" }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button v-permission="'dict:edit'" size="small" @click="handleEdit(scope.row)">
              编辑
            </el-button>
            <!-- 启用/禁用切换按钮 -->
            <el-button
              size="small"
              :type="scope.row.status === 'enabled' ? 'warning' : 'success'"
              @click="handleToggleStatus(scope.row)"
            >
              {{ scope.row.status === "enabled" ? "禁用" : "启用" }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页组件：前端分页 -->
      <div class="pagination">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :small="true"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>

      <!-- 新增/编辑字典类型弹窗 -->
      <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
        <el-form :model="formData" label-width="80px">
          <el-form-item label="字典名称">
            <el-input v-model="formData.dictName" placeholder="请输入字典名称" />
          </el-form-item>
          <el-form-item label="字典编码">
            <!-- 编辑时字典编码不可修改（disabled） -->
            <el-input
              v-model="formData.dictCode"
              placeholder="请输入字典编码"
              :disabled="isEdit"
            />
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="formData.status" placeholder="请选择状态">
              <el-option label="启用" value="enabled" />
              <el-option label="禁用" value="disabled" />
            </el-select>
          </el-form-item>
          <el-form-item label="描述">
            <el-input
              v-model="formData.dictDesc"
              type="textarea"
              placeholder="请输入字典描述"
              rows="3"
            />
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false">取消</el-button>
            <el-button type="primary" @click="handleSubmit">确定</el-button>
          </span>
        </template>
      </el-dialog>
    </div>
  </PageLayout>
</template>

<script setup lang="ts">
/**
 * 字典类型管理页面
 * 接口对应：api.dict (getTypeList / addType / updateType)
 * 数据流：后端一次返回全部数据，前端负责过滤和分页
 */
import { ref, reactive, computed, onMounted } from "vue";
import { useRouter } from "vue-router";
import { Plus, Search, List } from "@element-plus/icons-vue";
import { ElMessage } from "element-plus";
import PageLayout from "@/components/PageLayout.vue";
import api from "@/api";

const router = useRouter();

// ==================== 类型定义 ====================

/** 字典类型信息 */
interface DictType {
  id: number;
  uuid: string;           // 对外主键，用于编辑/删除
  dictName: string;       // 字典名称（显示名）
  dictCode: string;       // 字典编码（唯一标识，编辑时不可修改）
  dictDesc?: string;      // 字典描述
  status: string;         // 状态：enabled=启用，disabled=禁用
  createTime: string;
  createBy: number;
  updateBy?: number;
  updateTime?: string;
  isDel: boolean;
}

// ==================== 响应式数据 ====================

const searchKeyword = ref("");           // 本地搜索关键词
const currentPage = ref(1);              // 当前页码（前端分页）
const pageSize = ref(10);                // 每页条数（前端分页）
const total = ref(0);                    // 总数据条数
const dictTypeList = ref<DictType[]>([]); // 字典类型列表（后端一次拉取）
const dialogVisible = ref(false);        // 弹窗显隐
const dialogTitle = ref("添加字典类型");  // 弹窗标题
const isEdit = ref(false);               // 是否为编辑模式

/** 表单数据 */
const formData = reactive({
  id: 0,
  uuid: "",
  dictName: "",
  dictCode: "",
  status: "enabled",
  dictDesc: "",
  createTime: "",
});

// ==================== 计算属性 ====================

/**
 * 前端过滤 + 前端分页
 * 1. 根据 searchKeyword 过滤 dictName / dictCode
 * 2. 根据 currentPage / pageSize 对过滤后的数据进行切片
 * 3. 更新 total 供分页组件使用
 */
const filteredList = computed(() => {
  let result = dictTypeList.value;

  // 本地关键词过滤
  if (searchKeyword.value) {
    const keyword = searchKeyword.value.toLowerCase();
    result = result.filter(
      (item) =>
        item.dictName.toLowerCase().includes(keyword) ||
        item.dictCode.toLowerCase().includes(keyword)
    );
  }

  // 更新总数（用于分页组件显示）
  total.value = result.length;

  // 前端分页切片
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  return result.slice(start, end);
});

// ==================== 辅助方法 ====================

/**
 * 获取当前操作用户ID
 * TODO: 集成登录后，从 Pinia store 或 token 中获取真实用户ID
 */
const getCurrentUserId = (): number => {
  return 1;
};

// ==================== API 调用 ====================

/**
 * 获取字典类型列表
 * 接口：GET /api/system/dict/type/list
 * 后端一次返回全部数据，前端负责过滤和分页
 */
const getDictTypeList = async () => {
  try {
    const response = await api.dict.getTypeList();

    if (response.code === 200 && response.data) {
      // 确保 status 字段有默认值
      dictTypeList.value = response.data.map((item: any) => ({
        ...item,
        status: item.status || "enabled",
      }));
      total.value = dictTypeList.value.length;
    } else {
      console.error("获取字典类型列表失败:", response.msg);
      ElMessage.error(response.msg || "获取字典类型列表失败");
      dictTypeList.value = [];
    }
  } catch (error) {
    console.error("获取字典类型列表错误:", error);
    ElMessage.error("获取字典类型列表失败，请检查网络连接");
    dictTypeList.value = [];
  }
};

// ==================== 页面交互方法 ====================

/** 跳转到字典数据管理页面 */
const navigateToDictionaryData = () => {
  router.push("/system/dictionary/data");
};

/** 打开新增弹窗 */
const handleAdd = () => {
  dialogTitle.value = "添加字典类型";
  isEdit.value = false;
  Object.assign(formData, {
    id: 0,
    uuid: "",
    dictName: "",
    dictCode: "",
    status: "enabled",
    dictDesc: "",
    createTime: "",
  });
  dialogVisible.value = true;
};

/** 打开编辑弹窗，回填数据 */
const handleEdit = (row: DictType) => {
  dialogTitle.value = "编辑字典类型";
  isEdit.value = true;
  Object.assign(formData, {
    id: row.id,
    uuid: row.uuid,
    dictName: row.dictName,
    dictCode: row.dictCode,
    status: row.status,
    dictDesc: row.dictDesc || "",
    createTime: row.createTime,
  });
  dialogVisible.value = true;
};

/**
 * 提交表单（新增或编辑）
 * 新增接口：POST /api/system/dict/type/add
 * 编辑接口：PUT /api/system/dict/type/update
 */
const handleSubmit = async () => {
  // 表单校验
  if (!formData.dictName) {
    ElMessage.error("请输入字典名称");
    return;
  }
  if (!formData.dictCode) {
    ElMessage.error("请输入字典编码");
    return;
  }

  const currentUserId = getCurrentUserId();

  try {
    if (isEdit.value) {
      // 编辑模式
      const response = await api.dict.updateType(
        {
          uuid: formData.uuid,
          dictName: formData.dictName,
          dictCode: formData.dictCode,
          dictDesc: formData.dictDesc,
        },
        currentUserId
      );

      if (response.code === 200) {
        ElMessage.success("编辑成功");
        await getDictTypeList();  // 刷新列表
      } else {
        ElMessage.error(response.msg || "编辑失败");
      }
    } else {
      // 新增模式
      const response = await api.dict.addType(
        {
          dictName: formData.dictName,
          dictCode: formData.dictCode,
          dictDesc: formData.dictDesc,
        },
        currentUserId
      );

      if (response.code === 200) {
        ElMessage.success("添加成功");
        await getDictTypeList();  // 刷新列表
      } else {
        ElMessage.error(response.msg || "添加失败");
      }
    }

    dialogVisible.value = false;
  } catch (error) {
    console.error("提交表单错误:", error);
    ElMessage.error("操作失败，请检查网络连接");
  }
};

/** 搜索：重置到第一页 */
const handleSearch = () => {
  currentPage.value = 1;
};

/**
 * 切换字典类型状态（启用/禁用）
 * 复用 updateType 接口
 */
const handleToggleStatus = async (row: DictType) => {
  const newStatus = row.status === "enabled" ? "disabled" : "enabled";
  const currentUserId = getCurrentUserId();

  try {
    const response = await api.dict.updateType(
      {
        uuid: row.uuid,
        dictName: row.dictName,
        dictCode: row.dictCode,
        dictDesc: row.dictDesc,
      },
      currentUserId
    );

    if (response.code === 200) {
      row.status = newStatus;
      ElMessage.success(newStatus === "enabled" ? "已启用" : "已禁用");
    } else {
      ElMessage.error(response.msg || "状态切换失败");
    }
  } catch (error) {
    console.error("状态切换错误:", error);
    ElMessage.error("状态切换失败，请检查网络连接");
  }
};

/** 每页条数变化：重置到第一页 */
const handleSizeChange = (size: number) => {
  pageSize.value = size;
  currentPage.value = 1;
};

/** 页码变化 */
const handleCurrentChange = (page: number) => {
  currentPage.value = page;
};

// ==================== 生命周期 ====================
onMounted(() => {
  getDictTypeList();
});
</script>

<style scoped>
.dictionary-management {
  padding: 20px;
}

.operation-area {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.pagination {
  margin-top: 16px;
  text-align: right;
  font-size: 12px;
}
</style>