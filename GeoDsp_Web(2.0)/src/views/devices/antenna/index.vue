<template>
  <PageLayout>
    <div class="antenna-management">
      <!-- 搜索栏 -->
      <div class="search-bar">
        <div class="search-group">
          <el-input
            v-model="searchKeyword"
            placeholder="天线编号/设备编号"
            style="width: 200px"
            clearable
            @clear="handleSearch"
            @keyup.enter="handleSearch"
          />
          <el-select
            v-model="deviceFilter"
            placeholder="关联设备"
            style="width: 180px"
            @change="handleSearch"
            filterable
            clearable
          >
            <el-option label="全部设备" value="" />
            <el-option
              v-for="item in deviceList"
              :key="item.id"
              :label="item.deviceNum"
              :value="item.id"
            />
          </el-select>
        </div>
        <el-button type="primary" @click="handleSearch">查询</el-button>
      </div>

      <!-- 卡片 -->
      <div class="card">
        <div class="card-header">
          <div class="header-buttons">
            <el-button type="primary" @click="handleAdd">
              <el-icon><Plus /></el-icon>
              新增天线配置
            </el-button>
            <el-button @click="handleImport">
              <el-icon><Upload /></el-icon>
              文件导入
            </el-button>
            <el-button @click="handleDownloadTemplate">
              <el-icon><Download /></el-icon>
              模板下载
            </el-button>
          </div>
        </div>

        <div class="table-wrapper">
          <el-table :data="antennaList" stripe style="width: 100%" v-loading="loading">
            <el-table-column prop="antennaNum" label="天线编号" width="150" />
            <el-table-column label="关联设备" width="150">
              <template #default="scope">
                {{ getDeviceNum(scope.row.deviceId) || '-' }}
              </template>
            </el-table-column>
            <el-table-column label="发射线圈参数" width="200">
              <template #default="scope">
                {{ scope.row.sendcoilLen || '-' }}×{{ scope.row.sendcoilWidth || '-' }}m
                匝数: {{ scope.row.sendcoilTurns || '-' }}T
              </template>
            </el-table-column>
            <el-table-column label="接收线圈参数" width="180">
              <template #default="scope">
                尺寸: {{ scope.row.recvcoilSize || '-' }}m
                增益: {{ scope.row.recvcoilGain || '-' }}
              </template>
            </el-table-column>
            <el-table-column prop="offtime" label="关断时间(ms)" width="120" />
            <el-table-column label="点距" width="120">
              <template #default="scope">
                D: {{ scope.row.pointlenD || '-' }} / R: {{ scope.row.pointlenR || '-' }}m
              </template>
            </el-table-column>
            <el-table-column prop="workConfig" label="工作配置" width="100" />
            <el-table-column prop="note" label="备注" min-width="120" show-overflow-tooltip />
            <el-table-column prop="createTime" label="创建时间" width="160" />
            <el-table-column label="操作" width="150" fixed="right">
              <template #default="scope">
                <el-button type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
                <el-button type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>

      <!-- 分页 -->
      <div class="pagination-footer">
        <span class="total-text">共 {{ total }} 条</span>
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="prev, pager, next, sizes, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>

      <!-- 新增/编辑弹窗 -->
      <el-dialog
        v-model="dialogVisible"
        :title="dialogTitle"
        width="550px"
        @close="handleDialogClose"
      >
        <el-form
          ref="formRef"
          :model="formData"
          :rules="formRules"
          label-width="120px"
        >
          <el-form-item label="天线编号" prop="antennaNum">
            <el-input v-model="formData.antennaNum" placeholder="请输入天线编号" />
          </el-form-item>
          <el-form-item label="关联设备" prop="deviceId">
            <el-select
              v-model="formData.deviceId"
              placeholder="请选择关联设备"
              style="width: 100%"
              filterable
              clearable
            >
              <el-option
                v-for="item in deviceList"
                :key="item.id"
                :label="item.deviceNum"
                :value="item.id"
              />
            </el-select>
          </el-form-item>

          <div class="config-section-title">—— 发射线圈参数 ——</div>
          <el-form-item label="发射线圈长度(m)" prop="sendcoilLen">
            <el-input-number
              v-model="formData.sendcoilLen"
              :precision="2"
              :step="0.1"
              style="width: 100%"
            />
          </el-form-item>
          <el-form-item label="发射线圈宽度(m)" prop="sendcoilWidth">
            <el-input-number
              v-model="formData.sendcoilWidth"
              :precision="2"
              :step="0.1"
              style="width: 100%"
            />
          </el-form-item>
          <el-form-item label="发射线圈匝数" prop="sendcoilTurns">
            <el-input-number
              v-model="formData.sendcoilTurns"
              :precision="0"
              :step="1"
              style="width: 100%"
            />
          </el-form-item>

          <div class="config-section-title">—— 接收线圈参数 ——</div>
          <el-form-item label="接收线圈尺寸(m)" prop="recvcoilSize">
            <el-input-number
              v-model="formData.recvcoilSize"
              :precision="2"
              :step="0.1"
              style="width: 100%"
            />
          </el-form-item>
          <el-form-item label="接收线圈增益" prop="recvcoilGain">
            <el-input-number
              v-model="formData.recvcoilGain"
              :precision="2"
              :step="0.5"
              style="width: 100%"
            />
          </el-form-item>

          <div class="config-section-title">—— 采集参数 ——</div>
          <el-form-item label="关断时间(ms)" prop="offtime">
            <el-input-number
              v-model="formData.offtime"
              :precision="2"
              :step="0.1"
              style="width: 100%"
            />
          </el-form-item>
          <el-form-item label="点距D值(m)" prop="pointlenD">
            <el-input-number
              v-model="formData.pointlenD"
              :precision="2"
              :step="0.1"
              style="width: 100%"
            />
          </el-form-item>
          <el-form-item label="点距R值(m)" prop="pointlenR">
            <el-input-number
              v-model="formData.pointlenR"
              :precision="2"
              :step="0.1"
              style="width: 100%"
            />
          </el-form-item>
          <el-form-item label="工作配置" prop="workConfig">
            <el-input-number
              v-model="formData.workConfig"
              :precision="0"
              :step="1"
              style="width: 100%"
            />
          </el-form-item>
          <el-form-item label="备注" prop="note">
            <el-input
              v-model="formData.note"
              type="textarea"
              placeholder="请输入备注"
            />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">确定</el-button>
        </template>
      </el-dialog>
    </div>
  </PageLayout>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from "vue";
