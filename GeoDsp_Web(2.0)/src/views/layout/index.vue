<template>
  <el-container class="main-layout">
    <!-- 顶部导航栏 -->
    <el-header class="header">
      <div class="header-content">
        <div class="logo">
          <h2>GeoDsp 系统</h2>
        </div>
        <div class="user-info">
          <el-dropdown>
            <span class="el-dropdown-link">
              <el-icon><User /></el-icon>
              {{ currentUserName }}
              <el-icon class="el-icon--right"><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="handleProfile">个人中心</el-dropdown-item>
                <el-dropdown-item divided @click="handleLogout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </el-header>

    <el-container class="content-container">
      <!-- 侧边栏 -->
      <el-aside width="180px" class="sidebar">
        <div class="sidebar-scroll">
          <!-- 注意：删除了 router 属性，改为手动跳转 -->
          <el-menu
            :default-active="$route.path"
            class="main-menu"
            background-color="#304156"
            text-color="#bfcbd9"
            active-text-color="#409EFF"
          >
            <!-- 首页固定入口 -->
            <el-menu-item index="/" @click="router.push('/')">
              <el-icon><HomeFilled /></el-icon>
              <span>首页</span>
            </el-menu-item>

            <!-- 动态渲染菜单（过滤掉无组件路径且无子菜单的纯权限标记项） -->
            <template v-for="item in filteredMenus" :key="item.permissionCode">
              <!-- 有子菜单：显示 el-sub-menu -->
              <el-sub-menu
                v-if="item.children && item.children.length"
                :index="item.permissionCode"
              >
                <template #title>
                  <el-icon><component :is="getIconComponent(item.icon || 'Document')" /></el-icon>
                  <span>{{ item.permissionName }}</span>
                </template>
                <!-- 子菜单项：手动跳转，使用路由名称 -->
                <el-menu-item
                  v-for="child in item.children"
                  :key="child.permissionCode"
                  :index="child.permissionCode"
                  @click="router.push({ name: child.permissionCode })"
                >
                  <el-icon><component :is="getIconComponent(child.icon || 'Document')" /></el-icon>
                  <span>{{ child.permissionName }}</span>
                </el-menu-item>
              </el-sub-menu>

              <!-- 无子菜单：直接显示 el-menu-item，手动跳转 -->
              <el-menu-item
                v-else
                :index="item.permissionCode"
                @click="router.push({ name: item.permissionCode })"
              >
                <el-icon><component :is="getIconComponent(item.icon || 'Document')" /></el-icon>
                <span>{{ item.permissionName }}</span>
              </el-menu-item>
            </template>
          </el-menu>
        </div>
      </el-aside>

      <!-- 右侧内容区域 -->
      <el-main class="main-content">
        <div class="content-wrapper">
          <RouterView />
        </div>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import {
  User,
  ArrowDown,
  Setting,
  Monitor,
  Document,
  DataAnalysis,
  TrendCharts,
  OfficeBuilding,
  HomeFilled,
  Avatar,
  Key,
  Collection,
  Operation,
  Plus,
  View,
  Tools,
  InfoFilled,
  List,
  Select,
  PieChart,
} from "@element-plus/icons-vue";
import { computed } from "vue";
import { useRouter } from "vue-router";
import { useUserStore } from "../../stores/modules/user";
import { usePermissionStore } from "../../stores/modules/permission";
import { storeToRefs } from "pinia";
import { ElMessage } from "element-plus";

// 定义菜单项类型
interface MenuItem {
  permissionCode: string;
  permissionName: string;
  permissionType?: string;
  componentPath?: string;
  icon?: string;
  children?: MenuItem[];
}

const router = useRouter();
const userStore = useUserStore();

// 当前登录用户名
const currentUserName = computed(() => {
  return userStore.userState?.userInfo?.username || userStore.userState?.userInfo?.nickname || '用户';
});

// 获取权限菜单
const permissionStore = usePermissionStore();
const { menus } = storeToRefs(permissionStore);

