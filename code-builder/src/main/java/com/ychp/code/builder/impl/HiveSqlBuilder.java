package com.ychp.code.builder.impl;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Template;
import com.google.common.collect.Lists;
import com.ychp.code.builder.Builder;
import com.ychp.code.builder.dto.HiveColumnDto;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Handlebars handlebars = getHandlebars();
        Template template = handlebars.compile(templatePath);
        return template.apply(paramMap);
    }

    protected String getDefaultParamPath() {
        return "hive/param";
    }

    @Override
    protected Map<String, Object> getParams(BufferedReader bufferedReader) throws IOException {
        Map<String,Object> paramMap = new HashMap<>();
        String line;
        Boolean isColumns = false;
        String[] columnArr;
        List<HiveColumnDto> columns = Lists.newArrayList();
        HiveColumnDto hiveColumnDto;
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
                parseNormalParam(line, paramMap);
            } else {
                if(StringUtils.isEmpty(line.trim()) || !line.contains(COLUMN_SPLIT)) {
                    continue;
                }
                columnArr = line.trim().split(COLUMN_SPLIT_REGEX);
                hiveColumnDto = new HiveColumnDto(columnArr[1].trim(), columnArr[2].trim());
                columns.add(hiveColumnDto);
            }

        }

        return paramMap;
    }

    protected String getDefaultTemplate() {
        return "hive/full-template,hive/inc-template,hive/full-to-inc-template";
    }

    @Override
    protected String[] getFileSuff(Object fileSuffStr){
        if(fileSuffStr == null || StringUtils.isEmpty((String)fileSuffStr)){
            return new String[]{"_全量.q", "_增量.q", "_全量变增量.q"};
        } else {
            return ((String) fileSuffStr).split(SPLIT_COM);
        }
    }

    public static void main(String[] args){
        Builder builder = new HiveSqlBuilder();
        builder.build(args);
    }
}
