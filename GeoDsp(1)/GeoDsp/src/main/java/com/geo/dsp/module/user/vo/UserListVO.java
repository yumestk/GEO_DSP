package com.geo.dsp.module.user.vo;

import lombok.Data;
import java.time.OffsetDateTime;

/**
 * 用户列表展示VO（适配前端表格）
 */
@Data
public class UserListVO {
    private Long id;
    private String userName;   // 用户名
    private String account;    // 账号
    private String email;      // 邮箱
    private String phoneNum;   // 手机号
    private String roleName;   // 角色名称（关联查询）
    private OffsetDateTime createTime; // 创建时间
    private Short status;// 状态 0禁用 1正常
    private String uuid; //uuid
    private Short sex;   // 性别 0=女 1=男
}
