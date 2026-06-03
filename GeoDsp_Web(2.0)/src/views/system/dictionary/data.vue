<!-- 字典数据管理页面 -->
<!-- 功能：管理指定字典类型下的数据项（键值对），支持增删改查和前端分页 -->
<template>
  <div class="dictionary-data-management">
    <!-- 面包屑导航 -->
    <el-breadcrumb separator="/" class="breadcrumb">
      <el-breadcrumb-item :to="{ path: '/system' }"
        >系统管理</el-breadcrumb-item
      >
      <el-breadcrumb-item :to="{ path: '/system/dictionary' }"
        >字典管理</el-breadcrumb-item
      >
      <el-breadcrumb-item>字典数据管理</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 页面标题区 -->
    <div class="page-header">
      <div>
        <h2>字典数据管理</h2>
        <p>管理系统中的字典数据项</p>
      </div>
      <!-- 返回字典管理列表页 -->
      <el-button @click="navigateBack">
        <el-icon><ArrowLeft /></el-icon>
        返回字典管理
      </el-button>
    </div>

    <!-- 操作栏：新增按钮 + 筛选区 -->
    <div class="operation-area">
      <el-button v-permission="'dict:add'" type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        添加字典数据
      </el-button>
      <div class="search-area">
        <!-- 字典类型下拉选择，切换后重新加载对应数据 -->
        <el-select
          v-model="selectedDictCode"
          placeholder="选择字典类型"
          style="width: 150px; margin-right: 10px"
          @change="handleDictCodeChange"
        >
          <el-option
            v-for="dict in dictTypeList"
            :key="dict.dictCode"
            :label="dict.dictName"
            :value="dict.dictCode"
          />
        </el-select>
        <!-- 本地搜索框：过滤字典键/字符串值/描述 -->
        <el-input
          v-model="searchKeyword"
          placeholder="请输入字典键或字符串值搜索"
          style="width: 200px"
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

    <!-- 数据表格：展示当前字典类型下的所有数据项 -->
    <el-table :data="filteredList" stripe style="width: 100%">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="dictKey" label="字典键" />
      <el-table-column prop="dictStrVal" label="字符串值" />
      <el-table-column prop="dictNumVal" label="数字值" />
      <el-table-column prop="dictDesc" label="字典描述" />
      <el-table-column prop="createTime" label="创建时间" width="180" />
      <el-table-column label="操作" width="150">
        <template #default="scope">
          <el-button v-permission="'dict:edit'" size="small" @click="handleEdit(scope.row)">
            编辑
          </el-button>
          <el-button
            v-permission="'dict:delete'"
            size="small"
            type="danger"
            @click="handleDelete(scope.row)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件：前端分页，对本地数据切片展示 -->
    <div class="pagination">
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

    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form :model="formData" label-width="100px">
        <el-form-item label="字典键" prop="dictKey">
          <el-input v-model="formData.dictKey" placeholder="请输入字典键" />
        </el-form-item>
        <el-form-item label="字符串值" prop="dictStrVal">
          <el-input
            v-model="formData.dictStrVal"
            placeholder="请输入字符串值"
          />
        </el-form-item>
        <el-form-item label="数字值" prop="dictNumVal">
          <el-input-number
            v-model="formData.dictNumVal"
            :min="0"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="字典描述" prop="dictDesc">
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
</template>

<script setup lang="ts">
/**
 * 字典数据管理页面
 * 后端接口：/api/system/dict/*
 * 数据流：选择字典类型 → 加载全部数据 → 前端过滤 + 前端分页
 */
import { ref, reactive, computed, onMounted } from "vue";
import { useRouter } from "vue-router";
import { Plus, Search, ArrowLeft } from "@element-plus/icons-vue";
import { ElMessage, ElMessageBox } from "element-plus";
import api from "@/api";

const router = useRouter();

// ==================== 类型定义 ====================
/** 字典类型（如：性别、状态等分类） */
interface DictType {
  id: number;
  uuid: string;
  dictName: string;      // 字典显示名称
  dictCode: string;      // 字典编码，用于关联数据
  dictDesc?: string;
  status: string;
  createTime: string;
}

