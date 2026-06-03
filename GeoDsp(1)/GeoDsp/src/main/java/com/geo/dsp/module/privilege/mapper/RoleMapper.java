package com.geo.dsp.module.privilege.mapper;

import com.geo.dsp.module.privilege.entity.Role;
import com.geo.dsp.module.privilege.vo.RoleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleMapper {
    /**
     * 新增角色
     */
    int insertRole(Role role);

    /**
     * 分页查询角色列表（支持按名称/编码模糊搜索）
     */
    List<RoleVO> selectRolePage(
            @Param("keyword") String keyword,
            @Param("offset") int offset,
            @Param("size") int size
    );

    /**
     * 查询角色总数（用于分页）
     */
    long selectRoleCount(@Param("keyword") String keyword);

    /**
     * 根据UUID修改角色信息
     */
    int updateRole(Role role);

    /**
     * 根据UUID逻辑删除角色
     */
    int deleteByUuid(@Param("uuid") String uuid);

    /**
     * 查询全部角色（不分页，供下拉框用）
     */
    List<RoleVO> selectAllRoles();

    /**
     * 根据UUID查询角色ID
     */
    Long selectIdByUuid(@Param("uuid") String uuid);

    /**
     * 根据角色编码查询角色ID
     */
    Long selectIdByRoleCode(@Param("roleCode") String roleCode);
}