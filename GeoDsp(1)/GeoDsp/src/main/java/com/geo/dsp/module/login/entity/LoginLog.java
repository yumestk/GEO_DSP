package com.geo.dsp.module.login.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginLog {
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
     * 操作类型（1-登录，2-登出）
     */
    private Integer operateType;

    /**
     * 操作时间
     */
    private OffsetDateTime operateTime;

    /**
     * IP地址
     */
    private String ipAddress;

    /**
     * 浏览器/设备信息
     */
    private String userAgent;

    /**
     * 是否删除（false-未删，true-已删）
     */
    private Boolean isDel;
}
