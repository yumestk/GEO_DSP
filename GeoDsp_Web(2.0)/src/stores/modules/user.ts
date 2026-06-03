// src/stores/modules/user.ts
import { defineStore } from "pinia";
import { ref } from "vue";
import api from "../../api";
import { usePermissionStore } from "./permission";
import router from "@/router";
import { addDynamicRoutes, clearDynamicRoutes } from "@/router/dynamic";

// 定义用户信息类型
interface UserInfo {
  id: number;
  username: string;
  nickname: string;
  email: string;
  phone: string;
  avatar: string;
  roles: string[];
  permissions: string[];
  status?: string;
  createTime?: string;
}

interface LoginResponse {
  token: string;
  userInfo: UserInfo;
}

interface LoginResult {
  success: boolean;
  data?: LoginResponse;
  error?: {
    message: string;
  };
}

interface GetUserInfoResult {
  success: boolean;
  data?: UserInfo;
  error?: {
    message: string;
  };
}

interface LogoutResult {
  success: boolean;
  error?: {
    message: string;
  };
}

// 定义用户Store
export const useUserStore = defineStore("user", () => {
  // 用户状态
  const userState = ref({
    userInfo: ref<UserInfo | null>(null),
    token: ref<string | null>(null),
    isAuthenticated: ref<boolean>(false),
    permissions: ref<string[]>([]),
    roles: ref<string[]>([]),
    features: ref<string[]>([]),
  });

  // ==================== 登录方法（真实API版本） ====================
  const login = async (
    username: string,
    password: string,
  ): Promise<LoginResult> => {
    try {
      // 调用后端登录接口
      const response = await api.user.login({ account: username, password });
      
      if (response.code === 200) {
        // 解构后端返回的数据
        const { token, userInfo, menus, permissions } = response.data;
        
        // 1. 存储 token 到 localStorage 和 state
        localStorage.setItem("token", token);
        userState.value.token = token;
        userState.value.userInfo = userInfo;
        userState.value.isAuthenticated = true;
        
        // 2. 存储用户权限和角色
        if (userInfo) {
          userState.value.permissions = userInfo.permissions || [];
          userState.value.roles = userInfo.roles || [];
        }
        
        // 3. 存储菜单和按钮权限到 permissionStore（用于侧边栏渲染和按钮权限控制）
        const permissionStore = usePermissionStore();
        permissionStore.setPermissions(menus, userInfo.permissions);
        
        // 4. 动态添加路由（根据后端返回的菜单）
        clearDynamicRoutes(router);
        addDynamicRoutes(router, menus);
        
        return { success: true, data: { token, userInfo } };
      } else {
        return {
          success: false,
          error: { message: response.msg || "登录失败" },
        };
      }
    } catch (error: any) {
      console.error("登录错误:", error);
      return {
        success: false,
        error: { message: error.message || "登录失败" },
      };
    }
  };

  // ==================== 获取用户信息 ====================
  const getUserInfo = async (): Promise<GetUserInfoResult> => {
    try {
      const response = await api.user.getUserInfo();
      if (response.code === 200) {
        userState.value.userInfo = response.data;
        userState.value.isAuthenticated = true;

        if (response.data) {
          userState.value.permissions = response.data.permissions || [];
          userState.value.roles = response.data.roles || [];
        }

        return { success: true, data: response.data };
      } else {
        return {
          success: false,
          error: { message: response.msg || "获取用户信息失败" },
        };
      }
    } catch (error: any) {
      console.error("获取用户信息错误:", error);
      logout();
      return {
        success: false,
        error: { message: error.message || "获取用户信息失败" },
      };
    }
  };

  // ==================== 退出登录 ====================
  const logout = async (): Promise<LogoutResult> => {
    try {
      const response = await api.user.logout();
      return { success: response.code === 200 };
    } catch (error: any) {
      console.error("登出API调用错误:", error);
      return {
        success: false,
        error: { message: error.message || "登出失败" },
      };
    } finally {
      // 清除本地状态
      userState.value.userInfo = null;
      userState.value.token = null;
      userState.value.isAuthenticated = false;
      userState.value.permissions = [];
      userState.value.roles = [];

      // 清除 localStorage 中的 token
      localStorage.removeItem("token");
      
      // 清除动态路由
      clearDynamicRoutes(router);
      
      // 清除权限 store 中的数据
      const permissionStore = usePermissionStore();
      permissionStore.clearPermissions();
    }
  };

  // ==================== 检查用户是否已认证 ====================
  const isAuthenticated = () => {
    const token = localStorage.getItem("token");
    if (token) {
      userState.value.token = token;
      userState.value.isAuthenticated = true;
      return true;
    }
    return false;
  };

  // ==================== 更新用户信息 ====================
  const updateUserInfo = async (newInfo: Partial<UserInfo>) => {
    if (userState.value.userInfo) {
      userState.value.userInfo = { ...userState.value.userInfo, ...newInfo };
    }
  };

  // ==================== 检查权限 ====================
  const hasPermission = (permission: string) => {
    return userState.value.permissions.includes(permission);
  };

  const hasRole = (role: string) => {
    return userState.value.roles.includes(role);
  };

  return {
    userState,
    login,
    getUserInfo,
    logout,
    isAuthenticated,
    updateUserInfo,
    hasPermission,
    hasRole,
  };
});