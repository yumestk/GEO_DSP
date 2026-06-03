package com.geo.dsp.module.user.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

/**
 * 修改用户请求DTO
 * 完全匹配前端【编辑用户】弹窗表单
 */
@Data
public class UserUpdateDTO {

    /**
     * 用户UUID（必须传，前后端唯一标识）
     */
    @NotBlank(message = "用户UUID不能为空")
    private String uuid;

    /**
     * 用户名（前端可修改）
     */
    @NotBlank(message = "用户名不能为空")
    private String userName;

    /**
     * 登录账号（前端可修改）
     */
    @NotBlank(message = "账号不能为空")
    private String account;

    /**
     * 邮箱
     */
    @NotBlank(message = "邮箱不能为空")
    private String email;

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    private String phoneNum;

    /**
     * 状态 1=启用 0=禁用
     */
    private Short status;

    // ================== 角色信息（前端修改后同步） ==================
    /**
     * 角色名称
     */
    @NotBlank(message = "角色名称不能为空")
    private String roleName;

    /**
     * 角色代码
     */
    @NotBlank(message = "角色代码不能为空")
    private String roleCode;

    /**
     * 角色描述（可选）
     */
    private String roleDesc;

    /**
     * 性别 0=女 1=男（可选）
     */
    private Short sex;
}