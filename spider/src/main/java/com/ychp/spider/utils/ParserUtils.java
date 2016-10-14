package com.ychp.spider.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.ychp.spider.model.Rule;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 16/10/14
 */
public class ParserUtils {

    public static Rule getRule(Map<String,String> ruleValues){
        Rule rule = new Rule();
        rule.setUrlRegx(ruleValues.get("url").trim());
        String keyStr = ruleValues.get("keywords");
        rule.setKeyWords(keyStr);
        if(!StringUtils.isEmpty(keyStr)){
            List<String> tagTypes = Lists.newArrayList(keyStr.trim().split(","));
            rule.setKeyWord(tagTypes);
        }
        String videoStr = ruleValues.get("videoTag");
        if(!StringUtils.isEmpty(videoStr)){
            List<String> tagTypes = Lists.newArrayList(videoStr.trim().split(","));
            rule.setVideoTag(tagTypes);
        }
        String imageStr = ruleValues.get("imageTag");
        if(!StringUtils.isEmpty(imageStr)){
            List<String> tagTypes = Lists.newArrayList(imageStr.trim().split(","));
            rule.setImageTag(tagTypes);
        }
        String textStr = ruleValues.get("textTag");
        if(!StringUtils.isEmpty(textStr)){
            List<String> tagTypes = Lists.newArrayList(textStr.trim().split(","));
            rule.setTextTag(tagTypes);
        }
        String subTag = ruleValues.get("subTag");
        if(!StringUtils.isEmpty(subTag)){
            List<String> tagTypes = Lists.newArrayList(subTag.trim().split(","));
            rule.setSubTag(tagTypes);
        }
        Set<String> tags = Sets.newHashSet();
        tags.addAll(rule.getVideoTag());
        tags.addAll(rule.getImageTag());
        tags.addAll(rule.getTextTag());
        tags.addAll(rule.getSubTag());
        rule.getTags().addAll(tags);
        return rule;
    }
}
