package com.ychp.spider.model;

import lombok.Data;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 16/10/13
 */
@Data
public class SpiderData {

    private String dataRef;

    private String content;

    private String url;

    private String source;

    private String keyword;

    private String type;
}
