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

    //application config
    private String appName;

    private String modelVersion;

    private String loggerType;

    private String owner = "";

    //protocol config
    private String host = "127.0.0.1";

    private Integer port = -1;

    private Integer threads = 100;

    //registry config
    private String registryAddress = "127.0.0.1:2181";

    private Integer timeout = 10000;

    private String registry;

    private Boolean check = true;

    private Integer heartBeats = 10000;

    private String serialization;

    private String version = "1.0.0";



}
