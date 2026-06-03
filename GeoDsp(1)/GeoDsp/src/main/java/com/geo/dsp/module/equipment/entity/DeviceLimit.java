package com.geo.dsp.module.equipment.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceLimit {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 唯一标识
     */
    private String uuid;

    /**
     * 设备ID
     */
    private Long deviceId;

    /**
     * 开始使用时间
     */
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;
    private Long createBy;
    private OffsetDateTime createTime;
    private Long updateBy;
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
