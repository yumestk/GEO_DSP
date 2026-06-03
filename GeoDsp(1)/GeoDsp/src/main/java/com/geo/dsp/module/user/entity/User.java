package com.geo.dsp.module.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * 用户实体类
 * 对应数据库：users表
 * 系统用户主表，存储平台/企业/操作员信息
 */
@Data
@TableName("users")
public class User {
    // 主键ID
    private Long id;

    // 用户UUID（对外唯一标识）
    private String uuid;

    // 企业ID
    private Long companyId;

    // 用户类型 1平台 2企业 3操作员
    private Short userType;

    // 用户名
    private String userName;

    // 登录账号
    private String account;

    // 密码（加密）
    private String password;

    // 手机号
    private String phoneNum;

    // 邮箱
    private String email;

    // 状态 0禁用 1正常
    private Short status;

    // 头像ID
    private Long imageId;

    // 创建人ID
    private Long createBy;

    // 创建时间
    private OffsetDateTime createTime;

    // 修改人ID
    private Long updateBy;

    // 修改时间
    private OffsetDateTime updateTime;

    // 逻辑删除 false未删除 true已删除
    private Boolean isDel;

    // 备注
    private String note;

    // 性别 0=女，1=男
    private Short sex;
}