package com.geo.dsp.module.login.controller;

import com.geo.dsp.common.result.Result;
import com.geo.dsp.module.login.dto.SmsCodeDTO;
import com.geo.dsp.module.login.service.SmsService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/login/sms")
public class SmsController {

    @Resource
    private SmsService smsService;

    @PostMapping("/send")
    public Result<String> sendSmsCode(@Valid @RequestBody SmsCodeDTO dto) {
        smsService.sendSmsCode(dto.getPhoneNum());
        return Result.success("验证码发送成功");
    }
}