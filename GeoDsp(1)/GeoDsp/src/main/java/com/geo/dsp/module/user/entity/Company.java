package com.geo.dsp.module.user.entity;

import lombok.Data;
import java.time.OffsetDateTime;

/**
 * 企业实体类
 * 对应数据库表：companies（严格按你的库表设计）
 */
@Data
public class Company {

    // 自增主键（内部使用）
    private Long id;

    // 对外UUID（数据库自动生成：gen_random_uuid()）
    private String uuid;

    // 企业全称（唯一）
    private String companyName;

    // 企业简称
    private String companyAbbr;

    // 状态 0=禁用 1=正常
    private Short status;

    // 联系人姓名
    private String contactName;

    // 联系人电话
    private String contactPhone;

    // 联系人邮箱
    private String contactEmail;

    // 企业地址
    private String address;

    // 统一社会信用代码（唯一）
    private String licenseNum;

    // 创建人ID
    private Long createBy;

    // 创建时间（数据库自动 now()）
    private OffsetDateTime createTime;

    // 修改人ID
    private Long updateBy;

    // 修改时间
    private OffsetDateTime updateTime;

    // 备注
    private String note;

    // 删除标记：false=未删除
    private Boolean isDel;
}