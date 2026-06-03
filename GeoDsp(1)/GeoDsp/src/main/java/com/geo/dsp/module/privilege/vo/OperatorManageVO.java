package com.geo.dsp.module.privilege.vo;

import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class OperatorManageVO {
    private Long id;
    private String uuid;
    private Long companyId;
    private Long userId;
    private String operatorName;
    private String phoneNum;
    private Integer status;
    private OffsetDateTime startTime;
    private OffsetDateTime stopTime;
    private Long createBy;
    private OffsetDateTime createTime;
    private Long updateBy;
    private OffsetDateTime updateTime;
    private Boolean isDel;
    private String note;
}
