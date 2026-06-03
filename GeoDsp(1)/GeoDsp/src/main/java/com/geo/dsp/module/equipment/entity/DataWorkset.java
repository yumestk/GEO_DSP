package com.geo.dsp.module.equipment.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataWorkset {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 唯一标识
     */
    private String uuid;

    /**
     * 工作配置
     */
    private String workConfig;

    /**
     * 发射线圈长度
     */
    private Double sendcoilLen;

    /**
     * 发射线圈宽度
     */
    private Double sendcoilWidth;

    /**
     * 发射线圈匝数
     */
    private Integer sendcoilTurns;

    /**
     * 接收线圈尺寸
     */
    private Double recvcoilSize;

    /**
     * 接收线圈增益
     */
    private Double recvcoilGain;

    /**
     * 备注
     */
    private String note;

    /**
     * 是否删除（false-未删，true-已删）
     */
    private Boolean isDel;
}
