package com.geo.dsp.module.privilege.entity;

import lombok.Data;
import java.time.OffsetDateTime;

/**
 * 权限实体
 * 对应数据库表：permissions
 */
@Data
public class Permission {

    // 自增主键
    private Long id;

    // 对外UUID（数据库自动生成）
    private String uuid;

    // 权限名称
    private String permissionName;

    // 权限编码 如 device:add
    private String permissionCode;

    // 权限描述
    private String permissionDesc;

    // 权限类型
    private String permissionType;

    // 父权限
    private String parentPermission;

    // 前端组件路径（菜单权限使用，按钮权限为空）
    private String componentPath;

    // 状态
    private String status;

    // 创建人ID
    private Long createBy;

    // 创建时间
    private OffsetDateTime createTime;

    // 修改人ID
    private Long updateBy;

    // 修改时间
    private OffsetDateTime updateTime;

    // 删除标记
    private Boolean isDel;
}
