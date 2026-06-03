package com.geo.dsp.module.data.vo;

import lombok.Data;

@Data
public class OperateMethodVO {
    private Long id;
    private String uuid;
    private String methodName;
    private String methodCode;
    private String methodDesc;
    private String note;
}
