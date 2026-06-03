package com.geo.dsp.module.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperateMethod {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 唯一标识
     */
    private String uuid;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 方法编码
     */
    private String methodCode;

    /**
     * 方法描述
     */
    private String methodDesc;

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
