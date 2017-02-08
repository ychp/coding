import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.*;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 17/2/8
 */
public class HiveSqlFactory {

    private static final String FULL_IMPORT_DEFAULT_TEMPLATE_PATH = "full_import.q";

    private static final String INC_IMPORT_DEFAULT_TEMPLATE_PATH = "inc_import.q";

    private static final String DEFAULT_OUT_PATH = ".";

    private static final String PARAM_SPLIT_REGEX = ":";

    private static final String COLUMN_SPLIT = "|";

    private static final String COLUMN_SPLIT_REGEX = "\\|";

//    private static final String PARAM_PREF = "\\$\\{";

//    private static final String PARAM_SUF = "\\}";

    private static final String COLUMN_SUF = ",";

    public static void main(String[] args) {
        if(args.length == 0){
            throw new RuntimeException("run args not empty");
        }

        Map<String, String> argsMap = getArgsMap(args);

        String model = argsMap.get("model");

        BufferedReader bufferedReader = null;
        try {

            bufferedReader = getParamReader(argsMap.get("paramPath"));

            Map<String, Object> paramMap = getParams(bufferedReader);

            String fullImportTemplatePath = FULL_IMPORT_DEFAULT_TEMPLATE_PATH;
            String incImportTemplatePath = INC_IMPORT_DEFAULT_TEMPLATE_PATH;

            Boolean isDefaultFull = true;

            Boolean isDefaultInc = true;

            if(!StringUtils.isEmpty(argsMap.get("full_template"))){
                isDefaultFull = false;
                fullImportTemplatePath = argsMap.get("full_template");
            }

            if(!StringUtils.isEmpty(argsMap.get("inc_template"))){
                isDefaultInc = false;
                incImportTemplatePath = argsMap.get("inc_template");
            }

            String outPath = DEFAULT_OUT_PATH;

            if(!StringUtils.isEmpty(argsMap.get("outPath"))){
                outPath = argsMap.get("outPath");
            }
            switch (Integer.valueOf(model)) {
                case 0:
                    //全部模板
                    factoryFull(paramMap, getTemplateStream(fullImportTemplatePath, isDefaultFull), outPath);
                    factoryInc(paramMap, getTemplateStream(incImportTemplatePath, isDefaultInc), outPath);
                    break;
                case 1:
                    //全量模板
                    factoryFull(paramMap, getTemplateStream(fullImportTemplatePath, isDefaultFull), outPath);
                    break;
                case 2:
                    //增量模板
                    factoryInc(paramMap, getTemplateStream(incImportTemplatePath, isDefaultInc), outPath);
                    break;
                default:
                    factoryFull(paramMap, getTemplateStream(fullImportTemplatePath, isDefaultFull), outPath);
                    factoryInc(paramMap, getTemplateStream(incImportTemplatePath, isDefaultInc), outPath);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(bufferedReader != null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static Map<String,String> getArgsMap(String[] args) {
        Map<String,String> argsMap = new HashMap<String, String>();
        for(int i=0; i < args.length; ) {
            if(args[i].startsWith("--")) {
                argsMap.put(args[i].substring(2), args[i + 1]);
                i+=2;
            }
        }
        return argsMap;
    }

    private static BufferedReader getTemplateStream(String templatePath, Boolean isDefault) throws FileNotFoundException {
        BufferedReader bufferedReader;
        if(isDefault) {
            InputStream in = ClassLoader.getSystemResourceAsStream(templatePath);
            bufferedReader = new BufferedReader(new InputStreamReader(in));
        } else {
            File templateFile = new File(templatePath);

            if(!templateFile.exists()){
                throw new RuntimeException("template file not exist!");
            }

            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(templateFile)));
        }

        return bufferedReader;
    }

    private static void factoryInc(Map<String, Object> paramMap, BufferedReader incImportTemplateReader, String outPath) {
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outPath + File.separator + "inc_" + paramMap.get("table_name") +".q"))));
            String line;
            Map<String,String> dataTypeByColumns = (Map<String,String>)paramMap.get("dataTypeByColumns");
            List<String> columns = new ArrayList<String>();
            List<String> columnsWithDataType = new ArrayList<String>();

            for(String column : dataTypeByColumns.keySet()){
                columns.add(column.trim() + COLUMN_SUF);
                columnsWithDataType.add(column + trimRight(dataTypeByColumns.get(column)) + COLUMN_SUF);
            }

            while ((line = incImportTemplateReader.readLine()) != null) {
                if(line.contains("${table_name}")){
                    line = line.replace("${table_name}", (String)paramMap.get("table_name"));
                }
                if(line.contains("${database}")){
                    line = line.replace("${database}", (String)paramMap.get("database"));
                }
                if(line.contains("${inc_column}")){
                    line = line.replace("${inc_column}", (String)paramMap.get("inc_column"));
                }

                if(line.contains("${left_join_columns}")){
                    line = line.replace("${left_join_columns}", (String)paramMap.get("left_join_columns"));
                }

                if(line.contains("${table_columns}")){
                    String tmpLine;
                    for(String columnWithDataType : columnsWithDataType) {
                        tmpLine = line.replace("${table_columns}", trimLeft(columnWithDataType));
                        if(columnsWithDataType.indexOf(columnWithDataType) == columnsWithDataType.size() - 1){
                            tmpLine = tmpLine.replace(COLUMN_SUF, "");
                        }
                        bufferedWriter.write(tmpLine);
                        bufferedWriter.newLine();
                    }
                    bufferedWriter.flush();
                    continue;
                }

                if(line.contains("${columns}")){
                    String tmpLine;
                    for(String column : columns) {
                        tmpLine = line.replace("${columns}", column);
                        if(columns.indexOf(column) == columns.size() - 1){
                            tmpLine = tmpLine.replace(COLUMN_SUF, "");
                        }
                        bufferedWriter.write(tmpLine);
                        bufferedWriter.newLine();
                    }
                    bufferedWriter.flush();
                    continue;
                }

                if(line.contains("${columnsWithSuf}")){
                    String tmpLine;
                    for(String column : columns) {
                        tmpLine = line.replace("${columnsWithSuf}", column);
                        bufferedWriter.write(tmpLine);
                        bufferedWriter.newLine();
                    }
                    bufferedWriter.flush();
                    continue;
                }

                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
        } catch (IOException ie){
            ie.printStackTrace();
        } finally {
            if(incImportTemplateReader != null){
                try {
                    incImportTemplateReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(bufferedWriter != null){
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void factoryFull(Map<String, Object> paramMap, BufferedReader fullImportTemplateReader, String outPath) {
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outPath + File.separator + "full_" + paramMap.get("table_name") +".q"))));
            String line;
            Map<String,String> dataTypeByColumns = (Map<String,String>)paramMap.get("dataTypeByColumns");
            List<String> columns = new ArrayList<String>();
            List<String> columnsWithDataType = new ArrayList<String>();

            for(String column : dataTypeByColumns.keySet()){
                columns.add(column.trim() + COLUMN_SUF);
                columnsWithDataType.add(column + trimRight(dataTypeByColumns.get(column)) + COLUMN_SUF);
            }

            while ((line = fullImportTemplateReader.readLine()) != null) {
//                if(line.matches(PARAM_PREF + "[a-zA-Z]+[_a-zA-z]*" + PARAM_SUF)){
                if(line.contains("${table_name}")){
                    line = line.replace("${table_name}", (String)paramMap.get("table_name"));
                }
                if(line.contains("${database}")){
                    line = line.replace("${database}", (String)paramMap.get("database"));
                }

                if(line.contains("${table_columns}")){
                    String tmpLine;
                    for(String columnWithDataType : columnsWithDataType) {
                        tmpLine = line.replace("${table_columns}", trimLeft(columnWithDataType));
                        if(columnsWithDataType.indexOf(columnWithDataType) == columnsWithDataType.size() - 1){
                            tmpLine = tmpLine.replace(COLUMN_SUF, "");
                        }
                        bufferedWriter.write(tmpLine);
                        bufferedWriter.newLine();
                    }
                    bufferedWriter.flush();
                    continue;
                }

                if(line.contains("${columns}")){
                    String tmpLine;
                    for(String column : columns) {
                        tmpLine = line.replace("${columns}", column);
                        if(columns.indexOf(column) == columns.size() - 1){
                            tmpLine = tmpLine.replace(COLUMN_SUF, "");
                        }
                        bufferedWriter.write(tmpLine);
                        bufferedWriter.newLine();
                    }
                    bufferedWriter.flush();
                    continue;
                }

                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
        } catch (IOException ie){
            ie.printStackTrace();
        } finally {
            if(fullImportTemplateReader != null){
                try {
                    fullImportTemplateReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(bufferedWriter != null){
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static BufferedReader getParamReader(String paramPath) throws FileNotFoundException {
        BufferedReader bufferedReader;

        File paramFile = new File(paramPath);

        if(!paramFile.exists()){
            throw new RuntimeException("param file not exist!");
        }

        bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(paramFile)));

        return bufferedReader;
    }

    private static Map<String,Object> getParams(BufferedReader bufferedReader) throws IOException {
        Map<String,Object> paramMap = new HashMap<String, Object>();
        String line;
        Boolean isColumns = false;
        String[] paramKV;
        String[] columnArr;
        Map<String, String> dataTypeByColumns = new LinkedHashMap<String, String>();
        while ((line = bufferedReader.readLine()) != null){
            if(line.contains("columns start")) {
                isColumns = true;
                continue;
            } else if(line.contains("columns end")) {
                isColumns = false;
                paramMap.put("dataTypeByColumns", dataTypeByColumns);
                continue;
            }

            if(!isColumns){
                if(!StringUtils.isEmpty(line.trim()) && line.contains(PARAM_SPLIT_REGEX)){
                    paramKV = line.split(PARAM_SPLIT_REGEX);
                    paramMap.put(paramKV[0], paramKV[1]);
                }
            } else {
                if(StringUtils.isEmpty(line.trim()) || !line.contains(COLUMN_SPLIT)) {
                    continue;
                }
                columnArr = line.trim().split(COLUMN_SPLIT_REGEX);
                dataTypeByColumns.put(columnArr[1],columnArr[2]);
            }

        }

        return paramMap;
    }

    private static String trimRight(String source){
        String sResult;

        if (source.startsWith(" ")){
            sResult = source.substring(0,source.indexOf(source.trim().substring(0, 1)) + source.trim().length());
        } else {
            sResult = source.trim();
        }

        return sResult;
    }

    private static String trimLeft(String source){
        String sResult;

        if (source.startsWith(" ")){
            sResult = source.substring(source.indexOf(source.trim().substring(0, 1)));
        } else {
            sResult = source.trim();
        }

        return sResult;
    }
}
