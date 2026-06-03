package com.geo.dsp.module.privilege.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

/**
 * 新增角色表单DTO
 */
@Data
public class RoleAddDTO {
    @NotBlank(message = "角色名称不能为空")
    private String roleName;

    @NotBlank(message = "角色编码不能为空")
    private String roleCode;

    private String roleDesc;

    private Short status = 1; // 默认启用
}