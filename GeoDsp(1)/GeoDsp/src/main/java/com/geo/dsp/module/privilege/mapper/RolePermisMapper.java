package com.geo.dsp.module.privilege.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.geo.dsp.module.privilege.entity.RolePermis;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色权限关联Mapper（对应前端“角色权限配置”功能）
 */
@Mapper
public interface RolePermisMapper extends BaseMapper<RolePermis> {
    int batchInsert(@Param("list") List<RolePermis> rolePermisList);
    void insertBatch(@Param("roleId") Long roleId, @Param("permissionIds") List<Long> permissionIds);
    void deleteByRoleId(@Param("roleId") Long roleId);

    // 根据角色ID查询权限码列表（前端动态控件用）
    List<String> selectPermissionCodesByRoleId(@Param("roleId") Long roleId);
}