package com.geo.dsp.module.user.controller;

import com.geo.dsp.module.user.dto.UserAddDTO;
import com.geo.dsp.module.user.dto.UserUpdateDTO;
import com.geo.dsp.module.user.entity.User;
import com.geo.dsp.module.user.service.UserService;
import com.geo.dsp.common.result.Result;
import com.geo.dsp.module.user.vo.UserListVO;
import com.geo.dsp.common.vo.PageVo;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/add")
    public Result<Boolean> add(
            @Valid @RequestBody UserAddDTO dto,
            @RequestParam(value = "createBy", required = false, defaultValue = "1") Long createBy
    ) {
        return userService.addUser(dto, createBy);
    }

    @DeleteMapping("/{uuid}")
    public Result<Boolean> deleteUser(
            @PathVariable String uuid,
            @RequestParam(value = "updateBy", required = false) Long updateBy
    ) {
        return userService.deleteUser(uuid);
    }

    @PutMapping("/update")
    public Result<Boolean> update(
            @Valid @RequestBody UserUpdateDTO dto,
            @RequestParam(value = "updateBy", required = false, defaultValue = "1") Long updateBy
    ) {
        return userService.updateUser(dto, updateBy);
    }

    @GetMapping("/list")
    public Result<PageVo<UserListVO>> list(
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) Short status,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        PageVo<UserListVO> page = userService.getUserPage(userName, status, pageNum, pageSize);
        return Result.success(page);
    }

    @GetMapping("/search")
    public Result<PageVo<UserListVO>> search(
            @RequestParam(required = false) String userName,
            @RequestParam(value = "currentPage", defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        PageVo<UserListVO> page = userService.getUserPage(userName, null, pageNum, pageSize);
        return Result.success(page);
    }

    @PostMapping("/batch")
    public Result<Boolean> batchOperation(@RequestBody Map<String, Object> body) {
        @SuppressWarnings("unchecked")
        List<String> uuids = (List<String>) body.get("uuids");
        String action = (String) body.get("action");
        Object updateByObj = body.get("updateBy");
        Long updateBy = updateByObj != null ? ((Number) updateByObj).longValue() : 1L;

        if (uuids == null || uuids.isEmpty()) {
            return Result.fail("请选择要操作的用户");
        }

        boolean success = true;
        for (String uuid : uuids) {
            switch (action) {
                case "delete":
                    success = userService.deleteUser(uuid).getData();
                    break;
                case "enable":
                    success = userService.updateStatus(uuid, (short) 1, updateBy).getData();
                    break;
                case "disable":
                    success = userService.updateStatus(uuid, (short) 0, updateBy).getData();
                    break;
                default:
                    return Result.fail("不支持的操作类型: " + action);
            }
            if (!success) break;
        }
        return success ? Result.success(true) : Result.fail("批量操作失败");
    }

    @PutMapping("/status/{uuid}")
    public Result<Boolean> status(@PathVariable String uuid,
                                  @RequestParam Short status,
                                  @RequestParam Long updateBy) {
        return userService.updateStatus(uuid, status, updateBy);
    }

    @PutMapping
    public Result<Boolean> update(@RequestBody User user,
                                  @RequestParam Long updateBy) {
        return userService.updateUser(user, updateBy);
    }

    @GetMapping("/get/{id}")
    public Result<User> get(@PathVariable Long id) {
        return userService.findById(id);
    }

    @GetMapping("/uuid/{uuid}")
    public Result<User> getByUuid(@PathVariable String uuid) {
        return userService.findByUuid(uuid);
    }

    @GetMapping("/account/{account}")
    public Result<User> getByAccount(@PathVariable String account) {
        return userService.findByAccount(account);
    }

    @GetMapping("/page")
    public Result<Map<String, Object>> page(
            @RequestParam Long companyId,
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) Short status,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        return userService.pageList(companyId, userName, status, pageNum, pageSize);
    }

    @PutMapping("/resetPwd/{id}")
    public Result<Boolean> resetPwd(@PathVariable Long id,
                                    @RequestParam Long updateBy) {
        return userService.resetPwd(id, updateBy);
    }

    @GetMapping("/{uuid}/permissions")
    public Result<List<String>> getPermissions(@PathVariable String uuid) {
        return userService.getPermissionCodesByUserUuid(uuid);
    }
}
