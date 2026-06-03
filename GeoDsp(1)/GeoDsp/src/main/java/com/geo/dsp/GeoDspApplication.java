package com.geo.dsp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
// 启用事务管理（适配业务层的@Transactional注解）
@EnableTransactionManagement
public class GeoDspApplication {
    public static void main(String[] args) {
        SpringApplication.run(GeoDspApplication.class, args);
    }
}