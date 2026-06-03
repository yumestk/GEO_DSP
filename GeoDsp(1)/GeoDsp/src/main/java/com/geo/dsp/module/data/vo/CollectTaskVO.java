package com.geo.dsp.module.data.vo;

import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class CollectTaskVO {
    private Long id;
    private String uuid;
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
    private OffsetDateTime createTime;
}
