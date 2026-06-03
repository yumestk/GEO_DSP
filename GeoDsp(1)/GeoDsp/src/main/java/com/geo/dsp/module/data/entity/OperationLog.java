package com.geo.dsp.module.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperationLog {
    private Long id;
    private String uuid;
    private Long projectId;
    private String projectName;
    private String taskName;
    private Long engineeringId;
    private Long taskId;
    private String logDate;
    private String content;
    private Long creatorId;
    private OffsetDateTime createTime;
    private Long updateBy;
    private OffsetDateTime updateTime;
    private String note;
    private Boolean isDel;
}
