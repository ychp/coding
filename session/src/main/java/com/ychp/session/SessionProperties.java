package com.ychp.session;

import com.ychp.redis.properties.RedisProperties;
import lombok.Data;
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

    private RedisProperties redis;

    private Integer maxAge = 1800;

    private String domain;
}
