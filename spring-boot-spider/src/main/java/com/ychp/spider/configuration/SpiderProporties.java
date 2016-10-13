package com.ychp.spider.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 16/10/12
 */
@Data
@Component
@ConfigurationProperties(prefix = "spider")
public class SpiderProporties {

    private Map<String, String> rule;
}
