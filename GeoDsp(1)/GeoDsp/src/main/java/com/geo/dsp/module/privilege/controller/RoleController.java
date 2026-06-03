package com.geo.dsp.module.privilege.controller;

import com.geo.dsp.common.vo.PageVo;
import com.geo.dsp.common.result.Result;
import com.geo.dsp.module.privilege.dto.RoleAddDTO;
import com.geo.dsp.module.privilege.dto.RoleUpdateDTO;
import com.geo.dsp.module.privilege.service.RolePermissionService;
import com.geo.dsp.module.privilege.service.RoleService;
import com.geo.dsp.module.privilege.vo.RoleVO;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/system/roles")
public class RoleController {

    @Resource
    private RoleService roleService;

    @Resource
    private RolePermissionService rolePermissionService;

    /**
     * 角色列表查询（支持按名称/编码模糊搜索）
     * 前端调用：GET /system/roles?keyword=xxx&pageNum=1&pageSize=10
     */
    @GetMapping("/list")
    public Result<PageVo<RoleVO>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        return roleService.getRolePage(keyword, pageNum, pageSize);
    }

    /**
     * 新增角色
     * 前端调用：POST /system/roles
     */
    @PostMapping("/add")
    public Result<Boolean> add(
            @Valid @RequestBody RoleAddDTO dto,
            @RequestParam(value = "createBy", required = false, defaultValue = "1") Long createBy
    ) {
        return roleService.addRole(dto, createBy);
    }

    /**
     * 修改角色
     * 前端调用：PUT /system/roles
     */
    @PutMapping("/update")
    public Result<Boolean> update(
            @Valid @RequestBody RoleUpdateDTO dto,
            @RequestParam(value = "updateBy", required = false, defaultValue = "1") Long updateBy
    ) {
        return roleService.updateRole(dto, updateBy);
    }

    /**
     * 删除角色（逻辑删除）
     * 前端调用：DELETE /system/roles/{uuid}
     */
    @DeleteMapping("/{uuid}")
    public Result<Boolean> delete(@PathVariable String uuid) {
        return roleService.deleteRole(uuid);
    }

    /**
     * 获取全部角色列表（不分页，供权限管理页面下拉框用）
     */
    @GetMapping("/all")
    public Result<?> getAllRoles() {
        return roleService.getAllRoles();
    }

    /**
     * 保存角色权限分配（前端"保存权限分配"按钮调用）
     * @param roleUuid 角色UUID
     * @param permissionUuids 已分配的权限UUID列表
     */
    @PostMapping("/{roleUuid}/permissions")
    public Result<Boolean> assignPermissions(
            @PathVariable String roleUuid,
            @RequestBody List<String> permissionUuids,
            @RequestParam(defaultValue = "1") Long companyId,
            @RequestParam(defaultValue = "1") Long createBy
    ) {
        boolean ok = rolePermissionService.assignPermissionsToRole(roleUuid, permissionUuids, companyId, createBy);
        return ok ? Result.success(true) : Result.fail("权限分配失败");
    }
}