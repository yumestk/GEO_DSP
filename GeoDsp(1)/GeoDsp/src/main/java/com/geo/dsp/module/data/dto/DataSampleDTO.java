package com.geo.dsp.module.data.dto;

import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class DataSampleDTO {
    private Long pointId;
    private Double sampleValue;
    private String sampleUnit;
    private OffsetDateTime sampleTime;
    private Long deviceId;
    private Double calibrationValue;
    private Integer status;
    private String note;
    private Long operateBy;
}