import { useRoute } from "vue-router";
import { ElMessage, ElMessageBox } from "element-plus";
import { Plus, Upload, Download } from "@element-plus/icons-vue";
import PageLayout from "@/components/PageLayout.vue";
import type { FormInstance, FormRules } from "element-plus";
// ========== 真实后端API ==========
import api from "@/api";
// ================================

const route = useRoute();

// ==================== 类型定义 ====================

// 天线配置数据类型（对应 data_workset 表）
interface AntennaConfig {
  id: number;
  uuid: string;
  deviceId: number | null;
  antennaNum: string;
  workConfig?: number | null;
  sendcoilLen?: number | null;
  sendcoilWidth?: number | null;
  sendcoilTurns?: number | null;
  recvcoilSize?: number | null;
  recvcoilGain?: number | null;
  offtime?: number | null;
  pointlenD?: number | null;
  pointlenR?: number | null;
  note?: string | null;
  createTime: string;
}

// 设备数据类型（用于关联设备下拉框）
interface Device {
  id: number;
  deviceNum: string;
}

// ==================== Mock 数据 ====================

// 设备列表 Mock（用于关联设备下拉框）
const mockDeviceList: Device[] = [
  { id: 1, deviceNum: "DEV-001" },
  { id: 2, deviceNum: "DEV-002" },
  { id: 3, deviceNum: "DEV-003" },
  { id: 4, deviceNum: "DEV-004" },
  { id: 5, deviceNum: "DEV-005" },
  { id: 6, deviceNum: "DEV-006" },
  { id: 7, deviceNum: "DEV-007" },
  { id: 8, deviceNum: "DEV-008" },
];

// 天线配置 Mock 数据
const mockAntennaList: AntennaConfig[] = [
  {
    id: 1,
    uuid: "ant-001",
    deviceId: 1,
    antennaNum: "ANT-001",
    sendcoilLen: 1.5,
    sendcoilWidth: 1.5,
    sendcoilTurns: 10,
    recvcoilSize: 0.3,
    recvcoilGain: 8.5,
    offtime: 0.5,
    pointlenD: 0.5,
    pointlenR: 0.2,
    workConfig: 1,
    note: "标准配置",
    createTime: "2024-01-15 10:30:00",
  },
  {
    id: 2,
    uuid: "ant-002",
    deviceId: 2,
    antennaNum: "ANT-002",
    sendcoilLen: 2.0,
    sendcoilWidth: 2.0,
    sendcoilTurns: 12,
    recvcoilSize: 0.4,
    recvcoilGain: 10.0,
    offtime: 0.6,
    pointlenD: 0.6,
    pointlenR: 0.25,
    workConfig: 1,
    note: "高精度配置",
    createTime: "2024-01-16 14:20:00",
  },
  {
    id: 3,
    uuid: "ant-003",
    deviceId: null,
    antennaNum: "ANT-003",
    sendcoilLen: 1.8,
    sendcoilWidth: 1.8,
    sendcoilTurns: 8,
    recvcoilSize: 0.35,
    recvcoilGain: 7.5,
    offtime: 0.4,
    pointlenD: 0.4,
    pointlenR: 0.15,
    workConfig: 2,
    note: "备用配置",
    createTime: "2024-01-17 09:15:00",
  },
];

