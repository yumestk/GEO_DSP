package com.geo.dsp.module.privilege.mapper;

import com.geo.dsp.module.privilege.entity.Permission;
import com.geo.dsp.module.privilege.vo.PermissionVO;
import com.geo.dsp.module.privilege.vo.UserPermissionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface PermissionMapper {

    // 新增权限
    int insertPermission(Permission permission);

    // 分页列表（支持搜索）
    List<PermissionVO> selectPermissionPage(
            @Param("keyword") String keyword,
            @Param("offset") int offset,
            @Param("size") int size
    );

    // 总数
    long selectPermissionCount(@Param("keyword") String keyword);

    // 修改
    int updatePermission(Permission permission);

    // 逻辑删除
    int deleteByUuid(@Param("uuid") String uuid);

    // 查询全部权限（不分页，供角色权限分配用）
    List<PermissionVO> selectAllPermissions();

    // 根据角色ID查询已分配的权限UUID列表
    List<String> selectPermissionUuidsByRoleId(@Param("roleId") Long roleId);

    // 根据用户UUID查询权限详情（用于登录返回，前端构建菜单和按钮控制）
    List<UserPermissionVO> selectUserPermissionsByUserUuid(@Param("userUuid") String userUuid);

    // 根据权限编码列表查询对应的UUID列表
    List<String> selectUuidsByCodes(@Param("codes") List<String> codes);
}