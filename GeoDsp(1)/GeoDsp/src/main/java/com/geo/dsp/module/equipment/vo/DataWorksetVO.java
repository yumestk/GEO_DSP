package com.geo.dsp.module.equipment.vo;

import lombok.Data;

@Data
public class DataWorksetVO {
    private Long id;
    private String uuid;
    private String workConfig;
    private Double sendcoilLen;
    private Double sendcoilWidth;
    private Integer sendcoilTurns;
    private Double recvcoilSize;
    private Double recvcoilGain;
    private String note;
}
