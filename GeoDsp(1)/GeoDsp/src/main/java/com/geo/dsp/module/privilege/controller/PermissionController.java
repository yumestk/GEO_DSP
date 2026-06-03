package com.geo.dsp.module.privilege.controller;

import com.geo.dsp.common.vo.PageVo;
import com.geo.dsp.common.result.Result;
import com.geo.dsp.module.privilege.dto.PermissionAddDTO;
import com.geo.dsp.module.privilege.dto.PermissionUpdateDTO;
import com.geo.dsp.module.privilege.service.PermissionService;
import com.geo.dsp.module.privilege.vo.PermissionVO;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/system/permissions")
public class PermissionController {

    @Resource
    private PermissionService permissionService;

    @GetMapping("/list")
    public Result<PageVo<PermissionVO>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        return permissionService.getPage(keyword, pageNum, pageSize);
    }

    @PostMapping("/add")
    public Result<Boolean> add(
            @Valid @RequestBody PermissionAddDTO dto,
            @RequestParam(value = "createBy", required = false, defaultValue = "1") Long createBy
    ) {
        return permissionService.add(dto, createBy);
    }

    @PutMapping("/update")
    public Result<Boolean> update(
            @Valid @RequestBody PermissionUpdateDTO dto,
            @RequestParam(value = "updateBy", required = false, defaultValue = "1") Long updateBy
    ) {
        return permissionService.update(dto, updateBy);
    }

    @DeleteMapping("/{uuid}")
    public Result<Boolean> delete(@PathVariable String uuid) {
        return permissionService.delete(uuid);
    }

    @GetMapping("/all")
    public Result<?> getAllPermissions() {
        return permissionService.getAllPermissions();
    }

    @GetMapping("/role/{roleUuid}")
    public Result<?> getPermissionsByRole(@PathVariable String roleUuid) {
        return permissionService.getPermissionUuidsByRoleUuid(roleUuid);
    }

    @PostMapping("/role/save")
    public Result<Boolean> saveRolePermissions(@RequestBody Map<String, Object> body) {
        String roleUuid = (String) body.get("roleUuid");
        @SuppressWarnings("unchecked")
        java.util.List<String> permissionCodes = (java.util.List<String>) body.get("permissionCodes");
        Object updateByObj = body.get("updateBy");
        Long updateBy = updateByObj != null ? ((Number) updateByObj).longValue() : 1L;

        if (roleUuid == null || roleUuid.isEmpty()) {
            return Result.fail("角色UUID不能为空");
        }
        if (permissionCodes == null) {
            return Result.fail("权限编码列表不能为空");
        }

        return permissionService.saveRolePermissions(roleUuid, permissionCodes, updateBy);
    }
}
