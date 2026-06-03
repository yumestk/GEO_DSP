<!--
  企业管理页面
  功能：企业信息的增删改查，支持关键词搜索和后端分页
  接口：/api/system/companies/*
-->
<template>
  <PageLayout>
    <div class="enterprise-management">
      <!-- Gfast 风格：一个白色盒子 -->
      <div class="g-card">
        <!-- 搜索栏 -->
        <div class="search-bar">
          <div class="search-group">
            <el-input
              v-model="searchKeyword"
              placeholder="企业全称/简称/联系人"
              style="width: 220px"
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
          </div>
          <el-button type="primary" @click="handleSearch">查询</el-button>
        </div>

        <!-- 操作按钮栏 -->
        <div class="action-bar">
          <el-button v-permission="'company:add'" type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            添加企业
          </el-button>
        </div>

        <!-- 企业列表表格 -->
        <div class="table-wrapper">
          <el-table :data="displayList" stripe v-loading="loading">
            <el-table-column prop="companyName" label="企业全称" min-width="180" />
            <el-table-column prop="companyAbbr" label="企业简称" width="120" />
            <el-table-column prop="licenseNum" label="统一社会信用代码" width="180" />
            <el-table-column prop="contactName" label="联系人" width="100" />
            <el-table-column prop="contactPhone" label="联系电话" width="150">
              <template #default="scope">
                <div class="phone-cell">
                  <span class="phone-text">{{ formatPhoneNumber(scope.row.contactPhone, scope.row.showPhone) }}</span>
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
            <el-table-column prop="contactEmail" label="联系邮箱" min-width="160" />
            <el-table-column label="状态" width="80">
              <template #default="scope">
                <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
                  {{ scope.row.status === 1 ? "正常" : "禁用" }}
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
                <el-button v-permission="'company:edit'" size="small" @click="handleEdit(scope.row)">编辑</el-button>
                <el-button v-permission="'company:delete'" size="small" type="danger" @click="handleDelete(scope.row)">删除</el-button>
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

        <!-- 新增/编辑企业弹窗 -->
        <el-dialog
          v-model="dialogVisible"
          :title="dialogTitle"
          width="600px"
          @close="handleDialogClose"
        >
          <el-form
            ref="enterpriseFormRef"
            :model="enterpriseForm"
            :rules="enterpriseRules"
            label-width="120px"
          >
            <el-form-item label="企业全称" prop="companyName">
              <el-input v-model="enterpriseForm.companyName" placeholder="请输入企业全称" />
            </el-form-item>
            <el-form-item label="企业简称" prop="companyAbbr">
              <el-input v-model="enterpriseForm.companyAbbr" placeholder="请输入企业简称" />
            </el-form-item>
            <el-form-item label="统一社会信用代码" prop="licenseNum">
              <el-input v-model="enterpriseForm.licenseNum" placeholder="请输入统一社会信用代码" />
            </el-form-item>
            <el-form-item label="联系人姓名" prop="contactName">
              <el-input v-model="enterpriseForm.contactName" placeholder="请输入联系人姓名" />
            </el-form-item>
            <el-form-item label="联系人电话" prop="contactPhone">
              <el-input v-model="enterpriseForm.contactPhone" placeholder="请输入联系人电话" />
            </el-form-item>
            <el-form-item label="联系人邮箱" prop="contactEmail">
              <el-input v-model="enterpriseForm.contactEmail" placeholder="请输入联系人邮箱" />
            </el-form-item>
            <el-form-item label="企业地址" prop="address">
              <el-input v-model="enterpriseForm.address" type="textarea" :rows="2" placeholder="请输入企业地址" />
            </el-form-item>
            <el-form-item label="备注" prop="note">
              <el-input v-model="enterpriseForm.note" type="textarea" :rows="2" placeholder="请输入备注" />
            </el-form-item>
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="enterpriseForm.status">
                <el-radio :label="1">正常</el-radio>
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
 * 企业管理页面
 * 接口：api.company (getList / create / update / delete)
 * 联调时：将 USE_MOCK 改为 false，取消注释 API 代码
 */
