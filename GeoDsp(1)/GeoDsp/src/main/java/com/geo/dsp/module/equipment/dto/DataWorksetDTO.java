package com.geo.dsp.module.equipment.dto;

import lombok.Data;

@Data
public class DataWorksetDTO {
    private String uuid;
    private String workConfig;
    private Double sendcoilLen;
    private Double sendcoilWidth;
    private Integer sendcoilTurns;
    private Double recvcoilSize;
    private Double recvcoilGain;
    private String note;
    private Long operateBy;
}
