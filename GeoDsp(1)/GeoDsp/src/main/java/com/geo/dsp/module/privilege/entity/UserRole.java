package com.geo.dsp.module.privilege.entity;

import lombok.Data;
import java.util.UUID;

/**
 * 用户-角色关联表
 */
@Data
public class UserRole {
    private Long id;
    private String  userUuid;    // 关联用户UUID
    private String roleCode;  // 关联角色代码
    private String roleName;  // 角色名称
    private String roleDesc;  // 角色描述
}