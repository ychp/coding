package com.ychp.code.builder;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.Map;

import static com.ychp.code.builder.utils.BuilderUtils.*;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 17/2/9
 */
public abstract class Builder {

    public void build(String ... args){
        if(args.length == 0){
            throw new RuntimeException("run args not empty");
        }

        Map<String, String> argsMap = getArgsMap(args);

        BufferedReader paramReader = null;
        try {
            paramReader = getParamReader(argsMap.get(PARAM_PATH_KEY));
            Map<String, Object> paramMap = getParams(paramReader);
            String templatePath = paramMap.get(TEMPLATE_PATH_KEY) != null ? (String)paramMap.get(TEMPLATE_PATH_KEY) : null;
            paramMap.remove(TEMPLATE_PATH_KEY);
            String[] templates = getTemplatePath(templatePath);
            String[] fileSuff = getFileSuff(paramMap.get(FILE_SUFF_KEY));
            if(fileSuff.length > templates.length){
                throw new RuntimeException("file suff cannot more than template");
            }
            String content;
            String outPath = paramMap.get(OUT_PATH_KEY) != null ? (String) paramMap.get(OUT_PATH_KEY) : getDefaultOutPath();
            for(int i = 0; i < templates.length; i++){
                if(StringUtils.isEmpty(templates[i])){
                    continue;
                }
                content = buildFile(templates[i], paramMap);
                if (fileSuff.length == templates.length){
                    writeToLocal(outPath, fileSuff[i], content);
                } else  if (fileSuff.length == 1){
                    writeToLocal(outPath, fileSuff[1], content);
                } else  if (fileSuff.length == 0){
                    writeToLocal(outPath, null, content);
                }
            }

        } catch (IOException e){
            e.printStackTrace();
        }finally {
            if(paramReader != null){
                try {
                    paramReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    protected String getDefaultOutPath(){
        String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        int lastIndex = path.lastIndexOf(File.separator) + 1;
        return path.substring(0, lastIndex) + DEFAULT_OUT_FILE_NAME;
    }

    protected String[] getFileSuff(Object fileSuffStr){
        if(fileSuffStr == null || StringUtils.isEmpty((String)fileSuffStr)){
            return new String[0];
        } else {
            return ((String) fileSuffStr).split(SPLIT_COM);
        }
    }

    protected void writeToLocal(String outPath, String fileSuf, String content){
        BufferedWriter bufferedWriter = null;
        try{
            String fileName = outPath + (StringUtils.isEmpty(fileSuf) ? "" : fileSuf);
            File file = new File(fileName);
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            bufferedWriter.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(bufferedWriter != null){
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected abstract String buildFile(String templatePath, Map<String, Object> paramMap) throws IOException;

    private String[] getTemplatePath(String templatePath) throws FileNotFoundException {
        if(templatePath == null){
            return getDefaultTemplate().trim().split(SPLIT_COM);
        } else {
            return templatePath.trim().split(SPLIT_COM);
        }
    }

    /**
     * 获取运行变量
     * @param args main函数变量
     * @return 变量map
     */
    private Map<String,String> getArgsMap(String[] args) {
        Map<String,String> argsMap = Maps.newHashMap();
        for(int i=0; i < args.length; ) {
            if(args[i].startsWith("--")) {
                argsMap.put(args[i].substring(2), args[i + 1]);
                i+=2;
            }
        }
        return argsMap;
    }

    /**
     * 获取参数文件流
     * @param paramPath 参数文件路径
     * @return 文件流
     * @throws FileNotFoundException 文件不存在异常
     */
    private BufferedReader getParamReader(String paramPath) throws FileNotFoundException {
        BufferedReader bufferedReader;

        File paramFile = new File(paramPath);

        if(!paramFile.exists()){
            throw new RuntimeException("param file not exist!");
        }

        bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(paramFile)));

        return bufferedReader;
    }

    /**
     * 解析参数文件
     * @param bufferedReader 参数文件流
     * @return 参数map
     * @throws IOException 文件异常
     */
    protected Map<String,Object> getParams(BufferedReader bufferedReader) throws IOException {
        Map<String,Object> paramMap = Maps.newHashMap();
        String line;
        String[] paramKV;
        String key;
        String value;
        while ((line = bufferedReader.readLine()) != null){
            if(!StringUtils.isEmpty(line.trim()) && line.contains(PARAM_SPLIT_REGEX)){
                paramKV = line.split(PARAM_SPLIT_REGEX);
                key = paramKV[0];
                value = paramKV[1];
                paramMap.put(key, value);
                if(value.contains(SPLIT_COM)){
                    paramMap.put(key + "s", value.split(SPLIT_COM));
                }
            }
        }

        return paramMap;
    }

    protected abstract String getDefaultTemplate();

}
