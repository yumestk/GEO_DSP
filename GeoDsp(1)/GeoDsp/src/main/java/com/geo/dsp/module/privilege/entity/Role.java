package com.geo.dsp.module.privilege.entity;

import lombok.Data;
import java.time.OffsetDateTime;

/**
 * 角色实体类
 * 对应数据库 roles 表
 */
@Data
public class Role {
    // 数据库自增主键（内部使用）
    private Long id;
    // 自动生成的UUID（对外唯一标识，前后端交互用）
    private String uuid;
    // 角色名称（前端表单必填）
    private String roleName;
    // 角色编码（前端表单必填，唯一）
    private String roleCode;
    // 角色描述（前端表单可选）
    private String roleDesc;
    // 状态 1=启用 0=禁用（前端开关控制）
    private Short status;
    // 创建人ID（从Token获取）
    private Long createBy;
    // 创建时间（数据库自动）
    private OffsetDateTime createTime;
    // 修改人ID（从Token获取）
    private Long updateBy;
    // 修改时间（数据库自动）
    private OffsetDateTime updateTime;
    // 逻辑删除标记
    private Boolean isDel;
}
