package com.geo.dsp.module.data.dto;

import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class DataPointDTO {
    private Long lineId;
    private Double pointNo;
    private Double xCoord;
    private Double yCoord;
    private Double zCoord;
    private OffsetDateTime collectTime;
    private Long collectorId;
    private Long deviceId;
    private Integer status;
    private String note;
    private Long operateBy;
}
