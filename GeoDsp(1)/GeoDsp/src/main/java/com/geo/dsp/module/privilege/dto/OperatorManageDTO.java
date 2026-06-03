package com.geo.dsp.module.privilege.dto;

import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class OperatorManageDTO {
    private String uuid;
    private Long companyId;
    private Long userId;
    private String operatorName;
    private String phoneNum;
    private Integer status;
    private OffsetDateTime startTime;
    private OffsetDateTime stopTime;
    private String note;
    private Long operateBy;
}