let nextAntennaId = 4;
let nextAntennaUuidIndex = 4;

// ==================== 响应式数据 ====================

const antennaList = ref<AntennaConfig[]>([]);
const deviceList = ref<Device[]>([]);
const loading = ref(false);

const searchKeyword = ref("");
const deviceFilter = ref("");

const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(0);

const dialogVisible = ref(false);
const isEdit = ref(false);
const editId = ref<number | null>(null);
const editUuid = ref("");
const formRef = ref<FormInstance>();

const formData = reactive({
  antennaNum: "",
  deviceId: null as number | null,
  sendcoilLen: undefined as number | undefined,
  sendcoilWidth: undefined as number | undefined,
  sendcoilTurns: undefined as number | undefined,
  recvcoilSize: undefined as number | undefined,
  recvcoilGain: undefined as number | undefined,
  offtime: undefined as number | undefined,
  pointlenD: undefined as number | undefined,
  pointlenR: undefined as number | undefined,
  workConfig: undefined as number | undefined,
  note: "",
});

const formRules: FormRules = {
  antennaNum: [{ required: true, message: "请输入天线编号", trigger: "blur" }],
};

// ==================== 计算属性 ====================

const dialogTitle = computed(() => (isEdit.value ? "编辑天线配置" : "新增天线配置"));

// ==================== 辅助方法 ====================

// 根据设备ID获取设备编号
const getDeviceNum = (deviceId: number | null) => {
  if (!deviceId) return "-";
  const device = deviceList.value.find(d => d.id === deviceId);
  return device?.deviceNum || "-";
};

// ==================== API 调用 ====================

// ==================== 真实 API 模式 ====================

const fetchDeviceList = async () => {
  try {
    const response = await api.device.getList({ pageNum: 1, pageSize: 1000 }) as any;
    if (response.code === 200) {
      deviceList.value = (response.data.records || []).map((d: any) => ({
        id: d.id,
        deviceNum: d.deviceNum
      }));
    }
  } catch (error) {
    console.error("获取设备列表错误:", error);
  }
};

const fetchAntennaList = async () => {
  loading.value = true;
  try {
    const params: any = {
      pageNum: currentPage.value,
      pageSize: pageSize.value,
    };
    if (searchKeyword.value) params.keyword = searchKeyword.value;

    const response = await api.dataWorkset.getList(params) as any;
    if (response.code === 200) {
      antennaList.value = response.data.records || [];
      total.value = response.data.total || 0;
    } else {
      ElMessage.error(response.message || "获取天线配置列表失败");
    }
  } catch (error) {
    console.error("获取天线配置列表错误:", error);
    ElMessage.error("获取天线配置列表失败");
  } finally {
    loading.value = false;
  }
};

const addAntenna = async (data: any) => {
  const response = await api.dataWorkset.add(data) as any;
  return response;
};

const updateAntenna = async (data: any) => {
  const response = await api.dataWorkset.update(data.uuid, data) as any;
  return response;
};

const deleteAntenna = async (uuid: string) => {
  const response = await api.dataWorkset.delete(uuid) as any;
  return response;
};

// ==================== 页面交互方法 ====================

const resetForm = () => {
  if (formRef.value) {
    formRef.value.resetFields();
  }
  formData.antennaNum = "";
  formData.deviceId = null;
  formData.sendcoilLen = undefined;
  formData.sendcoilWidth = undefined;
  formData.sendcoilTurns = undefined;
  formData.recvcoilSize = undefined;
  formData.recvcoilGain = undefined;
  formData.offtime = undefined;
  formData.pointlenD = undefined;
  formData.pointlenR = undefined;
  formData.workConfig = undefined;
  formData.note = "";
  isEdit.value = false;
  editId.value = null;
  editUuid.value = "";
};

const handleAdd = () => {
  resetForm();
  dialogVisible.value = true;
};

