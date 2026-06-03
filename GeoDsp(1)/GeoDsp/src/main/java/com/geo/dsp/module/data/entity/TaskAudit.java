package com.geo.dsp.module.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskAudit {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 唯一标识
     */
    private String uuid;

    /**
     * 任务ID
     */
    private Long taskId;

    /**
     * 审核人ID
     */
    private Long auditorId;

    /**
     * 审核时间
     */
    private OffsetDateTime auditTime;

    /**
     * 审核结果（0-未通过，1-通过）
     */
    private Integer auditResult;

    /**
     * 审核意见
     */
    private String auditOpinion;

    /**
     * 创建人ID
     */
    private Long createBy;

    /**
     * 创建时间
     */
    private OffsetDateTime createTime;

    /**
     * 是否删除（false-未删，true-已删）
     */
    private Boolean isDel;
}