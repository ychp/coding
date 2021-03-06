package com.ychp.coding.redis.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 16/7/25
 */
@Data
@Component
@ConfigurationProperties(prefix = "spring.redis")
public class RedisProperties {

    private String host;

    private String port;

    private String password;

    @Value("${spring.redis.pool.max-active:10}")
    private String maxActive;

    @Value("${spring.redis.pool.max-idle:5}")
    private String maxIdle;

    @Value("${spring.redis.pool.min-idle:2}")
    private String minIdle;

    @Value("${spring.redis.pool.max-wait:6000}")
    private String maxWait;

    private String database;

    private String timeout;
}
