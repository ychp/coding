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
public class MysqlInsertSqlBuilder extends Builder {

    private static final String COLUMN_SPLIT = "|";

    private static final String COLUMN_SPLIT_REGEX = "\\|";

    protected String buildFile(String templatePath, Map<String, Object> paramMap) throws IOException {
        Handlebars handlebars = new Handlebars();
        Template template = handlebars.compile(templatePath);
        return template.apply(paramMap);
    }

    protected String getDefaultParamPath() {
        return "mysql-insert/param";
    }

    @Override
    protected Map<String, Object> getParams(BufferedReader bufferedReader) throws IOException {
        Map<String,Object> paramMap = new HashMap<>();
        String line;
        Boolean isData = false;
        String[] dataArr;
        List<Map<String, String>> columns = Lists.newArrayList();
        Map<String, String> data;
        Integer multiple = 1;
        while ((line = bufferedReader.readLine()) != null){
            if(line.contains("data start")) {
                isData = true;
                continue;
            } else if(line.contains("data end")) {
                isData = false;
                paramMap.put("datas", columns);
                continue;
            }

            if(!isData){
                parseNormalParam(line, paramMap);
            } else {
                if(StringUtils.isEmpty(line.trim()) || !line.contains(COLUMN_SPLIT)) {
                    continue;
                }
                multiple = paramMap.get("multiple") == null ? 1 : Integer.valueOf(paramMap.get("multiple").toString());
                dataArr = line.trim().split(COLUMN_SPLIT_REGEX);
                data = Maps.newHashMap();
                data.put("low", (int)(Double.valueOf(dataArr[0]) * multiple) + "");
                if(dataArr.length == 2 ){
                    data.put("high", "null");
                    data.put("score", dataArr[1] + "");
                } else {
                    data.put("high", (int)(Double.valueOf(dataArr[1]) * multiple) + "");
                    data.put("score", dataArr[2] + "");
                }
                columns.add(data);
            }

        }

        return paramMap;
    }

    protected String getDefaultTemplate() {
        return "mysql-insert/template";
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
        Builder builder = new MysqlInsertSqlBuilder();
        builder.build(args);
    }
}
