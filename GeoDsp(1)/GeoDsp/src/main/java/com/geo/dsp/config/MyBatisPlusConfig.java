package com.geo.dsp.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis-Plus分页插件配置
 * @author xxx
 */
@Configuration
@MapperScan("com.geo.dsp.module.*.mapper") // 扫描所有Mapper接口
public class MyBatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 分页插件，适配PostgreSQL
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return interceptor;
    }
}