package com.geo.dsp.module.equipment.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UseRecord {
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
     * 项目ID
     */
    private Long projectId;

    /**
     * 工程ID
     */
    private Long engineeringId;

    /**
     * 任务ID
     */
    private Long taskId;

    /**
     * 使用人ID
     */
    private Long userId;

    /**
     * 开始使用时间
     */
    private OffsetDateTime useStartTime;

    /**
     * 结束使用时间
     */
    private OffsetDateTime useEndTime;

    /**
     * 备注
     */
    private String note;

    /**
     * 是否删除（false-未删，true-已删）
     */
    private Boolean isDel;
}
