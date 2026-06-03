package com.geo.dsp.module.user.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

/**
 * 新增企业DTO（匹配前端表单）
 */
@Data
public class CompanyAddDTO {

    @NotBlank(message = "企业全称不能为空")
    private String companyName;

    @NotBlank(message = "企业简称不能为空")
    private String companyAbbr;

    @NotBlank(message = "联系人姓名不能为空")
    private String contactName;

    @NotBlank(message = "联系人电话不能为空")
    private String contactPhone;

    private String contactEmail;

    private String address;

    @NotBlank(message = "统一社会信用代码不能为空")
    private String licenseNum;

    private String note;

    // 默认正常
    private Short status = 1;
}
