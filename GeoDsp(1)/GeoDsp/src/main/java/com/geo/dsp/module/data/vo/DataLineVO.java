package com.geo.dsp.module.data.vo;

import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class DataLineVO {
    private Long id;
    private String uuid;
    private Boolean use;
    private Double name;
    private Long engineeringId;
    private Integer type;
    private String listDataPoint;
    private OffsetDateTime planStartTime;
    private OffsetDateTime planEndTime;
    private Integer status;
    private String note;
}
