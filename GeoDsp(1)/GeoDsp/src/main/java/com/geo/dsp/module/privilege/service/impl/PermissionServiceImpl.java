package com.geo.dsp.module.privilege.service.impl;

import com.geo.dsp.common.vo.PageVo;
import com.geo.dsp.common.result.Result;
import com.geo.dsp.module.privilege.dto.PermissionAddDTO;
import com.geo.dsp.module.privilege.dto.PermissionUpdateDTO;
import com.geo.dsp.module.privilege.entity.Permission;
import com.geo.dsp.module.privilege.mapper.PermissionMapper;
import com.geo.dsp.module.privilege.mapper.RoleMapper;
import com.geo.dsp.module.privilege.service.PermissionService;
import com.geo.dsp.module.privilege.service.RolePermissionService;
import com.geo.dsp.module.privilege.vo.PermissionVO;
import com.geo.dsp.module.privilege.vo.UserPermissionVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Resource
    private PermissionMapper permissionMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RolePermissionService rolePermissionService;

    @Override
    public Result<PageVo<PermissionVO>> getPage(String keyword, Integer pageNum, Integer pageSize) {
        int offset = (pageNum - 1) * pageSize;
        List<PermissionVO> list = permissionMapper.selectPermissionPage(keyword, offset, pageSize);
        long total = permissionMapper.selectPermissionCount(keyword);

        PageVo<PermissionVO> pageVo = new PageVo<>();
        pageVo.setRecords(list);
        pageVo.setTotal(total);
        pageVo.setPageNum(pageNum);
        pageVo.setPageSize(pageSize);
        return Result.success(pageVo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> add(PermissionAddDTO dto, Long createBy) {
        Permission p = new Permission();
        p.setPermissionName(dto.getPermissionName());
        p.setPermissionCode(dto.getPermissionCode());
        p.setPermissionDesc(dto.getPermissionDesc());
        p.setPermissionType(dto.getPermissionType());
        p.setParentPermission(dto.getParentPermission());
        p.setStatus(dto.getStatus());
        p.setCreateBy(createBy);
        p.setUpdateBy(createBy);
        p.setIsDel(false);

        return permissionMapper.insertPermission(p) > 0
                ? Result.success(true)
                : Result.fail("新增权限失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> update(PermissionUpdateDTO dto, Long updateBy) {
        Permission p = new Permission();
        p.setUuid(dto.getUuid());
        p.setPermissionName(dto.getPermissionName());
        p.setPermissionCode(dto.getPermissionCode());
        p.setPermissionDesc(dto.getPermissionDesc());
        p.setPermissionType(dto.getPermissionType());
        p.setParentPermission(dto.getParentPermission());
        p.setStatus(dto.getStatus());
        p.setUpdateBy(updateBy);

        return permissionMapper.updatePermission(p) > 0
                ? Result.success(true)
                : Result.fail("修改失败，权限不存在");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> delete(String uuid) {
        return permissionMapper.deleteByUuid(uuid) > 0
                ? Result.success(true)
                : Result.fail("删除失败，权限不存在");
    }

    @Override
    public Result<List<PermissionVO>> getAllPermissions() {
        List<PermissionVO> list = permissionMapper.selectAllPermissions();
        return Result.success(list);
    }

    @Override
    public Result<List<String>> getPermissionUuidsByRoleUuid(String roleUuid) {
        Long roleId = roleMapper.selectIdByUuid(roleUuid);
        if (roleId == null) {
            return Result.fail("角色不存在");
        }
        List<String> uuids = permissionMapper.selectPermissionUuidsByRoleId(roleId);
        return Result.success(uuids);
    }

    @Override
    public Result<List<UserPermissionVO>> getUserPermissions(String userUuid) {
        List<UserPermissionVO> list = permissionMapper.selectUserPermissionsByUserUuid(userUuid);
        return Result.success(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> saveRolePermissions(String roleUuid, List<String> permissionCodes, Long updateBy) {
        if (permissionCodes == null || permissionCodes.isEmpty()) {
            boolean ok = rolePermissionService.assignPermissionsToRole(roleUuid, java.util.Collections.emptyList(), 1L, updateBy);
            return ok ? Result.success(true) : Result.fail("权限分配失败");
        }
        List<String> permissionUuids = permissionMapper.selectUuidsByCodes(permissionCodes);
        boolean ok = rolePermissionService.assignPermissionsToRole(roleUuid, permissionUuids, 1L, updateBy);
        return ok ? Result.success(true) : Result.fail("权限分配失败");
    }
}