// 过滤掉没有组件路径且无子菜单的菜单项（如 admin:all、test:perm 等纯权限标记）
const filteredMenus = computed(() => {
  return menus.value.filter((item: MenuItem) => {
    const hasChildren = item.children && item.children.length > 0;
    const hasComponent = item.componentPath && item.componentPath !== '';
    return hasChildren || hasComponent;
  });
});

// 图标映射（将字符串图标名转为组件）
const getIconComponent = (iconName: string) => {
  const iconMap: Record<string, any> = {
    Setting: Setting,
    Monitor: Monitor,
    Document: Document,
    DataAnalysis: DataAnalysis,
    User: User,
    Avatar: Avatar,
    Key: Key,
    OfficeBuilding: OfficeBuilding,
    Collection: Collection,
    Operation: Operation,
    Plus: Plus,
    View: View,
    Tools: Tools,
    InfoFilled: InfoFilled,
    List: List,
    TrendCharts: TrendCharts,
    Select: Select,
    PieChart: PieChart,
    HomeFilled: HomeFilled,
  };
  return iconMap[iconName] || Document;
};

const handleProfile = () => {
  router.push("/profile");
};

const handleLogout = async () => {
  try {
    await userStore.logout();
    ElMessage.success("退出登录成功");
    router.push("/login");
  } catch (error) {
    console.error("退出登录出错:", error);
    ElMessage.success("退出登录成功");
    router.push("/login");
  }
};
</script>

<style scoped>
/* 主布局容器 */
.main-layout {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

/* 顶部导航栏 - 固定在顶部 */
.header {
  background-color: #304156;
  color: white;
  padding: 0;
  height: 50px;
  flex-shrink: 0;
  z-index: 100;
}

/* 顶部导航栏内容区域 */
.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100%;
  padding: 0 24px;
}

/* 系统Logo标题 */
.logo h2 {
  margin: 0;
  color: white;
  font-size: 1.1rem;
  font-weight: 500;
}

/* 用户信息区域 */
.user-info {
  color: white;
}

/* 用户下拉菜单链接 */
.el-dropdown-link {
  color: white;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 8px;
}

/* 侧边栏和内容区域的容器 */
.content-container {
  flex: 1;
  overflow: hidden;
}

/* 侧边栏 */
.sidebar {
  background-color: #304156;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  height: 100%;
}

/* 侧边栏滚动容器 */
.sidebar-scroll {
  flex: 1;
  overflow-y: auto;
  overflow-x: hidden;
}

/* 自定义侧边栏滚动条样式 */
.sidebar-scroll::-webkit-scrollbar {
  width: 4px;
}

.sidebar-scroll::-webkit-scrollbar-track {
  background: #263445;
  border-radius: 4px;
}

.sidebar-scroll::-webkit-scrollbar-thumb {
  background: #409eff;
  border-radius: 4px;
}

.sidebar-scroll::-webkit-scrollbar-thumb:hover {
  background: #66b1ff;
}

/* 主菜单 */
.main-menu {
  border-right: none;
  height: auto;
  min-height: 100%;
}

/* 右侧主内容区域 */
.main-content {
  background-color: #ffffff;
  padding: 0;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  height: 100%;
}

/* 内容包装器 - 占满高度，不滚动，让内部页面自己控制 */
.content-wrapper {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  padding: 16px;
  min-height: 0;
  background-color: #ffffff;
}

/* Element Plus 子菜单项样式覆盖 */
:deep(.el-sub-menu .el-menu-item) {
  background-color: #1f2d3d;
  padding-left: 50px !important;
}

:deep(.el-sub-menu .el-menu-item:hover) {
  background-color: #001528 !important;
}

:deep(.el-sub-menu .el-menu-item.is-active) {
  background-color: #001528 !important;
}

:deep(.el-sub-menu__title:hover) {
  background-color: #263445 !important;
}
</style>