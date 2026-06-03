import { createRouter, createWebHistory } from "vue-router";

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: "/",
      name: "Layout",
      component: () => import("../views/layout/index.vue"),
      children: [
        {
          path: "",
          name: "Home",
          component: () => import("../views/layout/Home.vue"),
        },
        // 系统管理相关路由
        {
          path: "system",
          name: "System",
          component: () => import("../views/system/index.vue"),
          meta: { title: "系统管理", icon: "Setting" },
          children: [
            {
              path: "",
              name: "SystemOverview",
              component: () => import("../views/system/overview.vue"),
              meta: { title: "系统概览", icon: "Monitor" },
            },
            {
              path: "users",
              name: "SystemUsers",
              component: () => import("../views/system/users/index.vue"),
              meta: { title: "用户管理", icon: "User" },
            },
            {
              path: "roles",
              name: "SystemRoles",
              component: () => import("../views/system/roles/index.vue"),
              meta: { title: "角色管理", icon: "Avatar" },
            },
            {
              path: "permission",
              name: "SystemPermission",
              component: () => import("../views/system/permission/index.vue"),
              meta: { title: "权限管理", icon: "Key" },
            },
            // 字典管理路由
            {
              path: "/system/dictionary",
              name: "Dictionary",
              component: () => import("../views/system/dictionary/index.vue"),
              meta: { title: "字典管理" },
            },
            {
              path: "/system/dictionary/data",
              name: "DictionaryData",
              component: () => import("../views/system/dictionary/data.vue"),
              meta: { title: "字典数据管理" },
            },
            {
              path: "operator",
              name: "SystemOperator",
              component: () => import("../views/system/operator/index.vue"),
              meta: { title: "操作员管理", icon: "UserFilled" },
            },
            {
              path: "logs",
              name: "SystemLogs",
              component: () => import("../views/system/logs/index.vue"),
              meta: { title: "日志管理", icon: "Document" },
            },
            {
              path: "settings",
              name: "SystemSettings",
              component: () => import("../views/system/settings/index.vue"),
              meta: { title: "配置管理", icon: "Setting" },
            },
            {
              path: "enterprise",
              name: "EnterpriseManagement",
              component: () => import("../views/system/enterprise/index.vue"),
              meta: { title: "企业管理", icon: "OfficeBuilding" },
            },
          ],
        },
        // 设备管理相关的路由配置
        {
          path: "device",
          name: "Device",
          component: () => import("../views/devices/index.vue"),
          meta: { title: "设备管理", icon: "Monitor" },
          children: [
            {
              path: "",
              name: "DeviceOverview",
              component: () => import("../views/devices/overview.vue"),
              meta: { title: "设备概览", icon: "View" },
            },
            {
              path: "register",
              name: "DeviceRegister",
              component: () => import("../views/devices/register/index.vue"),
              meta: { title: "设备登记", icon: "Plus" },
            },
            {
              path: "monitor",
              name: "DeviceMonitor",
              component: () => import("../views/devices/monitor/index.vue"),
              meta: { title: "设备监控", icon: "Monitor" },
            },
            {
              path: "maintenance",
              name: "DeviceMaintenance",
              component: () => import("../views/devices/maintenance/index.vue"),
              meta: { title: "设备维护", icon: "Tools" },
            },
            {
              path: "analysis",
              name: "DeviceAnalysis",
              component: () => import("../views/devices/analysis/index.vue"),
              meta: { title: "设备分析", icon: "DataAnalysis" },
            },
            {
              path: "antenna",
              name: "DeviceAntenna",
              component: () => import("../views/devices/antenna/index.vue"),
              meta: { title: "天线设备维护", icon: "Connection" },
            }
          ],
        },
        // 项目管理相关的路由配置
        {
          path: "project",
          name: "Project",
          component: () => import("../views/projects/index.vue"),
          meta: { title: "项目管理", icon: "Folder" },
          children: [
            {
              path: "",
              name: "ProjectOverview",
              component: () => import("../views/projects/overview.vue"),
              meta: { title: "项目概览", icon: "View" },
            },
            {
              path: "info",
              name: "ProjectInfo",
              component: () => import("../views/projects/info/index.vue"),
              meta: { title: "项目信息", icon: "Document" },
            },
            {
              path: "tasks",
              name: "ProjectTasks",
              component: () => import("../views/projects/tasks/index.vue"),
              meta: { title: "项目任务", icon: "List" },
            },
            {
              path: "methods",
              name: "ProjectMethods",
              component: () => import("../views/projects/methods/index.vue"),
              meta: { title: "作业方法", icon: "Tools" },
            },
            {
              path: "progress",
              name: "ProjectProgress",
              component: () => import("../views/projects/progress/index.vue"),
              meta: { title: "项目进度", icon: "TrendCharts" },
            },
            {
              path: "logs",
              name: "ProjectLogs",
              component: () => import("../views/projects/logs/index.vue"),
              meta: { title: "项目日志", icon: "Document" },
            },
            {
              path: "reports",
              name: "ProjectReports",
              component: () => import("../views/projects/reports/index.vue"),
              meta: { title: "项目报告", icon: "Files" },
            },
          ],
        },
        // 数据管理相关路由
        {
          path: "data",
          name: "Data",
          component: () => import("../views/data/index.vue"),
          meta: { title: "数据管理", icon: "DataAnalysis" },
          children: [
            {
              path: "",
              name: "DataOverview",
              component: () => import("../views/data/overview.vue"),
              meta: { title: "数据概览", icon: "View" },
            },
            {
              path: "quality",
              name: "DataQuality",
              component: () => import("../views/data/quality/index.vue"),
              meta: { title: "数据质量", icon: "CircleCheck" },
            },
            {
              path: "tasks",
              name: "DataTasks",
              component: () => import("../views/data/tasks/index.vue"),
              meta: { title: "数据任务", icon: "List" },
            },
          ],
        },
        {
          path: "data/preview",
          name: "DataPreview",
          component: () => import("../views/data/preview.vue"),
          meta: { title: "数据预览", icon: "View" },
        },
        // 统计分析相关路由
        {
          path: "statistics",
          name: "Statistics",
          component: () => import("../views/statistics/index.vue"),
          meta: { title: "统计分析", icon: "PieChart" },
          children: [
            {
              path: "",
              name: "StatisticsOverview",
              component: () => import("../views/statistics/overview.vue"),
              meta: { title: "统计概览", icon: "View" },
            },
            {
              path: "classification",
              name: "StatisticsClassification",
              component: () =>
                import("../views/statistics/classification/index.vue"),
              meta: { title: "统计分类", icon: "Grid" },
            },
            {
              path: "display",
              name: "StatisticsDisplay",
              component: () => import("../views/statistics/display/index.vue"),
              meta: { title: "统计展示", icon: "DataBoard" },
            },
          ],
        },
        // 其他功能路由
        {
          path: "data-analysis",
          name: "DataAnalysis",
          component: () => import("../views/data-analysis/index.vue"),
          meta: { title: "数据分析", icon: "DataLine" },
        },
        {
          path: "gis-map",
          name: "GisMap",
          component: () => import("../views/gis-map/index.vue"),
          meta: { title: "GIS地图", icon: "MapLocation" },
        },
        {
          path: "profile",
          name: "Profile",
          component: () => import("../views/profile/index.vue"),
          meta: { title: "个人中心", icon: "User" },
        },
      ],
    },
    {
      path: "/login",
      name: "Login",
      component: () => import("../views/login/index.vue"),
    },
  ],
});

