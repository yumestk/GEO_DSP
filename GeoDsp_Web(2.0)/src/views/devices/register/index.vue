<template>
  <PageLayout>
    <div class="device-register">
      <!-- Gfast 风格：一个白色盒子 -->
      <div class="g-card">
        <!-- 搜索栏 -->
        <div class="search-bar">
          <div class="search-group">
            <el-input
              v-model="searchKeyword"
              placeholder="设备编号"
              style="width: 200px"
              clearable
              @clear="handleSearch"
              @keyup.enter="handleSearch"
            />
            <el-select 
              v-model="statusFilter" 
              placeholder="全部状态" 
              style="width: 120px"
              clearable
              @change="handleSearch"
            >
              <el-option label="正常" :value="1" />
              <el-option label="禁用" :value="0" />
              <el-option label="占用" :value="2" />
            </el-select>
            <el-select 
              v-model="typeFilter" 
              placeholder="设备类型" 
              style="width: 140px"
              @change="handleSearch"
              filterable
              clearable
            >
              <el-option v-for="item in deviceTypeList" :key="item.id" :label="item.typeName" :value="item.id" />
            </el-select>
            <el-select 
              v-model="companyFilter" 
              placeholder="所属企业" 
              style="width: 160px"
              @change="handleSearch"
              filterable
              clearable
            >
              <el-option v-for="item in companyList" :key="item.id" :label="item.companyName" :value="item.id" />
            </el-select>
          </div>
          <div class="search-actions">
            <el-button type="success" @click="handleAntennaParamManage">
              <el-icon><Setting /></el-icon>
              天线参数管理
            </el-button>
            <el-button type="primary" @click="handleSearch">查询</el-button>
          </div>
        </div>

        <!-- 操作按钮栏 -->
        <div class="action-bar">
          <el-button  v-permission="'device:register:add'" type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            登记设备
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

        <!-- 设备列表表格 -->
        <div class="table-wrapper">
          <el-table :data="deviceList" stripe style="width: 100%" v-loading="loading">
            <el-table-column prop="deviceNum" label="设备编号" width="150" fixed="left" />
            <el-table-column label="设备类型" width="120">
              <template #default="scope">
                {{ getDeviceTypeName(scope.row.deviceTypeId) }}
              </template>
            </el-table-column>
            <el-table-column label="所属企业" width="150">
              <template #default="scope">
                {{ getCompanyName(scope.row.companyId) }}
              </template>
            </el-table-column>
            <!-- 天线配置信息列 -->
            <el-table-column label="天线配置信息" min-width="320">
              <template #default="scope">
                <div v-if="getAntennaConfig(scope.row.id) && (getAntennaConfig(scope.row.id) as any).antennaNum" class="antenna-config-cell">
                  <div class="config-line">天线编号: {{ (getAntennaConfig(scope.row.id) as any).antennaNum || '-' }}</div>
                  <div class="config-line">发射线圈: {{ (getAntennaConfig(scope.row.id) as any).sendcoilLen || '-' }}×{{ (getAntennaConfig(scope.row.id) as any).sendcoilWidth || '-' }}m  匝数: {{ (getAntennaConfig(scope.row.id) as any).sendcoilTurns || '-' }}T</div>
                  <div class="config-line">接收线圈: {{ (getAntennaConfig(scope.row.id) as any).recvcoilSize || '-' }}m  接收增益: {{ (getAntennaConfig(scope.row.id) as any).recvcoilGain || '-' }}</div>
                  <div class="config-line">关断时间: {{ (getAntennaConfig(scope.row.id) as any).offtime || '-' }}ms  点距D/R: {{ (getAntennaConfig(scope.row.id) as any).pointlenD || '-' }}/{{ (getAntennaConfig(scope.row.id) as any).pointlenR || '-' }}m</div>
                </div>
                <div v-else class="antenna-config-empty">
                  <span class="empty-text">未配置天线参数</span>
                  <el-link type="primary" @click="handleConfigAntenna(scope.row)">立即配置</el-link>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="100">
              <template #default="scope">
                <el-tag :type="getStatusType(scope.row.status)">
                  {{ getStatusText(scope.row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="note" label="备注" min-width="120" show-overflow-tooltip />
            <el-table-column prop="createTime" label="创建时间" width="170">
              <template #default="scope">
                <span class="nowrap">{{ formatDateTime(scope.row.createTime) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="240" fixed="right">
              <template #default="scope">
                <el-button v-permission="'device:register:edit'" type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
                <el-button type="info" size="small" @click="handleConfigAntenna(scope.row)">天线配置</el-button>
                <el-button v-permission="'device:register:delete'" type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
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
            layout="total, prev, pager, next, sizes, jumper"
            :total="total"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>

        <!-- 设备登记/编辑弹窗 -->
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
            <el-form-item label="设备编号" prop="deviceNum">
              <el-input v-model="formData.deviceNum" placeholder="请输入设备编号" />
            </el-form-item>
            <el-form-item label="设备类型" prop="deviceTypeId">
              <el-select v-model="formData.deviceTypeId" placeholder="请选择设备类型" style="width: 100%">
                <el-option v-for="item in deviceTypeList" :key="item.id" :label="item.typeName" :value="item.id" />
              </el-select>
            </el-form-item>
            <el-form-item label="所属企业" prop="companyId">
              <el-select v-model="formData.companyId" placeholder="请选择所属企业" style="width: 100%" filterable>
                <el-option v-for="item in companyList" :key="item.id" :label="item.companyName" :value="item.id" />
              </el-select>
            </el-form-item>
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="formData.status">
                <el-radio :label="1">正常</el-radio>
                <el-radio :label="0">禁用</el-radio>
                <el-radio :label="2">占用</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="备注" prop="note">
              <el-input v-model="formData.note" type="textarea" placeholder="请输入备注" />
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
import { ref, reactive, computed, onMounted } from "vue";
import { useRouter } from "vue-router";
import { ElMessage, ElMessageBox } from "element-plus";
import { Plus, Upload, Download, Setting } from "@element-plus/icons-vue";
import PageLayout from "@/components/PageLayout.vue";
import type { FormInstance, FormRules } from "element-plus";
import api from "@/api";
import { useUserStore } from "@/stores/modules/user";

const router = useRouter();
const userStore = useUserStore();

// ==================== 类型定义 ====================

interface DeviceType {
  id: number;
  uuid: string;
  typeName: string;
  typeDesc?: string;
}

interface Device {
  id: number;
  uuid: string;
  companyId: number;
  deviceNum: string;
  deviceTypeId: number;
  status: number;
  note?: string;
  createBy?: number;
  createTime: string;
  updateBy?: number;
  updateTime?: string;
  isDel?: boolean;
}

// 天线配置数据类型（用于展示）
interface AntennaConfig {
  id: number;
  uuid: string;
  deviceId: number;
  antennaNum: string;
  sendcoilLen?: number | null;
  sendcoilWidth?: number | null;
  sendcoilTurns?: number | null;
  recvcoilSize?: number | null;
  recvcoilGain?: number | null;
  offtime?: number | null;
  pointlenD?: number | null;
  pointlenR?: number | null;
  workConfig?: number | null;
  note?: string | null;
}

interface Company {
  id: number;
  uuid: string;
  companyName: string;
  companyAbbr?: string;
}

// ==================== Mock 数据 ====================

// ==================== 响应式数据 ====================

const deviceList = ref<Device[]>([]);
const deviceTypeList = ref<DeviceType[]>([]);
const companyList = ref<Company[]>([]);
const antennaConfigList = ref<AntennaConfig[]>([]);
const loading = ref(false);

const searchKeyword = ref("");
const statusFilter = ref("");
const typeFilter = ref("");
const companyFilter = ref("");

const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(0);

const dialogVisible = ref(false);
const isEdit = ref(false);
const editId = ref<number | null>(null);
const editUuid = ref("");
const formRef = ref<FormInstance>();

const formData = reactive({
  deviceNum: "",
  deviceTypeId: null as number | null,
  companyId: null as number | null,
  status: 1,
  note: "",
});

const formRules: FormRules = {
  deviceNum: [{ required: true, message: "请输入设备编号", trigger: "blur" }],
  deviceTypeId: [{ required: true, message: "请选择设备类型", trigger: "change" }],
  companyId: [{ required: true, message: "请选择所属企业", trigger: "change" }],
};

// ==================== 计算属性 ====================

const dialogTitle = computed(() => (isEdit.value ? "编辑设备" : "登记设备"));

// ==================== 辅助方法 ====================

const formatDateTime = (dateTime: string | undefined): string => {
  if (!dateTime) return "-";
  return dateTime.replace(/-/g, "/");
};

const getDeviceTypeName = (typeId: number) => {
  const type = deviceTypeList.value.find(t => t.id === typeId);
  return type?.typeName || "-";
};

const getCompanyName = (companyId: number) => {
  const company = companyList.value.find(c => c.id === companyId);
  return company?.companyName || "-";
};

// 通过设备ID获取天线配置
const getAntennaConfig = (deviceId: number): AntennaConfig | undefined => {
  return antennaConfigList.value.find(c => c.deviceId === deviceId);
};

const getStatusText = (status: number) => {
  const map: Record<number, string> = { 0: "禁用", 1: "正常", 2: "占用" };
  return map[status] || "未知";
};

const getStatusType = (status: number) => {
  const map: Record<number, string> = { 0: "danger", 1: "success", 2: "warning" };
  return map[status] || "info";
};

// ==================== API 调用 ====================

// ---------- API 调用 ----------

const fetchDeviceTypeList = async () => {
  try {
    const res = await api.device.getTypeList();
    deviceTypeList.value = res.data ?? [];
  } catch (error) {
    console.error("获取设备类型列表失败:", error);
  }
};

const fetchCompanyList = async () => {
  try {
    const res = await api.company.getList({ pageNum: 1, pageSize: 999 });
    companyList.value = res.data?.records ?? [];
  } catch (error) {
    console.error("获取企业列表失败:", error);
  }
};

const fetchAntennaConfigList = async () => {
  antennaConfigList.value = [];
};

const fetchDeviceList = async () => {
  loading.value = true;
  try {
    const params: any = { pageNum: currentPage.value, pageSize: pageSize.value };
    if (searchKeyword.value) params.deviceNum = searchKeyword.value;
    if (statusFilter.value !== "") params.status = Number(statusFilter.value);
    if (typeFilter.value !== "") params.deviceTypeId = Number(typeFilter.value);
    const res = await api.device.getList(params);
    deviceList.value = res.data?.records ?? [];
    total.value = res.data?.total ?? 0;
  } catch (error) {
    console.error("获取设备列表错误:", error);
    ElMessage.error("获取设备列表失败");
  } finally {
    loading.value = false;
  }
};

// ==================== 页面交互方法 ====================

const resetForm = () => {
  if (formRef.value) {
    formRef.value.resetFields();
  }
  formData.deviceNum = "";
  formData.deviceTypeId = null;
  formData.companyId = null;
  formData.status = 1;
  formData.note = "";
  isEdit.value = false;
  editId.value = null;
  editUuid.value = "";
};

const handleAdd = () => {
  resetForm();
  dialogVisible.value = true;
};

const handleEdit = (row: Device) => {
  resetForm();
  isEdit.value = true;
  editId.value = row.id;
  editUuid.value = row.uuid;
  formData.deviceNum = row.deviceNum;
  formData.deviceTypeId = row.deviceTypeId;
  formData.companyId = row.companyId;
  formData.status = row.status;
  formData.note = row.note || "";
  dialogVisible.value = true;
};

const handleDelete = async (row: Device) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除设备 "${row.deviceNum}" 吗？`,
      "提示",
      {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }
    );

    const res = await api.device.delete(row.uuid, 1);
    if (res.code === 200) {
      ElMessage.success(res.msg || "删除成功");
      await fetchDeviceList();
    } else {
      ElMessage.error(res.msg || "删除失败");
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
      companyId: Number(formData.companyId),
      deviceNum: formData.deviceNum,
      deviceTypeId: Number(formData.deviceTypeId),
      status: formData.status,
      note: formData.note || "",
      operateBy: userStore.userState.userInfo?.id || 1,
    };

    let res;
    if (isEdit.value) {
      res = await api.device.update(editUuid.value, submitData);
    } else {
      res = await api.device.add(submitData);
    }

    if (res.code === 200) {
      ElMessage.success(res.msg || (isEdit.value ? "修改成功" : "添加成功"));
      dialogVisible.value = false;
      await fetchDeviceList();
    } else {
      ElMessage.error(res.msg || "操作失败");
    }
  } catch (error) {
    console.error("提交失败:", error);
    ElMessage.error("操作失败");
  }
};

// 配置天线 - 跳转到天线维护页面，并携带设备ID
const handleConfigAntenna = (row: Device) => {
  router.push(`/device/antenna?deviceId=${row.id}`);
};

// 天线参数管理 - 跳转到天线维护页面（不携带ID）
const handleAntennaParamManage = () => {
  router.push("/device/antenna");
};

const handleImport = () => {
  ElMessage.info("文件导入功能开发中");
};

const handleDownloadTemplate = () => {
  ElMessage.info("模板下载功能开发中");
};

const handleSearch = () => {
  currentPage.value = 1;
  fetchDeviceList();
};

const handleSizeChange = (size: number) => {
  pageSize.value = size;
  currentPage.value = 1;
  fetchDeviceList();
};

const handleCurrentChange = (page: number) => {
  currentPage.value = page;
  fetchDeviceList();
};

const handleDialogClose = () => {
  resetForm();
};

onMounted(async () => {
  await fetchDeviceTypeList();
  await fetchCompanyList();
  await fetchAntennaConfigList();
  await fetchDeviceList();
});
</script>

<style scoped>
.device-register {
  height: 100%;
}

/* 天线配置信息样式 */
.antenna-config-cell {
  font-size: 12px;
  line-height: 1.6;
}

.config-line {
  white-space: nowrap;
}

.antenna-config-empty {
  display: flex;
  align-items: center;
  gap: 8px;
}

.empty-text {
  color: #909399;
  font-size: 12px;
}

/* 时间不换行 */
.nowrap {
  white-space: nowrap;
}
</style>