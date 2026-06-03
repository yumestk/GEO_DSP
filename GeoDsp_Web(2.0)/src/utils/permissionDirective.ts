// src/utils/permissionDirective.ts
import type { Directive, DirectiveBinding } from "vue";
import { usePermissionStore } from "@/stores/modules/permission";

/**
 * 权限指令 v-permission
 * 使用方式：
 *   <el-button v-permission="'system:user:add'">新增用户</el-button>
 *   <el-button v-permission="['system:user:add', 'system:user:create']">新增用户</el-button>
 * 
 * 原理：
 *   1. 获取当前用户拥有的权限列表（permissionStore.permissions）
 *   2. 检查指令传入的权限码是否在列表中
 *   3. 如果没有权限，则移除该 DOM 元素
 */
export const permissionDirective: Directive = {
  mounted(el: HTMLElement, binding: DirectiveBinding) {
    // 获取指令传入的值（权限码）
    const { value } = binding;

    // 如果没有传入权限码，不做处理（默认显示）
    if (!value) return;

    // 获取当前用户的权限列表
    const permissionStore = usePermissionStore();
    const userPermissions = permissionStore.permissions;

    // 如果没有获取到权限列表，说明还没登录或权限数据未加载，不显示按钮
    if (!userPermissions || userPermissions.length === 0) {
      el.parentNode?.removeChild(el);
      return;
    }

    // 判断是否有权限
    let hasAuth = false;

    // admin:all 是超级管理员通配符，拥有所有权限
    if (userPermissions.includes("admin:all")) {
      return;
    }

    if (typeof value === "string") {
      // 单权限：v-permission="'system:user:add'"
      hasAuth = userPermissions.includes(value);
    } else if (Array.isArray(value)) {
      // 多权限（满足其一即可）：v-permission="['system:user:add', 'system:user:create']"
      hasAuth = value.some((item: string) => userPermissions.includes(item));
    } else {
      // 无效的传参，移除元素
      el.parentNode?.removeChild(el);
      return;
    }

    // 如果没有权限，移除元素
    if (!hasAuth) {
      el.parentNode?.removeChild(el);
    }
  },
};