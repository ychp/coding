package com.ychp.coding.rpc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 16/9/21
 */
@Component
@ConfigurationProperties(prefix = "rpc")
@Data
public class RpcProperties {

    private String mode;

}
