package com.geo.dsp.module.privilege.service.impl;

import com.geo.dsp.module.privilege.entity.RolePermis;
import com.geo.dsp.module.privilege.mapper.RoleMapper;
import com.geo.dsp.module.privilege.mapper.RolePermisMapper;
import com.geo.dsp.module.privilege.service.RolePermissionService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RolePermissionServiceImpl implements RolePermissionService {

    @Resource
    private RolePermisMapper rolePermisMapper;

    @Resource
    private RoleMapper roleMapper;

    /**
     * 为角色分配权限（先清空旧关联，再批量插入新关联）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignPermissionsToRole(String roleUuid, List<String> permissionUuids,
                                           Long companyId, Long createBy) {
        Long roleId = roleMapper.selectIdByUuid(roleUuid);
        if (roleId == null) return false;

        rolePermisMapper.deleteByRoleId(roleId);

        if (permissionUuids == null || permissionUuids.isEmpty()) return true;

        List<RolePermis> list = permissionUuids.stream().map(puuid -> {
            RolePermis rp = new RolePermis();
            rp.setUuid(null);
            rp.setRoleId(roleId);
            rp.setPermissionUuid(puuid);
            rp.setCompanyId(companyId);
            rp.setCreateBy(createBy);
            rp.setUpdateBy(createBy);
            rp.setIsDel(false);
            return rp;
        }).collect(Collectors.toList());

        return rolePermisMapper.batchInsert(list) > 0;
    }
}
