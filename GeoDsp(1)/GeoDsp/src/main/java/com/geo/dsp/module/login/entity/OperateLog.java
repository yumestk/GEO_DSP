package com.geo.dsp.module.login.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperateLog {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 唯一标识
     */
    private String uuid;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 企业ID
     */
    private Long companyId;

    /**
     * 模块名称
     */
    private String moduleName;

    /**
     * 操作类型（1-新增，2-修改，3-删除，4-查询）
     */
    private Integer operateType;

    /**
     * 操作描述
     */
    private String operateDesc;

    /**
     * 操作时间
     */
    private OffsetDateTime operateTime;

    /**
     * 操作前记录
     */
    private String operateRecord;

    /**
     * 操作后记录
     */
    private String newRecord;

    /**
     * 是否删除（false-未删，true-已删）
     */
    private Boolean isDel;
}
