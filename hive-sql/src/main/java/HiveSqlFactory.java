import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 17/2/8
 */
public class HiveSqlFactory {

    private static final String FULL_IMPORT_DEFAULT_TEMPLATE_PATH = "full_import.q";

    private static final String INC_IMPORT_DEFAULT_TEMPLATE_PATH = "inc_import.q";

    private static final String PARAM_DEFAULT_TEMPLATE_PATH = "param_template";

    private static final String PARAM_SPLIT_REGEX = ":";

    private static final String COLUMN_SPLIT = "|";

    private static final String COLUMN_SPLIT_REGEX = "\\|";

    public static void main(String[] args) {
        if(args.length == 0){
            throw new RuntimeException("run args not empty");
        }
        String model = args[0];

        BufferedReader bufferedReader = null;
        try {

            bufferedReader = getParamReader(args);

            Map<String, Object> paramMap = getParams(bufferedReader);

            String fullImportTemplatePath = FULL_IMPORT_DEFAULT_TEMPLATE_PATH;
            String incImportTemplatePath = INC_IMPORT_DEFAULT_TEMPLATE_PATH;

            Boolean isDefault = true;
            switch (Integer.valueOf(model)) {
                case 0:
                    //全部模板
                    factoryFull(paramMap, getTemplateStream(fullImportTemplatePath, isDefault));
                    factoryInc(paramMap, getTemplateStream(incImportTemplatePath, isDefault));
                    break;
                case 1:
                    //全量模板
                    factoryFull(paramMap, getTemplateStream(fullImportTemplatePath, isDefault));
                    break;
                case 2:
                    //增量模板
                    factoryInc(paramMap, getTemplateStream(incImportTemplatePath, isDefault));
                    break;
                default:
                    factoryFull(paramMap, getTemplateStream(fullImportTemplatePath, isDefault));
                    factoryInc(paramMap, getTemplateStream(incImportTemplatePath, isDefault));
                    break;
            }
        } catch (FileNotFoundException ffe){

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

    private static BufferedReader getTemplateStream(String fullImportTemplatePath, Boolean isDefault) {
        return null;
    }

    private static void factoryInc(Map<String, Object> paramMap, BufferedReader incImportTemplateReader) {

    }

    private static void factoryFull(Map<String, Object> paramMap, BufferedReader fullImportTemplateReader) {

    }

    private static BufferedReader getParamReader(String[] args) {
        BufferedReader bufferedReader;
//        if(args.length == 1){
//            throw new RuntimeException("param file path not empty!");
//        }
//        String paramsPath = args[1];
//
//        File paramFile = new File(paramsPath);
//
//        if(!paramFile.exists()){
//            throw new RuntimeException("param file not exist!");
//        }
//
//        bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(paramFile)));

        InputStream in = ClassLoader.getSystemResourceAsStream(PARAM_DEFAULT_TEMPLATE_PATH);
        bufferedReader = new BufferedReader(new InputStreamReader(in));
        return bufferedReader;
    }

    private static Map<String,Object> getParams(BufferedReader bufferedReader) throws IOException {
        Map<String,Object> paramMap = new HashMap<String, Object>();
        String line;
        Boolean isColumns = false;
        String[] paramKV;
        String[] columnArr;
        Map<String, String> dataTypeByColumns = new HashMap<String, String>();
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
                dataTypeByColumns.put(columnArr[1].trim(),columnArr[2].trim());
            }

        }

        return paramMap;
    }
}