import { ref, reactive, computed, onMounted } from "vue";
import PageLayout from "@/components/PageLayout.vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { Plus, View, Hide } from "@element-plus/icons-vue";
import type { FormInstance, FormRules } from "element-plus";
import api from "@/api";

// ==================== 配置开关 ====================
const USE_MOCK = false;

// ==================== 类型定义 ====================
interface Enterprise {
  id: number;
  uuid: string;
  companyName: string;
  companyAbbr: string;
  status: number;
  contactName: string;
  contactPhone: string;
  contactEmail?: string;
  address?: string;
  licenseNum: string;
  createBy: number;
  createTime: string;
  updateBy?: number;
  updateTime?: string;
  note?: string;
  isDel: boolean;
  showPhone?: boolean;
}

// ==================== 响应式数据 ====================
const enterpriseList = ref<Enterprise[]>([]);
const loading = ref(false);
const searchKeyword = ref("");
const statusFilter = ref<number | null>(null);
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(0);
const dialogVisible = ref(false);
const isEdit = ref(false);
const enterpriseFormRef = ref<FormInstance>();

const enterpriseForm = reactive({
  id: 0,
  uuid: "",
  companyName: "",
  companyAbbr: "",
  status: 1,
  contactName: "",
  contactPhone: "",
  contactEmail: "",
  address: "",
  licenseNum: "",
  note: "",
});

const enterpriseRules: FormRules = {
  companyName: [{ required: true, message: "请输入企业全称", trigger: "blur" }],
  companyAbbr: [{ required: true, message: "请输入企业简称", trigger: "blur" }],
  licenseNum: [{ required: true, message: "请输入统一社会信用代码", trigger: "blur" }],
  contactName: [{ required: true, message: "请输入联系人姓名", trigger: "blur" }],
  contactPhone: [
    { required: true, message: "请输入联系人电话", trigger: "blur" },
    { pattern: /^1[3-9]\d{9}$/, message: "请输入正确的手机号码", trigger: "blur" }
  ],
};

// ==================== 计算属性 ====================
const dialogTitle = computed(() => (isEdit.value ? "编辑企业" : "添加企业"));

const displayList = computed(() => {
  let list = [...enterpriseList.value];
  
  if (searchKeyword.value) {
    const kw = searchKeyword.value.toLowerCase();
    list = list.filter(item => 
      item.companyName.toLowerCase().includes(kw) ||
      item.companyAbbr.toLowerCase().includes(kw) ||
      item.contactName.toLowerCase().includes(kw)
    );
  }
  if (statusFilter.value !== null) {
    list = list.filter(item => item.status === statusFilter.value);
  }
  
  total.value = list.length;
  const start = (currentPage.value - 1) * pageSize.value;
  return list.slice(start, start + pageSize.value);
});

// ==================== 模拟数据 ====================
const mockEnterpriseList: Enterprise[] = [
  {
    id: 1, uuid: "mock-001", companyName: "北京物探科技有限公司", companyAbbr: "物探科技",
    licenseNum: "91110000MA00123456", contactName: "张建国", contactPhone: "13800138001",
    contactEmail: "zhangjg@example.com", address: "北京市朝阳区", status: 1,
    createBy: 1, createTime: "2025-01-01 10:00:00", isDel: false,
  },
  {
    id: 2, uuid: "mock-002", companyName: "上海地质勘查有限公司", companyAbbr: "上海地勘",
    licenseNum: "91310000MA00789123", contactName: "李丽华", contactPhone: "13900139002",
    contactEmail: "lilh@example.com", address: "上海市浦东新区", status: 1,
    createBy: 1, createTime: "2025-02-01 10:00:00", isDel: false,
  },
  {
    id: 3, uuid: "mock-003", companyName: "广州测绘工程有限公司", companyAbbr: "广州测绘",
    licenseNum: "91440000MA00456789", contactName: "王德明", contactPhone: "13700137003",
    contactEmail: "wangdm@example.com", address: "广州市天河区", status: 0,
    createBy: 1, createTime: "2025-03-01 10:00:00", isDel: false,
  },
  {
    id: 4, uuid: "mock-004", companyName: "深圳物探装备有限公司", companyAbbr: "深圳物探",
    licenseNum: "91440300MA00123456", contactName: "陈思远", contactPhone: "13612345678",
    contactEmail: "chensy@example.com", address: "深圳市南山区", status: 1,
    createBy: 1, createTime: "2025-04-01 10:00:00", isDel: false,
  },
  {
    id: 5, uuid: "mock-005", companyName: "成都地质调查院", companyAbbr: "成都地调",
    licenseNum: "91510000MA00123456", contactName: "赵明远", contactPhone: "13512345678",
    contactEmail: "zhaomy@example.com", address: "成都市武侯区", status: 1,
    createBy: 1, createTime: "2025-05-01 10:00:00", isDel: false,
  },
];

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

