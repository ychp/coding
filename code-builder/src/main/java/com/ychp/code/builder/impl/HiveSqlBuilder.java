package com.ychp.code.builder.impl;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.google.common.collect.Lists;
import com.ychp.code.builder.Builder;
import com.ychp.code.builder.dto.ColumnDto;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ychp.code.builder.utils.BuilderUtils.PARAM_SPLIT_REGEX;
import static com.ychp.code.builder.utils.BuilderUtils.SPLIT_COM;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 17/2/10
 */
public class HiveSqlBuilder extends Builder {

    private static final String COLUMN_SPLIT = "|";

    private static final String COLUMN_SPLIT_REGEX = "\\|";

    protected String buildFile(String templatePath, Map<String, Object> paramMap) throws IOException {
        Handlebars handlebars = new Handlebars();
        Template template = handlebars.compile(templatePath);
        String result = template.apply(paramMap);
        System.out.println(result);
        return result;
    }

    @Override
    protected Map<String, Object> getParams(BufferedReader bufferedReader) throws IOException {
        Map<String,Object> paramMap = new HashMap<String, Object>();
        String line;
        Boolean isColumns = false;
        String[] paramKV;
        String[] columnArr;
        List<ColumnDto> columns = Lists.newArrayList();
        ColumnDto columnDto;
        String key;
        String value;
        while ((line = bufferedReader.readLine()) != null){
            if(line.contains("columns start")) {
                isColumns = true;
                continue;
            } else if(line.contains("columns end")) {
                isColumns = false;
                paramMap.put("columns", columns);
                continue;
            }

            if(!isColumns){
                if(!StringUtils.isEmpty(line.trim()) && line.contains(PARAM_SPLIT_REGEX)){
                    paramKV = line.split(PARAM_SPLIT_REGEX);
                    key = paramKV[0];
                    value = paramKV[1];
                    paramMap.put(key, value);
                    if(value.contains(SPLIT_COM)){
                        paramMap.put(key + "s", value.split(SPLIT_COM));
                    }
                }
            } else {
                if(StringUtils.isEmpty(line.trim()) || !line.contains(COLUMN_SPLIT)) {
                    continue;
                }
                columnArr = line.trim().split(COLUMN_SPLIT_REGEX);
                columnDto = new ColumnDto(columnArr[1].trim(), columnArr[2].trim());
                columns.add(columnDto);
            }

        }

        return paramMap;
    }

    protected String getDefaultTemplate() {
        return "hive/full-template,hive/inc-template";
    }

    @Override
    protected String[] getFileSuff(Object fileSuffStr){
        if(fileSuffStr == null || StringUtils.isEmpty((String)fileSuffStr)){
            return new String[]{"全量.q", "增量.q"};
        } else {
            return ((String) fileSuffStr).split(SPLIT_COM);
        }
    }

    public static void main(String[] args){
        Builder builder = new HiveSqlBuilder();
        builder.build(args);
    }
}
