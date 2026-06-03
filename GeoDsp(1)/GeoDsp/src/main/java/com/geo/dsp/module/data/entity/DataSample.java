package com.geo.dsp.module.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataSample {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 唯一标识
     */
    private String uuid;

    /**
     * 测点ID
     */
    private Long pointId;

    /**
     * 采样值
     */
    private Double sampleValue;

    /**
     * 采样单位
     */
    private String sampleUnit;

    /**
     * 采样时间
     */
    private OffsetDateTime sampleTime;

    /**
     * 设备ID
     */
    private Long deviceId;

    /**
     * 校准值
     */
    private Double calibrationValue;

    /**
     * 状态（0-未校验，1-已校验，2-异常）
     */
    private Integer status;

    /**
     * 创建人ID
     */
    private Long createBy;

    /**
     * 创建时间
     */
    private OffsetDateTime createTime;

    /**
     * 更新人ID
     */
    private Long updateBy;

    /**
     * 更新时间
     */
    private OffsetDateTime updateTime;

    /**
     * 备注
     */
    private String note;

    /**
     * 是否删除（false-未删，true-已删）
     */
    private Boolean isDel;
}
