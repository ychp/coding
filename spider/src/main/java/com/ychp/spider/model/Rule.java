package com.ychp.spider.model;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 16/10/12
 */
@Data
public class Rule {

    private String urlRegx;

    private List<String> keyWord = Lists.newArrayList();

    private List<String> tags = Lists.newArrayList();

    private List<String> videoTag = Lists.newArrayList();

    private List<String> imageTag = Lists.newArrayList();

    private List<String> textTag = Lists.newArrayList();

    private List<String> subTag = Lists.newArrayList();

    private String keyWords;

}
