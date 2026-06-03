package com.geo.dsp.module.equipment.dto;

import lombok.Data;

@Data
public class DeviceDTO {
    private Long companyId;
    private String deviceNum;
    private Long deviceTypeId;
    private Integer antennaNum;
    private Integer status;
    private String note;
    private Long operateBy;
}
