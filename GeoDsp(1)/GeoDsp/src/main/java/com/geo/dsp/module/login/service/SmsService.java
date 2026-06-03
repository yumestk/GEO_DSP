package com.geo.dsp.module.login.service;

public interface SmsService {
    /**
     * 发验证码
     * @param phoneNum
     */
    void sendSmsCode(String phoneNum);

    /**
     * 校验验证码
     * @param phoneNum
     * @param code
     * @return
     */
    boolean verifyCode(String phoneNum, String code);
}