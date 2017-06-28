package com.ychp.session;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 2017/6/26
 */
@Data
@Component
@ConfigurationProperties(prefix = "session")
public class SessionProperties {

    @Value("${session.redis.host:127.0.0.1}")
    private String redisHost;

    @Value("${session.redis.port:6379}")
    private String redisPort;

    @Value("${session.redis.password:}")
    private String password;

    @Value("${session.redis.pool.max-active:10}")
    private String maxActive;

    @Value("${session.redis.pool.max-idle:5}")
    private String maxIdle;

    @Value("${session.redis.pool.min-idle:2}")
    private String minIdle;

    @Value("${session.redis.pool.max-wait:6000}")
    private String maxWait;

    @Value("${session.redis.database:0}")
    private String database;

    @Value("${session.redis.timeout:1800}")
    private String timeout;

    @Value("${session.max-age:1800}")
    private Integer maxAge;

    private String domain;
}
