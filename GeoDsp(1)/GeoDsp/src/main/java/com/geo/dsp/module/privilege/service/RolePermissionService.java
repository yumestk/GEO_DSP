package com.geo.dsp.module.privilege.service;

import com.geo.dsp.module.privilege.entity.Permission;

import java.util.List;

/**
 * 角色权限关联Service（对应前端角色权限配置界面）
 */
public interface RolePermissionService {
    /**
     * 为角色分配权限（前端“保存权限配置”功能）
     * @param roleUuid 角色UUID
     * @param permissionUuids 权限UUID列表（前端勾选的权限）
     * @param companyId 企业ID（多租户隔离）
     * @param createBy 创建人ID
     * @return 是否成功
     */
    boolean assignPermissionsToRole(String roleUuid, List<String> permissionUuids, Long companyId, Long createBy);
}
