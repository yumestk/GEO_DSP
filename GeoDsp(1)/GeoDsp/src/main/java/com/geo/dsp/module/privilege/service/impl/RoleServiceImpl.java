package com.geo.dsp.module.privilege.service.impl;

import com.geo.dsp.common.vo.PageVo;
import com.geo.dsp.common.result.Result;
import com.geo.dsp.module.privilege.dto.RoleAddDTO;
import com.geo.dsp.module.privilege.dto.RoleUpdateDTO;
import com.geo.dsp.module.privilege.entity.Role;
import com.geo.dsp.module.privilege.mapper.RoleMapper;
import com.geo.dsp.module.privilege.service.RoleService;
import com.geo.dsp.module.privilege.vo.RoleVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Resource
    private RoleMapper roleMapper;

    @Override
    public Result<PageVo<RoleVO>> getRolePage(String keyword, Integer pageNum, Integer pageSize) {
        int offset = (pageNum - 1) * pageSize;
        List<RoleVO> list = roleMapper.selectRolePage(keyword, offset, pageSize);
        long total = roleMapper.selectRoleCount(keyword);

        PageVo<RoleVO> pageVo = new PageVo<>();
        pageVo.setRecords(list);
        pageVo.setTotal(total);
        pageVo.setPageNum(pageNum);
        pageVo.setPageSize(pageSize);
        return Result.success(pageVo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> addRole(RoleAddDTO dto, Long createBy) {
        Role role = new Role();
        role.setRoleName(dto.getRoleName());
        role.setRoleCode(dto.getRoleCode());
        role.setRoleDesc(dto.getRoleDesc());
        role.setStatus(dto.getStatus());
        role.setCreateBy(createBy);
        role.setUpdateBy(createBy);
        role.setIsDel(false);

        int rows = roleMapper.insertRole(role);
        if (rows <= 0) {
            return Result.fail("新增角色失败");
        }
        return Result.success(true);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> updateRole(RoleUpdateDTO dto, Long updateBy) {
        Role role = new Role();
        role.setUuid(dto.getUuid());
        role.setRoleName(dto.getRoleName());
        role.setRoleCode(dto.getRoleCode());
        role.setRoleDesc(dto.getRoleDesc());
        role.setStatus(dto.getStatus());
        role.setUpdateBy(updateBy);

        int rows = roleMapper.updateRole(role);
        if (rows <= 0) {
            return Result.fail("角色不存在或修改失败");
        }
        return Result.success(true);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> deleteRole(String uuid) {
        int rows = roleMapper.deleteByUuid(uuid);
        if (rows <= 0) {
            return Result.fail("角色不存在或已删除");
        }
        return Result.success(true);
    }

    @Override
    public Result<List<RoleVO>> getAllRoles() {
        List<RoleVO> list = roleMapper.selectAllRoles();
        return Result.success(list);
    }
}