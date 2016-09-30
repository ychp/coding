package com.ychp.log.model;

import lombok.Data;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 16/9/30
 */
@Data
public class Nginx {

    private String ip;

    private String timeStr;

    private String method;

    private String uri;

    private String protocol;

    private String url;

    private String domain;

    private Integer httpCode;

    private Integer contentLength;

    private String ua;

    private UserAgent userAgent;

}
