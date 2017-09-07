package com.ychp.redis.properties;

import lombok.Data;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 2017/7/19
 */
@Data
public class RedisPoolProperties {

    private Integer maxActive = 10;

    private Integer maxIdle = 5;

    private Integer minIdle = 5;

    private Integer maxWait = 6000;
}
