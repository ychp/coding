package com.ychp.redis.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 16/7/25
 */
@Data
@Component
@ConfigurationProperties(prefix = "redis")
public class RedisProperties {

    private String host = "127.0.0.1";

    private Integer port = 6379;

    private String password;

    private RedisPoolProperties pool;

    private Integer database = 0;

    private Integer timeout = 6000;
}
