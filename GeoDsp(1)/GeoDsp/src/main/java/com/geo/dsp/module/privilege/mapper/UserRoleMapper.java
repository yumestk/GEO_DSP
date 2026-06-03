package com.geo.dsp.module.privilege.mapper;

import com.geo.dsp.module.privilege.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserRoleMapper {

    /**
     * 新增用户角色关联
     */
    int insertUserRole(UserRole userRole);

    /**
     * 根据用户 UUID 删除角色关联（物理删除，因为用户已逻辑删除）
     * @param userUuid 用户 UUID
     * @return 影响行数
     */
    int deleteByUserUuid(@Param("userUuid") String userUuid);

    /**
     * 根据用户UUID查询角色编码
     */
    String selectRoleCodeByUserUuid(@Param("userUuid") String userUuid);
}