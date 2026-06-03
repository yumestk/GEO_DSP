package com.geo.dsp.module.privilege.dto;

import lombok.Data;

/**
 * 角色 DTO（新增/编辑入参）
 */
@Data
public class RoleDTO {

    private Long companyId;   // 所属企业 ID
    private String roleName;  // 角色名称
    private String roleDesc;  // 角色描述

    /**
     * 操作人 ID（创建/修改人）
     */
    private Long operateBy;
}
