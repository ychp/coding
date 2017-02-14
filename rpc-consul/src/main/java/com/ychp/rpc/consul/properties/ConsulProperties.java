package com.ychp.rpc.consul.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 17/2/13
 */
@Data
@Component
@ConfigurationProperties(prefix = "rpc.consul")
public class ConsulProperties {

    private String address;

    private String host;

    private Integer port;

    private String health = "http://127.0.0.1:9020";

    private Integer interval = 5;

    private String version;
}