/** 字典数据项（具体键值对） */
interface DictData {
  id: number;
  uuid: string;
  dictKey: string;       // 字典键
  dictStrVal?: string;   // 字符串值
  dictNumVal?: number;   // 数字值
  dictDesc?: string;     // 描述
  createBy: number;
  createTime: string;
  updateBy?: number;
  updateTime?: string;
  isDel: boolean;
}

// ==================== 路由方法 ====================
/** 返回字典管理列表页 */
const navigateBack = () => {
  router.push("/system/dictionary");
};

// ==================== 响应式数据 ====================
const selectedDictCode = ref("");      // 当前选中的字典类型编码
const searchKeyword = ref("");         // 本地搜索关键词
const currentPage = ref(1);            // 当前页码（前端分页用）
const pageSize = ref(10);              // 每页条数（前端分页用）
const total = ref(0);                  // 总数据条数（前端分页用）
const dictTypeList = ref<DictType[]>([]);   // 字典类型列表
const dictDataList = ref<DictData[]>([]);   // 当前字典类型下的全部数据（从后端一次拉取）
const dialogVisible = ref(false);       // 弹窗显隐
const dialogTitle = ref("添加字典数据"); // 弹窗标题
const isEdit = ref(false);              // 是否为编辑模式

/** 表单数据（新增/编辑共用） */
const formData = reactive<DictData>({
  id: 0,
  uuid: "",
  dictKey: "",
  dictStrVal: "",
  dictNumVal: undefined,
  dictDesc: "",
  createBy: 1,
  createTime: "",
  updateBy: 1,
  updateTime: "",
  isDel: false,
});

// ==================== 计算属性 ====================
/**
 * 前端过滤 + 前端分页
 * 1. 根据 searchKeyword 过滤 dictKey / dictStrVal / dictDesc
 * 2. 根据 currentPage / pageSize 对过滤后的数据进行切片
 * 3. 更新 total 供分页组件使用
 */
