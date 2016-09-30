package com.ychp.log.parser;

import com.google.common.collect.Lists;
import lombok.Getter;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 16/9/30
 */
public abstract class Parser<T> {

    protected BufferedReader reader;

    protected BufferedWriter writer;

    @Getter
    protected List<T> datas;

    @Getter
    protected Map<String,Object> summaryDatas;

    protected Map<String,String> uaWithIp;

    protected List<String> blackIp;

    protected List<String> tool = Lists.newArrayList("scrapy","curl","httpclient","wget");

    protected void setFile(String path){
        File file = new File(path);
        try {
            Reader rd = new FileReader(file);
            reader = new BufferedReader(rd);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected void initOutputStream(String path){
        File file = new File(path);
        try {
            Writer wt = new FileWriter(file, false);
            writer = new BufferedWriter(wt);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public abstract void parserAll(String path);

    public abstract T parserLine(String content);

    public abstract void summary();

    public abstract void printAll();

    public abstract void printOne(String path, String key);

    public abstract void printIpNotAllow(String path);

    public abstract void printUa(String path);

}
