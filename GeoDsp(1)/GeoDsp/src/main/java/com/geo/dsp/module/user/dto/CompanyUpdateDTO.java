package com.geo.dsp.module.user.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

/**
 * 修改企业DTO
 */
@Data
public class CompanyUpdateDTO {

    @NotBlank(message = "企业UUID不能为空")
    private String uuid;

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

    private Short status;
}