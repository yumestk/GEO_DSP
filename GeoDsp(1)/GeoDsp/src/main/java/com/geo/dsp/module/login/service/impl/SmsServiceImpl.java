package com.geo.dsp.module.login.service.impl;

import com.geo.dsp.module.login.service.SmsService;
import com.geo.dsp.module.login.util.AliyunSmsUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class SmsServiceImpl implements SmsService {

    private static final String SMS_CODE_PREFIX = "sms:code:";
    private static final int SMS_CODE_EXPIRE = 300;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private AliyunSmsUtil aliyunSmsUtil;

    @Override
    public void sendSmsCode(String phoneNum) {
        String code = generateCode();
        
        // 存入Redis，5分钟有效
        redisTemplate.opsForValue().set(SMS_CODE_PREFIX + phoneNum, code, SMS_CODE_EXPIRE, TimeUnit.SECONDS);
        
        // TODO: 阿里云配置填好后，取消下面注释启用实际短信发送
        // aliyunSmsUtil.sendSms(phoneNum, code);
        
        log.info("手机号 {} 验证码: {} (5分钟内有效)", phoneNum, code);
    }

    @Override
    public boolean verifyCode(String phoneNum, String code) {
        String savedCode = (String) redisTemplate.opsForValue().get(SMS_CODE_PREFIX + phoneNum);
        if (savedCode == null) {
            return false;
        }
        if (savedCode.equals(code)) {
            redisTemplate.delete(SMS_CODE_PREFIX + phoneNum);
            return true;
        }
        return false;
    }

    private String generateCode() {
        return String.format("%06d", new Random().nextInt(1000000));
    }
}