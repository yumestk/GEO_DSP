package com.geo.dsp.module.user.vo;

import lombok.Data;
import java.time.OffsetDateTime;

/**
 * 企业列表VO（匹配前端表格）
 */
@Data
public class CompanyVO {
    private Long id;
    private String uuid;
    private String companyName;    // 企业全称
    private String companyAbbr;    // 简称
    private String contactName;    // 联系人
    private String contactPhone;   // 电话
    private String licenseNum;      // 信用代码
    private Short status;           // 状态
    private OffsetDateTime createTime; // 创建时间
}