package com.geo.dsp.module.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollectTask {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 唯一标识
     */
    private String uuid;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务描述
     */
    private String taskDesc;

    /**
     * 执行人ID
     */
    private Long executorId;

    /**
     * 开始时间
     */
    private OffsetDateTime startTime;

    /**
     * 结束时间
     */
    private OffsetDateTime endTime;

    /**
     * 操作方法ID
     */
    private Long methodId;

    /**
     * 设备ID
     */
    private Long deviceId;

    /**
     * 状态（0-未开始，1-进行中，2-已完成，3-已终止）
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