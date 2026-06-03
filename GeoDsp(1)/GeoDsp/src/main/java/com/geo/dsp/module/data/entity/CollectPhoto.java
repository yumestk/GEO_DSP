package com.geo.dsp.module.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollectPhoto {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 唯一标识
     */
    private String uuid;

    /**
     * 测点ID
     */
    private Long pointId;

    /**
     * 照片URL
     */
    private String photoUrl;

    /**
     * 照片名称
     */
    private String photoName;

    /**
     * 照片大小（字节）
     */
    private Long photoSize;

    /**
     * 采集时间
     */
    private OffsetDateTime collectTime;

    /**
     * 上传人ID
     */
    private Long uploaderId;

    /**
     * 创建人ID
     */
    private Long createBy;

    /**
     * 创建时间
     */
    private OffsetDateTime createTime;

    /**
     * 备注
     */
    private String note;

    /**
     * 是否删除（false-未删，true-已删）
     */
    private Boolean isDel;
}
