package com.geo.dsp.module.privilege.entity;

import com.geo.dsp.common.util.UuidUtil;
import lombok.Data;

/**
 * 角色权限关联实体（对应前端角色权限配置功能）
 */
@Data
public class RolePermis {
    private Long id;
    private String uuid;          // 对外主键
    private Long companyId;       // 所属企业ID
    private Long roleId;          // 角色ID
    private Long permissionId;    // 权限ID
    private String permissionUuid;// 权限UUID
    private Long createBy;        // 创建人ID
    private Long updateBy;        // 修改人ID
    private Boolean isDel;        // 删除标记

    public void setUuid(String uuid) {
        this.uuid = UuidUtil.generateUuid();
    }
}