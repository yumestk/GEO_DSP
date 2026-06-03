package com.geo.dsp.module.privilege.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

/**
 * 修改角色表单DTO
 */
@Data
public class RoleUpdateDTO {
    @NotBlank(message = "角色UUID不能为空")
    private String uuid; // 前端传递，用于定位角色

    @NotBlank(message = "角色名称不能为空")
    private String roleName;

    @NotBlank(message = "角色编码不能为空")
    private String roleCode;

    private String roleDesc;

    private Short status;
}