const handleEdit = (row: AntennaConfig) => {
  resetForm();
  isEdit.value = true;
  editId.value = row.id;
  editUuid.value = row.uuid;
  formData.antennaNum = row.antennaNum;
  formData.deviceId = row.deviceId;
  formData.sendcoilLen = row.sendcoilLen ?? undefined;
  formData.sendcoilWidth = row.sendcoilWidth ?? undefined;
  formData.sendcoilTurns = row.sendcoilTurns ?? undefined;
  formData.recvcoilSize = row.recvcoilSize ?? undefined;
  formData.recvcoilGain = row.recvcoilGain ?? undefined;
  formData.offtime = row.offtime ?? undefined;
  formData.pointlenD = row.pointlenD ?? undefined;
  formData.pointlenR = row.pointlenR ?? undefined;
  formData.workConfig = row.workConfig ?? undefined;
  formData.note = row.note || "";
  dialogVisible.value = true;
};

const handleDelete = async (row: AntennaConfig) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除天线配置 "${row.antennaNum}" 吗？`,
      "提示",
      {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }
    );

    const response = await deleteAntenna(row.uuid);
    if (response.code === 200) {
      ElMessage.success(response.message || "删除成功");
      await fetchAntennaList();
    } else {
      ElMessage.error(response.message || "删除失败");
    }
  } catch (error) {
    if (error !== "cancel") {
      console.error("删除失败:", error);
      ElMessage.error("删除失败");
    }
  }
};

const handleSubmit = async () => {
  if (!formRef.value) return;

  try {
    await formRef.value.validate();

    const submitData = {
      antennaNum: formData.antennaNum,
      deviceId: formData.deviceId,
      sendcoilLen: formData.sendcoilLen,
      sendcoilWidth: formData.sendcoilWidth,
      sendcoilTurns: formData.sendcoilTurns,
      recvcoilSize: formData.recvcoilSize,
      recvcoilGain: formData.recvcoilGain,
      offtime: formData.offtime,
      pointlenD: formData.pointlenD,
      pointlenR: formData.pointlenR,
      workConfig: formData.workConfig,
      note: formData.note,
    };

    let response;
    if (isEdit.value) {
      response = await updateAntenna({
        id: editId.value,
        uuid: editUuid.value,
        ...submitData,
      });
    } else {
      response = await addAntenna(submitData);
    }

    if (response.code === 200) {
      ElMessage.success(response.message || (isEdit.value ? "修改成功" : "添加成功"));
      dialogVisible.value = false;
      await fetchAntennaList();
    } else {
      ElMessage.error(response.message || "操作失败");
    }
  } catch (error) {
    console.error("提交失败:", error);
    ElMessage.error("操作失败");
  }
};

const handleImport = () => {
  ElMessage.info("文件导入功能开发中");
};

const handleDownloadTemplate = () => {
  ElMessage.info("模板下载功能开发中");
};

const handleSearch = () => {
  currentPage.value = 1;
  fetchAntennaList();
};

const handleSizeChange = (size: number) => {
  pageSize.value = size;
  currentPage.value = 1;
  fetchAntennaList();
};

const handleCurrentChange = (page: number) => {
  currentPage.value = page;
  fetchAntennaList();
};

const handleDialogClose = () => {
  resetForm();
};

// ==================== 生命周期 ====================

onMounted(async () => {
  await fetchDeviceList();
  await fetchAntennaList();
  
  // 处理从设备登记页面传递过来的 deviceId 参数
  const deviceId = route.query.deviceId;
  if (deviceId) {
    // 如果传入了设备ID，自动打开新增弹窗并选中该设备
    handleAdd();
    formData.deviceId = Number(deviceId);
  }
});
</script>

<style scoped>
.antenna-management {
  min-height: 100%;
  display: flex;
  flex-direction: column;
  padding: 0;
  background-color: #ffffff;
}

.search-bar {
  background: white;
  border-radius: 12px;
  padding: 12px 20px;
  margin-bottom: 12px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 12px;
}

.search-group {
  display: flex;
  gap: 12px;
  align-items: center;
  flex-wrap: wrap;
}

.card {
  display: flex;
  flex-direction: column;
  background: white;
  border-radius: 12px;
  overflow: visible;
}

.card-header {
  flex-shrink: 0;
  padding: 12px 20px;
  border-bottom: 1px solid #ebeef5;
  display: flex;
  justify-content: flex-start;
  align-items: center;
}

.header-buttons {
  display: flex;
  gap: 12px;
  align-items: center;
}

.table-wrapper {
  overflow-x: auto;
}

.pagination-footer {
  flex-shrink: 0;
  margin-top: 12px;
  padding: 12px 20px;
  background: white;
  border-radius: 8px;
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 16px;
  flex-wrap: wrap;
}

.total-text {
  color: #606266;
  font-size: 13px;
}

.config-section-title {
  font-weight: 600;
  color: #409eff;
  margin: 16px 0 12px 0;
  padding-left: 8px;
  border-left: 3px solid #409eff;
}
</style>