package com.geo.dsp.module.data.entity;

import lombok.Data;
import java.time.OffsetDateTime;

/**
 * 字典实体
 * 对应数据库表：dict（严格按你的库表设计）
 * UUID 由数据库自动生成 gen_random_uuid()
 */
@Data
public class Dict {

    /**
     * 自增主键
     */
    private Long id;

    /**
     * 对外唯一主键（后端自动生成）
     */
    private String uuid;

    /**
     * 字典名称
     */
    private String dictName;

    /**
     * 字典编码
     */
    private String dictCode;

    /**
     * 字典键
     */
    private String dictKey;

    /**
     * 字符串值
     */
    private String dictStrVal;

    /**
     * 数字值
     */
    private Double dictNumVal;

    /**
     * 字典描述
     */
    private String dictDesc;

    /**
     * 创建人ID
     */
    private Long createBy;

    /**
     * 创建时间
     */
    private OffsetDateTime createTime;

    /**
     * 修改人ID
     */
    private Long updateBy;

    /**
     * 修改时间
     */
    private OffsetDateTime updateTime;

    /**
     * 删除标记
     */
    private Boolean isDel;
}