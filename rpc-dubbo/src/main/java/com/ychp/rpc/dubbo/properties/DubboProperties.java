package com.ychp.rpc.dubbo.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 17/2/21
 */
@Data
@ConfigurationProperties(prefix = "rpc.dubbo")
public class DubboProperties {

    private String appName;

    private String registry;

    private String host;

    private Integer port;

    private Integer threads;

    private String version;

}
