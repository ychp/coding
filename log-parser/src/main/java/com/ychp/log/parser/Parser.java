package com.ychp.log.parser;

import com.google.common.collect.Lists;
import lombok.Getter;

import java.io.*;
import java.util.*;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 16/9/30
 */
public abstract class Parser<T> {

    private BufferedReader reader;

    @Getter
    private BufferedWriter writer;

    @Getter
    protected List<T> datas;

    @Getter
    protected Map<String,Map<String,Long>> summaryDatas;

    Map<String,String> uaWithIp;

    List<String> blackIp;

    void setFile(String path){
        File file = new File(path);
        try {
            Reader rd = new FileReader(file);
            reader = new BufferedReader(rd);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    void initOutputStream(String path){
        File file = new File(path);
        try {
            Writer wt = new FileWriter(file, false);
            writer = new BufferedWriter(wt);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void parserAll(){
        String str;
        datas = Lists.newArrayList();
        try {
            while ((str = reader.readLine()) != null){
                datas.add(parserLine(str));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public abstract T parserLine(String content);

    public abstract void summary();

    public abstract void printAll();

    public void printOne(String path, String key){
        initOutputStream(path);
        String line;
        Map<String,Long> dataMap = (Map<String,Long>)summaryDatas.get(key);
        List<Map.Entry<String,Long>> list = new ArrayList<Map.Entry<String,Long>>(dataMap.entrySet());
        //然后通过比较器来实现排序
        Collections.sort(list,new Comparator<Map.Entry<String,Long>>() {
            //升序排序
            public int compare(Map.Entry<String, Long> o1,
                               Map.Entry<String, Long> o2) {
                return -o1.getValue().compareTo(o2.getValue());
            }

        });

        try {
            for(Map.Entry<String,Long> entry:list){
                line = entry.getKey() + "=" + entry.getValue();
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void printIpNotAllow(String path){}

    public void printUa(String path){}

}
