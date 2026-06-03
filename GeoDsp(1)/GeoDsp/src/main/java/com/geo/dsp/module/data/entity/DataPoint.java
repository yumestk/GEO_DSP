package com.geo.dsp.module.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataPoint {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 唯一标识
     */
    private String uuid;

    /**
     * 作业线ID
     */
    private Long lineId;

    /**
     * 测点编号
     */
    private Double pointNo;

    /**
     * X坐标
     */
    private Double xCoord;

    /**
     * Y坐标
     */
    private Double yCoord;

    /**
     * Z坐标
     */
    private Double zCoord;

    /**
     * 采集时间
     */
    private OffsetDateTime collectTime;

    /**
     * 采集人ID
     */
    private Long collectorId;

    /**
     * 设备ID
     */
    private Long deviceId;

    /**
     * 状态（0-未采集，1-已采集，2-异常）
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
