package com.geo.dsp.module.privilege.entity;

import lombok.Data;
import java.time.OffsetDateTime;

/**
 * 操作员管理表实体，对应表：operator_manage
 */
@Data
public class OperatorManage {
    private Long id;
    private String uuid;
    private Long companyId;
    private Long userId;
    private String operatorName;
    private String phoneNum;
    private Integer status;          // 0-未工作 1-工作
    private OffsetDateTime startTime;
    private OffsetDateTime stopTime;
    private Long createBy;
    private OffsetDateTime createTime;
    private Long updateBy;
    private OffsetDateTime updateTime;
    private Boolean isDel;
    private String note;
}
