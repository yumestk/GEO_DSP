package com.geo.dsp.module.data.dto;

import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class TaskAuditDTO {
    private Long taskId;
    private Long auditorId;
    private OffsetDateTime auditTime;
    private Integer auditResult;
    private String auditOpinion;
    private Long operateBy;
}
