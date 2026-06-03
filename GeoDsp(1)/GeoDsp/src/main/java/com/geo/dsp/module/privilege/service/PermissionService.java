package com.geo.dsp.module.privilege.service;

import com.geo.dsp.common.vo.PageVo;
import com.geo.dsp.common.result.Result;
import com.geo.dsp.module.privilege.dto.PermissionAddDTO;
import com.geo.dsp.module.privilege.dto.PermissionUpdateDTO;
import com.geo.dsp.module.privilege.vo.PermissionVO;
import com.geo.dsp.module.privilege.vo.UserPermissionVO;

import java.util.List;

public interface PermissionService {
    Result<PageVo<PermissionVO>> getPage(String keyword, Integer pageNum, Integer pageSize);
    Result<Boolean> add(PermissionAddDTO dto, Long createBy);
    Result<Boolean> update(PermissionUpdateDTO dto, Long updateBy);
    Result<Boolean> delete(String uuid);

    // 获取全部权限列表（不分页，供角色权限分配下拉/穿梭框用）
    Result<List<PermissionVO>> getAllPermissions();

    // 根据角色UUID获取已分配的权限UUID列表
    Result<List<String>> getPermissionUuidsByRoleUuid(String roleUuid);

    // 根据用户UUID获取权限详情（用于登录返回，前端构建菜单和按钮控制）
    Result<List<UserPermissionVO>> getUserPermissions(String userUuid);

    // 保存角色权限分配（前端传入权限编码列表，后端转成UUID存储）
    Result<Boolean> saveRolePermissions(String roleUuid, List<String> permissionCodes, Long updateBy);
}