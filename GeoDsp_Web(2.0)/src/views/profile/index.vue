<template>
  <div class="profile">
    <div class="page-header">
      <h2>个人中心</h2>
      <p>查看和管理您的个人信息</p>
    </div>

    <div class="profile-info">
      <el-card>
        <template #header>
          <div class="card-header">
            <span>基本信息</span>
          </div>
        </template>
        <el-descriptions :column="1" border>
          <el-descriptions-item label="ID">{{
            userInfo.id
          }}</el-descriptions-item>
          <el-descriptions-item label="用户名">{{
            userInfo.username
          }}</el-descriptions-item>
          <el-descriptions-item label="角色">{{
            userInfo.nickname
          }}</el-descriptions-item>
          <el-descriptions-item label="邮箱">{{
            userInfo.email
          }}</el-descriptions-item>
          <el-descriptions-item label="手机号">{{
            userInfo.phone
          }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="userInfo.status === 'active' ? 'success' : 'danger'">
              {{ userInfo.status === "active" ? "正常" : "禁用" }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">{{
            userInfo.createTime
          }}</el-descriptions-item>
        </el-descriptions>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import { useUserStore } from "../../stores/modules/user";
const userStore = useUserStore();

interface UserInfo {
  id: number;
  username: string;
  nickname: string;
  email: string;
  phone: string;
  avatar?: string;
  roles?: string[];
  permissions?: string[];
  status: string;
  createTime: string;
}

const userInfo = ref<UserInfo>({
  id: 0,
  username: "",
  nickname: "",
  email: "",
  phone: "",
  status: "",
  createTime: "",
});

const initUserInfo = async () => {
  try {
    const result = await userStore.getUserInfo();
    if (result.success && result.data) {
      userInfo.value = {
        ...result.data,
        status: result.data.status || "active",
        createTime: result.data.createTime || "2023-01-01 10:00:00",
      };
    } else {
      userInfo.value = {
        id: 1,
        username: "admin",
        nickname: "管理员",
        email: "admin@example.com",
        phone: "13800138000",
        avatar: "",
        roles: ["admin"],
        permissions: ["system:manage", "user:manage", "role:manage"],
        status: "active",
        createTime: "2023-01-01 10:00:00",
      };
    }
  } catch (error) {
    console.error("获取用户信息失败:", error);
    userInfo.value = {
      id: 1,
      username: "admin",
      nickname: "管理员",
      email: "admin@example.com",
      phone: "13800138000",
      avatar: "",
      roles: ["admin"],
      permissions: ["system:manage", "user:manage", "role:manage"],
      status: "active",
      createTime: "2023-01-01 10:00:00",
    };
  }
};

onMounted(async () => {
  await initUserInfo();
});
</script>

<style scoped>
/* 个人中心主容器 */
.profile {
  padding: 20px;
}

/* 页面头部 */
.page-header {
  margin-bottom: 20px;
}

/* 页面标题 */
.page-header h2 {
  margin-bottom: 10px;
  color: #303133;
}

/* 页面描述文字 */
.page-header p {
  margin: 0;
  color: #909399;
}

/* 个人信息区域 */
.profile-info {
  max-width: 800px;
}

/* 卡片头部 */
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

/* Element Plus 描述列表组件 */
:deep(.el-descriptions) {
  margin-bottom: 20px;
}

/* 描述列表标签 */
:deep(.el-descriptions__label) {
  font-weight: bold;
}
</style>
