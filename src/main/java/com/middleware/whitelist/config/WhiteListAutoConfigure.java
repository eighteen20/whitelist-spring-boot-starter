package com.middleware.whitelist.config;

import com.middleware.whitelist.DoJoinPoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 白名单自动配置
 *
 * @author liujm
 * @ConditionalOnClass 当 WhiteListProperties 位于当前类路径上，才会实例化一个类。除此之外还有其他属于此系列的常用的注解。
 * @ConditionalOnMissingBean 仅仅在当前上下文中不存在某个对象时，才会实例化一个 Bean
 */
@Configuration
@ConditionalOnClass(WhiteListProperties.class)
@EnableConfigurationProperties(WhiteListProperties.class)
public class WhiteListAutoConfigure {

    @Bean("whiteListConfig")
    @ConditionalOnMissingBean
    public String whiteListConfig(WhiteListProperties properties) {
        return properties.getUsers();
    }

    @Bean
    @ConditionalOnMissingBean
    public DoJoinPoint point() {
        return new DoJoinPoint();
    }
}
