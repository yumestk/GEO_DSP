package com.geo.dsp.module.data.vo;

import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class ProjectVO {
    private Long id;
    private String uuid;
    private Long companyId;
    private String projectName;
    private String projectCode;
    private String projectAddress;
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;
    private Long managerId;
    private Integer status;
    private String note;
    private OffsetDateTime createTime;
}
