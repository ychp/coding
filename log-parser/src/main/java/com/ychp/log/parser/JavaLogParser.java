package com.ychp.log.parser;

import com.google.common.collect.Maps;
import com.ychp.log.model.Java;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 16/12/7
 */
public class JavaLogParser extends Parser<Java> {

    @Override
    public Java parserLine(String content) {
        Java java = new Java();
        content = content.trim();
        if(StringUtils.isEmpty(content) || content.startsWith("at")){
            return null;
        }

        String [] strArr = content.split(" ");

        if(strArr.length < 6){
//            System.out.println("error struct content:" + content);
            return null;
        }

        String time = strArr[0] + " " + strArr[1];
        java.setTimeStr(time);
        java.setLevel(strArr[2]);
        java.setClassFullName(strArr[4]);
        java.setClassLine(strArr[5]);
        java.setMsg(content.substring(content.indexOf(strArr[5]) + strArr[5].length()));
        return java;
    }

    @Override
    public void summary() {
        System.out.println("summary start");
        summaryDatas = Maps.newHashMap();
        Map<String, Long> levelCount = Maps.newTreeMap();
        Map<String, Long> classLineCount = Maps.newTreeMap();
        for(Java java : datas){
            levelCount.put(java.getLevel(), levelCount.get(java.getLevel()) == null ? 1 : levelCount.get(java.getLevel()) + 1);
            classLineCount.put(java.getClassLine(), classLineCount.get(java.getClassLine()) == null ? 1 : classLineCount.get(java.getClassLine()) + 1);
        }
        summaryDatas.put("level", levelCount);
        summaryDatas.put("classLine", classLineCount);
        System.out.println("summary end");
    }

    @Override
    public void printAll() {
        System.out.println("print start");
        printOne("/Users/yingchengpeng/java/levelCount.txt", "level");
        printOne("/Users/yingchengpeng/java/classLineCount.txt", "classLine");
        System.out.println("print end");
    }

    public static void main(String[] args){
        Parser parser = new JavaLogParser();
        parser.setFile("/Users/yingchengpeng/schedule-2016-12-06.log");
        parser.parserAll();
        parser.summary();
        parser.printAll();
    }
}
