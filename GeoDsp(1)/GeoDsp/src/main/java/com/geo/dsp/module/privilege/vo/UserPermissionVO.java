package com.geo.dsp.module.privilege.vo;

import lombok.Data;

/**
 * 用户权限详情VO（登录返回，前端构建菜单和按钮控制用）
 * 数据格式：扁平列表 + parentPermission，前端构建树
 */
@Data
public class UserPermissionVO {
    private String uuid;              // 权限UUID
    private String permissionCode;    // 权限编码（按钮用 v-if 判断）
    private String permissionName;    // 权限名称（菜单显示用）
    private String permissionType;    // 类型：menu/button
    private String componentPath;      // 前端组件路径（菜单用，按钮为空）
    private String parentPermission;  // 父权限UUID（前端构建树用）
}
