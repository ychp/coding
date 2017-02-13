package com.ychp.image;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 16/10/23
 */
@Data
@Component
@ConfigurationProperties(prefix = "image")
public class ImageProperties {

    private String imageUrl;

    private String path;
}