const togglePhoneVisible = (row: Enterprise) => {
  row.showPhone = !row.showPhone;
};

const resetForm = () => {
  if (enterpriseFormRef.value) enterpriseFormRef.value.resetFields();
  enterpriseForm.id = 0;
  enterpriseForm.uuid = "";
  enterpriseForm.companyName = "";
  enterpriseForm.companyAbbr = "";
  enterpriseForm.status = 1;
  enterpriseForm.contactName = "";
  enterpriseForm.contactPhone = "";
  enterpriseForm.contactEmail = "";
  enterpriseForm.address = "";
  enterpriseForm.licenseNum = "";
  enterpriseForm.note = "";
};

const generateId = () => Date.now();
const generateUuid = () => Date.now().toString() + Math.random().toString(36).substring(2, 8);

// ==================== API 调用 ====================
const fetchEnterpriseList = async () => {
  loading.value = true;
  
  try {
    const response = await api.company.getList({
      keyword: searchKeyword.value || undefined,
      pageNum: currentPage.value,
      pageSize: pageSize.value
    }) as any;
    if (response.code === 200) {
      enterpriseList.value = (response.data.records || []).map((item: Enterprise) => ({ ...item, showPhone: false }));
      total.value = response.data.total || 0;
    }
  } catch (error) {
    ElMessage.error("获取企业列表失败");
  } finally {
    loading.value = false;
  }
};

// ==================== 页面交互 ====================
const handleSearch = () => { currentPage.value = 1; };
const handleSizeChange = (size: number) => { pageSize.value = size; currentPage.value = 1; };
const handleCurrentChange = (page: number) => { currentPage.value = page; };

const handleAdd = () => {
  isEdit.value = false;
  resetForm();
  dialogVisible.value = true;
};

const handleEdit = (row: Enterprise) => {
  isEdit.value = true;
  Object.assign(enterpriseForm, { ...row });
  dialogVisible.value = true;
};

const handleDelete = async (row: Enterprise) => {
  try {
    await ElMessageBox.confirm(`确定删除企业 "${row.companyName}" 吗？`, "提示", {
      confirmButtonText: "确定", cancelButtonText: "取消", type: "warning",
    });
    
    await api.company.delete(row.uuid);
    ElMessage.success("删除成功");
    if (displayList.value.length === 0 && currentPage.value > 1) currentPage.value--;
    await fetchEnterpriseList();
  } catch (error) {
    if (error !== "cancel") {
      console.error("删除企业错误:", error);
      ElMessage.error("删除失败，请检查网络连接");
    }
  }
};

const handleSubmit = async () => {
  if (!enterpriseFormRef.value) return;
  await enterpriseFormRef.value.validate();
  
  const createBy = getCurrentUserId();
  if (isEdit.value) {
    await api.company.update(enterpriseForm as any, createBy);
  } else {
    await api.company.create(enterpriseForm as any, createBy);
  }
  ElMessage.success(isEdit.value ? "修改成功" : "添加成功");
  dialogVisible.value = false;
  resetForm();
  await fetchEnterpriseList();
};

const handleDialogClose = () => { resetForm(); };

// ==================== 生命周期 ====================
onMounted(() => {
  fetchEnterpriseList();
});
</script>

<style scoped>
.enterprise-management {
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