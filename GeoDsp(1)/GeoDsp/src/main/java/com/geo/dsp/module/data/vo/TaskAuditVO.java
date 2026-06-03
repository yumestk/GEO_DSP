package com.geo.dsp.module.data.vo;

import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class TaskAuditVO {
    private Long id;
    private String uuid;
    private Long taskId;
    private Long auditorId;
    private OffsetDateTime auditTime;
    private Integer auditResult;
    private String auditOpinion;
    private OffsetDateTime createTime;
}
