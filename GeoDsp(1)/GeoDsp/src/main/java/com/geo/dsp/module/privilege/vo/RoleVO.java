package com.geo.dsp.module.privilege.vo;

import lombok.Data;
import java.time.OffsetDateTime;

/**
 * 角色列表VO，和前端表格字段一一对应
 */
@Data
public class RoleVO {
    private Long id;
    private String uuid;          // 前端编辑/删除时传递
    private String roleName;      // 角色名称
    private String roleCode;      // 角色编码
    private String roleDesc;      // 角色描述
    private Short status;         // 状态
    private OffsetDateTime createTime; // 创建时间
}