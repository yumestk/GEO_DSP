package com.geo.dsp.module.privilege.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

/**
 * 修改权限DTO
 * 字段已按要求修改：parent_uuid → parentPermission
 */
@Data
public class PermissionUpdateDTO {

    @NotBlank(message = "权限UUID不能为空")
    private String uuid;

    @NotBlank(message = "权限名称不能为空")
    private String permissionName;

    @NotBlank(message = "权限编码不能为空")
    private String permissionCode;

    private String permissionDesc;

    private String permissionType;

    private String parentPermission;

    // 前端组件路径（菜单权限使用，按钮权限为空）
    private String componentPath;

    private String status;
}
