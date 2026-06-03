import { createApp } from "vue";
import "./style.css";
import App from "./App.vue";
import router from "./router";
import pinia from "./stores";
import ElementPlus from "element-plus";
import "element-plus/dist/index.css";
import axios from "axios";
import zhCn from "element-plus/es/locale/lang/zh-cn";
import { usePermissionStore } from "./stores/modules/permission";
import { addDynamicRoutes } from "./router/dynamic";
import { permissionDirective } from "./utils/permissionDirective";


// 添加环境变量调试信息
console.log("应用初始化 - 环境变量:", {
  VITE_API_BASE_URL: import.meta.env.VITE_API_BASE_URL,
  MODE: import.meta.env.MODE,
  DEV: import.meta.env.DEV,
  PROD: import.meta.env.PROD,
});

// // 测试axios直接调用
// const testAxios = async () => {
//   try {
//     console.log("开始测试axios直接调用...");

//     // 创建一个临时的axios实例
//     const axiosInstance = axios.create({
//       baseURL: import.meta.env.VITE_API_BASE_URL || "http://localhost:9966/geo",
//       timeout: 10000,
//     });

//     console.log("临时axios实例配置:", {
//       baseURL: axiosInstance.defaults.baseURL,
//       timeout: axiosInstance.defaults.timeout,
//     });

//     // 尝试一个简单的get请求
//     const response = await axiosInstance.get("/api/user/list");
//     console.log("axios测试请求成功:", response.data);
//   } catch (error) {
//     console.error("axios测试请求失败:", error);
//   }
// };

// testAxios();

const app = createApp(App);

app.use(router);
app.use(pinia);
app.use(ElementPlus, {
  locale: zhCn,
});
app.directive("permission", permissionDirective);

// 恢复动态路由（页面刷新时使用）
const permissionStore = usePermissionStore();
permissionStore.restorePermissions();

if (permissionStore.menus.length > 0) {
  addDynamicRoutes(router, permissionStore.menus);
  console.log('刷新页面后，动态路由已恢复，数量:', permissionStore.menus.length);
}

app.mount("#app");