const filteredList = computed(() => {
  let result = dictDataList.value;

  // 本地关键词过滤
  if (searchKeyword.value) {
    const keyword = searchKeyword.value.toLowerCase();
    result = result.filter(
      (item) =>
        item.dictKey.toLowerCase().includes(keyword) ||
        (item.dictStrVal && item.dictStrVal.toLowerCase().includes(keyword)) ||
        (item.dictDesc && item.dictDesc.toLowerCase().includes(keyword))
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
 * 成功后默认选中第一个类型并加载其数据
 */
const getDictTypeList = async () => {
  try {
    const response = await api.dict.getTypeList();

    if (response.code === 200 && response.data) {
      dictTypeList.value = response.data;
      // 默认选中第一个字典类型
      if (dictTypeList.value.length > 0 && !selectedDictCode.value) {
        selectedDictCode.value = dictTypeList.value[0].dictCode;
        await loadDictData();
      }
    } else {
      console.error("获取字典类型列表失败:", response.msg);
      ElMessage.error(response.msg || "获取字典类型列表失败");
    }
  } catch (error) {
    console.error("获取字典类型列表错误:", error);
    ElMessage.error("获取字典类型列表失败，请检查网络连接");
  }
};

/**
 * 根据字典编码加载字典数据
 * 接口：GET /api/system/dict/data/list?dictCode=xxx
 * 后端一次返回该类型下的全部数据，前端负责过滤和分页
 */
const loadDictData = async () => {
  if (!selectedDictCode.value) return;

  try {
    const response = await api.dict.getDataList(selectedDictCode.value);

    if (response.code === 200 && response.data) {
      dictDataList.value = response.data;
      total.value = dictDataList.value.length;
    } else {
      console.error("获取字典数据失败:", response.msg);
      ElMessage.error(response.msg || "获取字典数据失败");
      dictDataList.value = [];
    }
  } catch (error) {
    console.error("获取字典数据错误:", error);
    ElMessage.error("获取字典数据失败，请检查网络连接");
    dictDataList.value = [];
  }
};

// ==================== 增删改操作 ====================
/** 打开新增弹窗，重置表单 */
const handleAdd = () => {
  dialogTitle.value = "添加字典数据";
  isEdit.value = false;
  Object.assign(formData, {
    id: 0,
    uuid: "",
    dictKey: "",
    dictStrVal: "",
    dictNumVal: undefined,
    dictDesc: "",
    createBy: 1,
    createTime: "",
    updateBy: 1,
    updateTime: "",
    isDel: false,
  });
  dialogVisible.value = true;
};

/** 打开编辑弹窗，回填当前行数据 */
const handleEdit = (row: DictData) => {
  dialogTitle.value = "编辑字典数据";
  isEdit.value = true;
  Object.assign(formData, {
    id: row.id,
    uuid: row.uuid,
    dictKey: row.dictKey,
    dictStrVal: row.dictStrVal || "",
    dictNumVal: row.dictNumVal,
    dictDesc: row.dictDesc || "",
    createBy: row.createBy,
    createTime: row.createTime,
    updateBy: row.updateBy,
    updateTime: row.updateTime,
    isDel: row.isDel,
  });
  dialogVisible.value = true;
};

/**
 * 删除字典数据
 * 接口：DELETE /api/system/dict/delete/{uuid}
 */
const handleDelete = async (row: DictData) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除字典键"${row.dictKey}"的数据吗？`,
      "警告",
      {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }
    );

    const response = await api.dict.delete(row.uuid);

    if (response.code === 200) {
      ElMessage.success("删除成功");
      await loadDictData();  // 刷新列表
    } else {
      ElMessage.error(response.msg || "删除失败");
    }
  } catch (error) {
    if (error !== "cancel") {
      console.error("删除字典数据错误:", error);
      ElMessage.error("删除失败，请检查网络连接");
    }
  }
};

/**
 * 提交表单（新增或编辑）
 * 新增接口：POST /api/system/dict/data/add
 * 编辑接口：PUT /api/system/dict/data/update
 */
const handleSubmit = async () => {
  // 表单校验
  if (!formData.dictKey) {
    ElMessage.error("请输入字典键");
    return;
  }
  if (!formData.dictStrVal && formData.dictNumVal === undefined) {
    ElMessage.error("请输入字符串值或数字值");
    return;
  }

  const currentUserId = getCurrentUserId();

  try {
    if (isEdit.value) {
      // 编辑模式
      const response = await api.dict.updateData(
        {
          uuid: formData.uuid,
          dictKey: formData.dictKey,
          dictStrVal: formData.dictStrVal,
          dictNumVal: formData.dictNumVal,
          dictDesc: formData.dictDesc,
        },
        currentUserId
      );

      if (response.code === 200) {
        ElMessage.success("编辑成功");
        dialogVisible.value = false;
        await loadDictData();  // 刷新列表
      } else {
        ElMessage.error(response.msg || "编辑失败");
      }
    } else {
      // 新增模式
      const response = await api.dict.addData(
        {
          dictCode: selectedDictCode.value,  // 当前选中的字典类型编码
          dictKey: formData.dictKey,
          dictStrVal: formData.dictStrVal,
          dictNumVal: formData.dictNumVal,
          dictDesc: formData.dictDesc,
        },
        currentUserId
      );

      if (response.code === 200) {
        ElMessage.success("添加成功");
        dialogVisible.value = false;
        await loadDictData();  // 刷新列表
      } else {
        ElMessage.error(response.msg || "添加失败");
      }
    }
  } catch (error) {
    console.error("提交表单错误:", error);
    ElMessage.error("操作失败，请检查网络连接");
  }
};

// ==================== 事件处理 ====================
/** 切换字典类型时，重新加载数据并重置搜索/分页 */
const handleDictCodeChange = () => {
  loadDictData();
  handleSearch();  // 重置搜索关键词和页码
};

/** 搜索/重置搜索：重置到第一页 */
const handleSearch = () => {
  currentPage.value = 1;
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
/* 页面布局样式 */
.dictionary-data-management {
  padding: 20px;
}

.breadcrumb {
  margin-bottom: 16px;
  font-size: 14px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
  font-size: 24px;
}

.page-header p {
  margin: 5px 0 0 0;
  color: #606266;
}

.operation-area {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  text-align: right;
}
</style>