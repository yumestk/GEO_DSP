package com.geo.dsp.module.login.util;

import com.geo.dsp.config.AliyunSmsConfig;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AliyunSmsUtil {

    @Resource
    private AliyunSmsConfig smsConfig;

    public void sendSms(String phoneNum, String code) {
        try {
            log.info("阿里云短信发送: phoneNum={}, code={}, signName={}, templateCode={}", 
                    phoneNum, code, smsConfig.getSignName(), smsConfig.getTemplateCode());
            
            // TODO: 配置填好后，替换为下方实际调用
            // 实际发送需要根据新版SDK调整调用方式
            // DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", smsConfig.getAccessKeyId(), smsConfig.getAccessKeySecret());
            // IAcsClient client = new DefaultAcsClient(profile);
            // SendSmsRequest request = new SendSmsRequest();
            // ...
            
            log.info("短信验证码已发送(模拟): {}", code);
        } catch (Exception e) {
            log.error("短信发送失败: {}", e.getMessage());
            throw new RuntimeException("短信发送失败: " + e.getMessage());
        }
    }
}