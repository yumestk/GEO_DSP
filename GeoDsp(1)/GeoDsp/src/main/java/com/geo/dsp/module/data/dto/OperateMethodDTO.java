package com.geo.dsp.module.data.dto;

import lombok.Data;

@Data
public class OperateMethodDTO {
    private String methodName;
    private String methodCode;
    private String methodDesc;
    private String note;
    private Long operateBy;
}
