package com.middleware.whitelist.annotation;

import java.lang.annotation.*;

/**
 * 在需要使用到的白名单服务的接口上，添加此注解并配置必要的信息
 *
 * @author liujm
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface DoWhiteList {
    String key() default "";

    String returnJson() default "";
}
