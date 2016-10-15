package com.ychp.spider.core;

import com.ychp.spider.parser.Parser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 16/10/14
 */
@Slf4j
@Component
public class ParserRegistry {
    @Autowired
    private ApplicationContext applicationContext;

    private Map<String, Parser> parserBeans;

    @PostConstruct
    public void init(){
        registerActions();
    }

    private void registerActions() {
        parserBeans = applicationContext.getBeansOfType(Parser.class);
    }

    /**
     * 获取相应名称的动作
     * @param name Bean id
     * @return Action
     */
    public Parser getParser(String name){
        return parserBeans.get(name);
    }

}
