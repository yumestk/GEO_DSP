package com.geo.dsp.module.equipment.vo;

import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class DeviceVO {
    private Long id;
    private String uuid;
    private Long companyId;
    private String deviceNum;
    private Long deviceTypeId;
    private String deviceTypeName;
    private Integer antennaNum;
    private Integer status;
    private String note;
    private OffsetDateTime createTime;
}
