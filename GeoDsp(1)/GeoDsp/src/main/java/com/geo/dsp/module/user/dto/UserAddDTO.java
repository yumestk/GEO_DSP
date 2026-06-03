package com.geo.dsp.module.user.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

/**
 * 添加用户请求DTO（适配前端表单）
 */
@Data
public class UserAddDTO {

    @NotBlank(message = "用户名不能为空")
    private String userName;

    @NotBlank(message = "账号不能为空")
    private String account;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "邮箱不能为空")
    private String email;

    @NotBlank(message = "手机号不能为空")
    private String phoneNum;

    @NotBlank(message = "角色名称不能为空")
    private String roleName;

    @NotBlank(message = "角色代码不能为空")
    private String roleCode;

    private String roleDesc; // 角色描述（可选）

    private Short sex;
}