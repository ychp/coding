package com.ychp.coding.freemarker.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 16/7/30
 */
@ConfigurationProperties(prefix = "spring.freemarker")
public class CustomerFreemarkerProperties {

    private Map<String,Object> variables = new HashMap<>();

    public void setVariables(Map<String,Object> variables){
        this.variables = variables;
    }

    public  Map<String,Object> getVariables(){
        return variables;
    }

}
