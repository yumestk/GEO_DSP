package com.geo.dsp.module.privilege.service;

import com.geo.dsp.common.vo.PageVo;
import com.geo.dsp.common.result.Result;
import com.geo.dsp.module.privilege.dto.RoleAddDTO;
import com.geo.dsp.module.privilege.dto.RoleUpdateDTO;
import com.geo.dsp.module.privilege.vo.RoleVO;

import java.util.List;

public interface RoleService {
    /**
     * 分页查询角色列表
     */
    Result<PageVo<RoleVO>> getRolePage(String keyword, Integer pageNum, Integer pageSize);

    /**
     * 新增角色
     */
    Result<Boolean> addRole(RoleAddDTO dto, Long createBy);

    /**
     * 修改角色
     */
    Result<Boolean> updateRole(RoleUpdateDTO dto, Long updateBy);

    /**
     * 删除角色（逻辑删除）
     */
    Result<Boolean> deleteRole(String uuid);

    /**
     * 获取全部角色列表（不分页，供下拉框用）
     */
    Result<List<RoleVO>> getAllRoles();
}