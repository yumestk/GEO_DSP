package com.geo.dsp.common.util;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * MyBatis-Plus字段自动填充处理器
 * @author xxx
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        // 填充创建时间
        this.strictInsertFill(metaObject, "createTime", OffsetDateTime::now, OffsetDateTime.class);
        // 填充UUID
        this.strictInsertFill(metaObject, "uuid", UUID::randomUUID, UUID.class);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 填充修改时间
        this.strictUpdateFill(metaObject, "updateTime", OffsetDateTime::now, OffsetDateTime.class);
    }
}