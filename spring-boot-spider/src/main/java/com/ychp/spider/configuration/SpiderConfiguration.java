package com.ychp.spider.configuration;

import com.google.common.collect.Lists;
import com.ychp.spider.model.Rule;
import com.ychp.spider.parser.BaseParser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 16/10/13
 */
@Configuration
@EnableConfigurationProperties({SpiderProporties.class})
public class SpiderConfiguration {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private SpiderProporties spiderProporties;

    @PostConstruct
    public void initParser(){
        Map<String, BaseParser> parsers = applicationContext.getBeansOfType(BaseParser.class);
        Rule rule;

        for(BaseParser parser : parsers.values()){
            rule = getRule(parser.getConfigPrex());
            parser.setRule(rule);
        }
    }

    private Rule getRule(String configPrex) {
        Rule rule = new Rule();
        rule.setUrlRegx(spiderProporties.getRule().get("spider.rule.url-" + configPrex).trim());
        String keyStr = spiderProporties.getRule().get("spider.rule.keyword-" + configPrex);
        rule.setKeyWords(keyStr);
        if(!StringUtils.isEmpty(keyStr)){
            List<String> tagTypes = Lists.newArrayList(keyStr.trim().split(","));
            rule.setKeyWord(tagTypes);
        }
        String tagTypeStr = spiderProporties.getRule().get("spider.rule.tagType-" + configPrex);
        if(!StringUtils.isEmpty(tagTypeStr)){
            List<String> tagTypes = Lists.newArrayList(tagTypeStr.trim().split(","));
            rule.setTagType(tagTypes);
        }
        String videoStr = spiderProporties.getRule().get("spider.rule.video-" + configPrex);
        if(!StringUtils.isEmpty(videoStr)){
            List<String> tagTypes = Lists.newArrayList(videoStr.trim().split(","));
            rule.setVideoTag(tagTypes);
        }
        String imageStr = spiderProporties.getRule().get("spider.rule.image-" + configPrex);
        if(!StringUtils.isEmpty(imageStr)){
            List<String> tagTypes = Lists.newArrayList(imageStr.trim().split(","));
            rule.setImageTag(tagTypes);
        }
        String textStr = spiderProporties.getRule().get("spider.rule.text-" + configPrex);
        if(!StringUtils.isEmpty(textStr)){
            List<String> tagTypes = Lists.newArrayList(textStr.trim().split(","));
            rule.setTextTag(tagTypes);
        }
        String subTag = spiderProporties.getRule().get("spider.rule.subTag-" + configPrex);
        if(!StringUtils.isEmpty(subTag)){
            List<String> tagTypes = Lists.newArrayList(subTag.trim().split(","));
            rule.setSubTag(tagTypes);
        }
        return rule;
    }
}
