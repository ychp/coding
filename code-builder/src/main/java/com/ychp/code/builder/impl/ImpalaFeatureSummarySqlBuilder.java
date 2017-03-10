package com.ychp.code.builder.impl;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ychp.code.builder.Builder;
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
public class ImpalaFeatureSummarySqlBuilder extends Builder {

    private static final String COLUMN_SPLIT = "|";

    private static final String COLUMN_SPLIT_REGEX = "\\|";

    protected String buildFile(String templatePath, Map<String, Object> paramMap) throws IOException {
        Handlebars handlebars = new Handlebars();
        Template template = handlebars.compile(templatePath);
        return template.apply(paramMap);
    }

    protected String getDefaultParamPath() {
        return "impala-feature-summary/param";
    }

    @Override
    protected Map<String, Object> getParams(BufferedReader bufferedReader) throws IOException {
        Map<String,Object> paramMap = new HashMap<>();
        String line;
        String[] dataArr;
        List<Map<String, String>> columns = Lists.newArrayList();
        Map<String, String> data;
        while ((line = bufferedReader.readLine()) != null){
            if(StringUtils.isEmpty(line.trim()) || !line.contains(COLUMN_SPLIT)) {
                continue;
            }
            dataArr = line.trim().split(COLUMN_SPLIT_REGEX);
            if(dataArr.length < 2 || (StringUtils.isEmpty(dataArr[0]) && StringUtils.isEmpty(dataArr[1]))){
                continue;
            }
            data = Maps.newHashMap();
            data.put("name", dataArr[0]);
            data.put("id", dataArr[1]);
            columns.add(data);
        }

        return paramMap;
    }

    protected String getDefaultTemplate() {
        return "impala-feature-summary/template";
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
        Builder builder = new ImpalaFeatureSummarySqlBuilder();
        builder.build(args);
    }
}
