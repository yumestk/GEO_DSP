package com.geo.dsp.module.data.dto;

import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class CollectTaskDTO {
    private Long projectId;
    private String taskName;
    private String taskDesc;
    private Long executorId;
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;
    private Long methodId;
    private Long deviceId;
    private Integer status;
    private String note;
    private Long operateBy;
}
