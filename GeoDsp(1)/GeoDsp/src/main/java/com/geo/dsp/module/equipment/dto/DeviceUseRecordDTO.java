package com.geo.dsp.module.equipment.dto;

import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class DeviceUseRecordDTO {
    private Long deviceId;
    private Long projectId;
    private Long engineeringId;
    private Long taskId;
    private Long userId;
    private OffsetDateTime useStartTime;
    private OffsetDateTime useEndTime;
    private String note;
    private Long operateBy;
}
