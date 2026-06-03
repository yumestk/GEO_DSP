package com.geo.dsp.module.login.controller;

import com.geo.dsp.common.result.Result;
import com.geo.dsp.module.login.dto.LoginDTO;
import com.geo.dsp.module.login.service.LoginService;
import com.geo.dsp.module.login.vo.LoginVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class LoginController {

    @Resource
    private LoginService loginService;

    @PostMapping("/login")
    public Result<LoginVO> login(@Validated @RequestBody LoginDTO dto) {
        try {
            return Result.success(loginService.login(dto));
        } catch (Exception e) {
            log.error("登录失败", e);
            return Result.fail(e.getMessage() != null ? e.getMessage() : "登录失败，请检查网络或数据库连接");
        }
    }

    @GetMapping("/info")
    public Result<LoginVO.UserInfo> info(@RequestHeader(value = "Authorization", required = false) String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        try {
            return Result.success(loginService.getUserInfo(token));
        } catch (Exception e) {
            log.error("获取用户信息失败", e);
            return Result.fail(e.getMessage() != null ? e.getMessage() : "获取用户信息失败");
        }
    }

    @PostMapping("/logout")
    public Result<Boolean> logout(
            @RequestHeader(value = "Authorization", required = false) String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        loginService.logout(token);
        return Result.success(true);
    }
}
