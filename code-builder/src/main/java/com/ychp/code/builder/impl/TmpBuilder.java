package com.ychp.code.builder.impl;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ychp.code.builder.Builder;
import com.ychp.code.builder.dto.HiveColumnDto;
import com.ychp.code.builder.utils.MybatisUtils;
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
public class TmpBuilder extends Builder {

    private static final String COLUMN_SPLIT = "|";

    private static final String COLUMN_SPLIT_REGEX = "\\|";

    protected String buildFile(String templatePath, Map<String, Object> paramMap) throws IOException {
        Handlebars handlebars = new Handlebars();
        Template template = handlebars.compile(templatePath);
        return template.apply(paramMap);
    }

    protected String getDefaultParamPath() {
        return "tmp/param";
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
            data = Maps.newHashMap();
            data.put("name", dataArr[1]);
            data.put("ename", MybatisUtils.underscoreName(dataArr[2]));
            data.put("id", dataArr[0]);
            columns.add(data);
        }
        paramMap.put("datas", columns);
        return paramMap;
    }

    protected String getDefaultTemplate() {
        return "tmp/template";
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
        Builder builder = new TmpBuilder();
        builder.build(args);
    }
}
