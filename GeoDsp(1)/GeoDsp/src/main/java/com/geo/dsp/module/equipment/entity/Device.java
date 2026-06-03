package com.geo.dsp.module.equipment.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Device {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 唯一标识
     */
    private String uuid;

    /**
     * 企业ID
     */
    private Long companyId;

    /**
     * 设备编号
     */
    private String deviceNum;

    /**
     * 设备类型ID
     */
    private Long deviceTypeId;

    /**
     * 天线数量
     */
    private Integer antennaNum;

    /**
     * 状态（0-停用，1-启用，2-维修中）
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
