package com.ychp.coding.common;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 16/8/27
 */
@Data
@Component
@ConfigurationProperties()
public class Configs {

    @Value(value = "${api.ip.address.type}")
    private Integer ipAdressApiType;

    @Value(value = "${api.ip.address.apiKey}")
    private String ipAdressApiKey;

}
