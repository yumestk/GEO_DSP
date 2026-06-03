package com.geo.dsp.module.privilege.vo;

import lombok.Data;
import java.time.OffsetDateTime;

/**
 * 权限列表VO（匹配前端表格）
 */
@Data
public class PermissionVO {
    private Long id;
    private String uuid;
    private String permissionName;    // 权限名称
    private String permissionCode;    // 权限编码
    private String permissionDesc;    // 描述
    private String permissionType;    // 类型
    private String parentPermission;  // 父级权限
    private String componentPath;      // 前端组件路径
    private String status;             // 状态
    private OffsetDateTime createTime; // 创建时间
}
