package com.ychp.coding.rpc.dubbo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 16/9/21
 */
@Component
@ConfigurationProperties(prefix = "rpc.dubbo")
@Data
public class DubboProperties {

    private String appName;

    private String address;

    private String protocol;

    private String serialization;

    private Integer port;

    private String host;

    private Integer threads;

    private Integer heartbeat;

    private Integer retries;

    private Integer timeout;

    private String version;

    private Boolean check;

}
