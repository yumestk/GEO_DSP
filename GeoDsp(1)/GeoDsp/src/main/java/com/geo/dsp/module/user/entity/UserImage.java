package com.geo.dsp.module.user.entity;

import lombok.Data;
import java.util.UUID;

/**
 * 用户头像实体类
 * 表说明：存储用户头像二进制数据
 */
@Data
public class UserImage {
    private Long id;
    private String uuid;
    private byte[] image;
    private Boolean isDel;

    public void setUuid(String uuid) {
        if (uuid == null || uuid.isEmpty()) {
            this.uuid = UUID.randomUUID().toString().replace("-", "");
        } else if (!uuid.matches("^[0-9a-fA-F]{32}$")) {
            throw new IllegalArgumentException("头像UUID格式非法");
        } else {
            this.uuid = uuid.toLowerCase();
        }
    }
}