package com.ychp.code.builder.impl;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.helper.UnlessHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ychp.code.builder.Builder;
import com.ychp.code.builder.dto.MybatisColumnDto;
import com.ychp.code.builder.utils.MybatisUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ychp.code.builder.utils.BuilderUtils.*;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 17/2/10
 */
public class MybatisBuilder extends Builder {

    private static final String TABLE_NAME_KEY = "tableName";
    private static final String DATABASE_TYPE_KEY = "databaseType";
    private static final String DATABASE_HOST_KEY = "host";
    private static final String DATABASE_PORT_KEY = "port";
    private static final String DATABASE_KEY = "database";
    private static final String DATABASE_USERNAME_KEY = "username";
    private static final String DATABASE_PASSWORD_KEY = "password";
    private static final String MODEL_FILTER_COLUMN_KEY = "modelFilterColumn";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern("yyyy/MM/dd");

    protected String buildFile(String templatePath, Map<String, Object> paramMap) throws IOException {
        Handlebars handlebars = new Handlebars();
        handlebars.registerHelper("formatDate", (Helper<Date>) (context, options) -> {
            if (context == null){
                return null;
            }
            return new DateTime(context).toString(DATE_TIME_FORMATTER);
        });
        Template template = handlebars.compile(templatePath);
        return template.apply(paramMap);
    }

    protected String getDefaultParamPath() {
        return "mybatis/param";
    }

    @Override
    protected Map<String, Object> generalTemplateParamMap(Map<String, Object> paramMap) {
        Map<String, Object> templateParamMap = Maps.newHashMap();
        templateParamMap.put(TABLE_NAME_KEY, paramMap.get(TABLE_NAME_KEY));
        templateParamMap.put("modelName", paramMap.get("modelName"));
        templateParamMap.put("modelPackage", paramMap.get("modelPackage"));
        templateParamMap.put("package", paramMap.get("package"));

        String tableName = (String) paramMap.get(TABLE_NAME_KEY);
        String databaseType = (String) paramMap.get(DATABASE_TYPE_KEY);
        String host = (String) paramMap.get(DATABASE_HOST_KEY);
        String port = (String) paramMap.get(DATABASE_PORT_KEY);
        String database = (String) paramMap.get(DATABASE_KEY);
        String username = (String) paramMap.get(DATABASE_USERNAME_KEY);
        String password = (String) paramMap.get(DATABASE_PASSWORD_KEY);
        String modelFilterColumn = (String) paramMap.get(MODEL_FILTER_COLUMN_KEY );
        List<String> modelFilterColumns = null;
        if(!StringUtils.isEmpty(modelFilterColumn)){
            modelFilterColumns = Lists.newArrayList(modelFilterColumn.split(","));
        }

        String url="jdbc:mysql://" + host + ":" + port + "/" + database + "?user=" + username + "&password=" + password;
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection(url);
            DatabaseMetaData dbMetaData = connection.getMetaData();

            MybatisColumnDto columnDto;
            String columnName;
            String columnType;
            int datasize;

            //主键
            MybatisColumnDto primaryColumnDto = null;
            String catalog = connection.getCatalog();
            ResultSet columnRs = dbMetaData.getPrimaryKeys(catalog, null, tableName);
            while(columnRs.next()){
                columnName = columnRs.getString("COLUMN_NAME");
                primaryColumnDto = new MybatisColumnDto();
                primaryColumnDto.setSqlColumn(columnName);
                primaryColumnDto.setJavaColumn(MybatisUtils.camelName(columnName));
                primaryColumnDto.setJavaXmlColumn("{" + MybatisUtils.camelName(columnName) + "}");
            }

            List<MybatisColumnDto> columns = Lists.newArrayList();
            ResultSet colRet = dbMetaData.getColumns(null,"%", tableName,"%");
            while(colRet.next()) {
                columnName = colRet.getString("COLUMN_NAME");
                columnType = colRet.getString("TYPE_NAME");
                datasize = colRet.getInt("COLUMN_SIZE");
                if(primaryColumnDto != null && primaryColumnDto.getSqlColumn().equals(columnName)){
                    primaryColumnDto.setJavaType(MybatisUtils.getJavaTypeByDBType(columnType, datasize));
                    primaryColumnDto.setModelColumn(false);
                    templateParamMap.put("primaryColumn", primaryColumnDto);
                    continue;
                }

                columnDto = new MybatisColumnDto();
                columnDto.setSqlColumn(columnName);
                columnDto.setJavaColumn(MybatisUtils.camelName(columnName));
                columnDto.setJavaXmlColumn("{" + MybatisUtils.camelName(columnName) + "}");
                columnDto.setJavaType(MybatisUtils.getJavaTypeByDBType(columnType, datasize));

                if(modelFilterColumns != null
                        && (modelFilterColumns.contains(columnDto.getJavaColumn())
                        || modelFilterColumns.contains(columnDto.getJavaXmlColumn()))){
                    columnDto.setModelColumn(false);
                }
                columns.add(columnDto);
            }

            templateParamMap.put("columns", columns);


        } catch (SQLException | IllegalAccessException | InstantiationException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
          if(connection != null){
              try {
                  connection.close();
              } catch (SQLException e) {
                  e.printStackTrace();
              }
          }
        }
        return templateParamMap;
    }

    @Override
    protected Map<String, Object> getParams(BufferedReader bufferedReader) throws IOException {
        Map<String,Object> paramMap = new HashMap<>();
        String line;
        String[] paramKV;
        while ((line = bufferedReader.readLine()) != null){
            if(!StringUtils.isEmpty(line.trim()) && line.contains(PARAM_SPLIT_REGEX)){
                paramKV = line.split(PARAM_SPLIT_REGEX);
                paramMap.put(paramKV[0], paramKV[1]);
            }
        }
        return paramMap;
    }

    protected String getDefaultTemplate() {
        return "mybatis/ModelTemplate,mybatis/RepositoryTemplate-xml,mybatis/RepositoryTemplate-java";
    }

    @Override
    protected String[] getFileSuff(Object fileSuffStr){
        if(fileSuffStr == null || StringUtils.isEmpty((String)fileSuffStr)){
            return new String[]{".java", "Repository.xml", "Repository.java"};
        } else {
            return ((String) fileSuffStr).split(SPLIT_COM);
        }
    }

    @Override
    protected String getDefaultOutPath(Map<String, Object> paramMap) {
        paramMap.put(FILE_NAME_KEY, paramMap.get("modelName"));
        return super.getDefaultOutPath(paramMap);
    }

    public static void main(String[] args){
        Builder builder = new MybatisBuilder();
        builder.build(args);
    }
}
