package com.geo.dsp.module.equipment.vo;

import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class DeviceUseRecordVO {
    private Long id;
    private String uuid;
    private Long deviceId;
    private Long projectId;
    private Long engineeringId;
    private Long taskId;
    private Long userId;
    private OffsetDateTime useStartTime;
    private OffsetDateTime useEndTime;
    private String note;
}
