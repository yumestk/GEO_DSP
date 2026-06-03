package com.geo.dsp.module.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataHistory {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 唯一标识
     */
    private String uuid;

    /**
     * 数据ID（关联测点/采样等表ID）
     */
    private Long dataId;

    /**
     * 数据类型（point-测点，sample-采样，line-作业线）
     */
    private String dataType;

    /**
     * 修改前值（JSON格式）
     */
    private String oldValue;

    /**
     * 修改后值（JSON格式）
     */
    private String newValue;

    /**
     * 操作人ID
     */
    private Long operatorId;

    /**
     * 操作时间
     */
    private OffsetDateTime operateTime;

    /**
     * 操作描述
     */
    private String operateDesc;

    /**
     * 是否删除（false-未删，true-已删）
     */
    private Boolean isDel;
}
