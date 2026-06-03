// src/router/dynamic.ts
// 这个文件的作用：根据后端返回的菜单数据，动态添加路由

import type { Router } from "vue-router";

/**
 * 菜单项的类型定义
 * 这个结构必须和 user.ts 中 getAdminMenus 返回的数据结构保持一致
 */
interface MenuItem {
  permissionCode: string;      // 权限编码，例如 "system:user"
  permissionName: string;      // 菜单显示名称，例如 "用户管理"
  componentPath?: string;      // 组件文件路径，例如 "views/system/users/index.vue"
  children?: MenuItem[];       // 子菜单
}

/**
 * 存储动态添加的路由名称列表
 * 用于退出登录时批量清除路由
 */
const dynamicRouteNames: string[] = [];

/**
 * 核心函数：根据菜单数据动态添加路由
 * @param router - Vue Router 实例
 * @param menus - 菜单树数据（从 permissionStore.menus 获取）
 * 
 * 工作原理：
 * 1. 遍历菜单树的每一项
 * 2. 用 permissionCode 生成路由路径（例如 "system:user" -> "/system/user"）
 * 3. 调用 router.addRoute() 把路由添加进去
 * 4. 如果有子菜单，递归处理
 */
export const addDynamicRoutes = (router: Router, menus: MenuItem[]) => {
  /**
   * 递归函数：遍历菜单项并添加路由
   * @param items - 当前层级的菜单项数组
   */
  const addRoutes = (items: MenuItem[]) => {
    items.forEach(item => {
      // ========== 第一步：生成路由路径 ==========
      // 把 "system:user" 中的冒号换成斜杠，得到 "system/user"
      // 然后加上开头的斜杠，得到 "/system/user"
      const fullPath = `/${item.permissionCode.replace(/:/g, '/')}`;

      // ========== 第二步：检查是否需要添加路由 ==========
      // 只有有组件路径的菜单项才需要添加路由
      // 纯目录（没有 componentPath）不需要添加路由
      if (item.componentPath) {
        // 路由名称直接使用 permissionCode，保证唯一性
        const routeName = item.permissionCode;

        // 避免重复添加（防止多次登录时重复添加）
        if (!router.hasRoute(routeName)) {
          try {
            console.log('添加子路由:', routeName, fullPath);

            // 调用 router.addRoute 添加路由
            // 注意：这里的 component 是动态导入，只有访问到这个路由时才会加载对应的 .vue 文件
            router.addRoute("Layout",{
              path: fullPath,                           // 路由路径，如 "/system/user"
              name: routeName,                          // 路由名称，如 "system:user"
              component: () => import(/* @vite-ignore */ `../${item.componentPath}`), // 组件路径
              meta: { title: item.permissionName },     // 页面标题
            });

            // 记录已添加的路由名称，方便退出登录时清除
            dynamicRouteNames.push(routeName);
          } catch (error) {
            console.error(`添加动态路由失败: ${routeName}`, error);
          }
        }
      }

      // ========== 第三步：递归处理子菜单 ==========
      if (item.children && item.children.length) {
        addRoutes(item.children);
      }
    });
  };

  // 开始递归添加路由
  addRoutes(menus);
};

/**
 * 清除所有动态添加的路由
 * 在用户退出登录时调用，防止下一个用户看到上一个用户的路由
 * @param router - Vue Router 实例
 */
export const clearDynamicRoutes = (router: Router) => {
  // 遍历所有已添加的路由名称
  dynamicRouteNames.forEach(routeName => {
    // 如果路由存在，则移除
    if (router.hasRoute(routeName)) {
      router.removeRoute(routeName);
    }
  });
  // 清空记录数组
  dynamicRouteNames.length = 0;
};