// 不需要权限校验的路由名称（基础页面）
const PUBLIC_ROUTES = ["Home", "Login", "Layout", "Profile"];

// 添加路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem("token");

  // 如果访问的是登录页面，且已登录，则重定向到首页
  if (to.path === "/login" && token) {
    next("/");
    return;
  }

  // 如果访问的不是登录页面，且未登录，则重定向到登录页面
  if (to.path !== "/login" && !token) {
    next("/login");
    return;
  }

  // 已登录用户：检查路由权限
  if (token && to.name && typeof to.name === "string") {
    const routeName = to.name as string;

    // 公开路由直接放行
    if (PUBLIC_ROUTES.includes(routeName)) {
      next();
      return;
    }

    // 从 localStorage 读取用户权限列表
    const savedPermissions = localStorage.getItem("user_permissions");
    if (savedPermissions) {
      try {
        const permissions: string[] = JSON.parse(savedPermissions);
        // admin:all 拥有所有权限
        if (permissions.includes("admin:all") || permissions.includes(routeName)) {
          next();
          return;
        }
      } catch (e) {
        console.error("解析权限数据失败:", e);
      }
    }

    // 无权限，重定向到首页
    console.warn(`用户无权访问路由: ${routeName}`);
    next("/");
    return;
  }

  next();
});

export default router;
