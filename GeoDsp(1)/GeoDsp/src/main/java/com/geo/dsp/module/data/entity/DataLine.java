package com.geo.dsp.module.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataLine {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 唯一标识
     */
    private String uuid;

    /**
     * 是否启用（false-否，true-是）
     */
    private Boolean use;

    /**
     * 作业线编号
     */
    private Double name;

    /**
     * 工程ID
     */
    private Long engineeringId;

    /**
     * 类型（1-测线，2-剖面）
     */
    private Integer type;

    /**
     * 测点列表（JSON格式）
     */
    private String listDataPoint;

    /**
     * 计划开始时间
     */
    private OffsetDateTime planStartTime;

    /**
     * 计划结束时间
     */
    private OffsetDateTime planEndTime;

    /**
     * 状态（0-未开始，1-进行中，2-已完成）
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
