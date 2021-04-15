package com.middleware.whitelist.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 白名单配置属性
 * 在 yml 或者 properties 中读取到我们自己设定的配置信息:middleware.white
 *
 * @author liujm
 */
@ConfigurationProperties(prefix = "middleware.white")
public class WhiteListProperties {
    private String users;

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }
}
