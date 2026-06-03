// src/stores/modules/permission.ts
import { defineStore } from "pinia";
import { ref } from "vue";

/**
 * 权限管理 Store
 * 职责：存储菜单树和按钮权限列表，供侧边栏和权限控制使用
 * 
 * 数据来源：登录时从后端（或 Mock）获取，通过 setPermissions 存入
 * 
 * 字段说明：
 * - menus: 菜单树，用于渲染侧边栏
 * - permissions: 按钮权限编码列表，用于 v-permission 指令判断
 */
export const usePermissionStore = defineStore("permission", () => {
  // ==================== 状态定义 ====================
  
  /** 菜单树数据，用于侧边栏渲染 */
  const menus = ref<any[]>([]);
  
  /** 按钮权限编码列表，如 ["system:user:add", "system:user:edit"] */
  const permissions = ref<string[]>([]);

  // ==================== 方法定义 ====================
  
  /**
 * 设置权限数据（登录成功后调用）
 * @param menuData - 菜单树数据，结构为：
 *   [{
 *     permissionCode: "system",
 *     permissionName: "系统管理",
 *     permissionType: "M",
 *     parentPermission: null,
 *     componentPath: "",
 *     children: [...]
 *   }]
 * @param permissionList - 按钮权限编码列表，如 ["system:user:add", "system:user:edit"]
 */
  const setPermissions = (menuData: any[], permissionList: string[]) => {
    menus.value = menuData;
    permissions.value = permissionList;
    
    // 存一份到 localStorage，防止页面刷新后数据丢失
    localStorage.setItem("user_menus", JSON.stringify(menuData));
    localStorage.setItem("user_permissions", JSON.stringify(permissionList));
  };

  /**
   * 检查是否有某个按钮权限
   * @param code - 权限编码，如 "system:user:add"
   * @returns 是否有权限
   * 
   * 使用方式：在组件中调用
   * import { usePermissionStore } from "@/stores/modules/permission";
   * const permissionStore = usePermissionStore();
   * permissionStore.hasPermission("system:user:add")
   */
  const hasPermission = (code: string): boolean => {
    if (!code) return true;  // 没有传入权限编码，默认显示
    if (permissions.value.includes("admin:all")) return true; // 超级管理员
    return permissions.value.includes(code);
  };

  /**
   * 清除权限数据（退出登录时调用）
   * 
   * 使用方式：在 userStore.logout 中调用
   * const permissionStore = usePermissionStore();
   * permissionStore.clearPermissions();
   */
  const clearPermissions = () => {
    menus.value = [];
    permissions.value = [];
    localStorage.removeItem("user_menus");
    localStorage.removeItem("user_permissions");
  };

  /**
   * 从 localStorage 恢复权限数据（页面刷新时调用）
   * 
   * 使用方式：在 main.ts 中调用
   * const permissionStore = usePermissionStore();
   * permissionStore.restorePermissions();
   */
  const restorePermissions = () => {
    const savedMenus = localStorage.getItem("user_menus");
    const savedPermissions = localStorage.getItem("user_permissions");
    if (savedMenus) menus.value = JSON.parse(savedMenus);
    if (savedPermissions) permissions.value = JSON.parse(savedPermissions);
  };

  // ==================== 返回给外部使用的方法 ====================
  return {
    menus,           // 菜单树数据
    permissions,     // 按钮权限列表
    setPermissions,  // 设置权限（登录时调用）
    hasPermission,   // 检查按钮权限
    clearPermissions,// 清除权限（退出登录时调用）
    restorePermissions, // 恢复权限（页面刷新时调用）
  };
});