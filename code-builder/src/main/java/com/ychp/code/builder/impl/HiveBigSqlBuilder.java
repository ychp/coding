package com.ychp.code.builder.impl;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ychp.code.builder.Builder;
import com.ychp.code.builder.dto.HiveColumnDto;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ychp.code.builder.utils.BuilderUtils.SPLIT_COM;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 17/2/10
 */
public class HiveBigSqlBuilder extends Builder {

    protected String buildFile(String templatePath, Map<String, Object> paramMap) throws IOException {
        Map<String, Object> params = Maps.newHashMap();
        params.put("database", paramMap.get("database"));
        Long baseIndex = Long.valueOf((String) paramMap.get("base_index"));
        Long interval = Long.valueOf((String) paramMap.get("interval"));
        Long endIndex = Long.valueOf((String) paramMap.get("end_index"));

        Handlebars handlebars = new Handlebars();
        Template template = handlebars.compile(templatePath);
        StringBuilder result = new StringBuilder();
        Long size = 0L;
        for(;baseIndex >= endIndex;){
            params.put("partitionId", baseIndex);
            result.append(template.apply(params));
            size ++;
            baseIndex -= interval;
        }

        System.out.println("final size : " + size);
        return result.toString();
    }

    protected String getDefaultParamPath() {
        return "hive-big/param";
    }

    protected String getDefaultTemplate() {
        return "hive-big/item-template";
    }

    @Override
    protected String[] getFileSuff(Object fileSuffStr){
        if(fileSuffStr == null || StringUtils.isEmpty((String)fileSuffStr)){
            return new String[]{".q"};
        } else {
            return ((String) fileSuffStr).split(SPLIT_COM);
        }
    }

    public static void main(String[] args){
        Builder builder = new HiveBigSqlBuilder();
        builder.build(args);
    }
}
