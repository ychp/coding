package com.ychp.log.model;

import lombok.Data;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 16/9/30
 */
@Data
public class UserAgent {

    private String system;

    private String systemName;

    private String browser;

    private String browserName;

    private String browserVersion;

    private String device;

    private String type;

}
