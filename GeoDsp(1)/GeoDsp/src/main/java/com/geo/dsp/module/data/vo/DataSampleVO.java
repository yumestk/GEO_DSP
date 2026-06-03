package com.geo.dsp.module.data.vo;

import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class DataSampleVO {
    private Long id;
    private String uuid;
    private Long pointId;
    private Double sampleValue;
    private String sampleUnit;
    private OffsetDateTime sampleTime;
    private Long deviceId;
    private Double calibrationValue;
    private Integer status;
    private String note;
}
