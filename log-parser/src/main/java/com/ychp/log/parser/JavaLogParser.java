package com.ychp.log.parser;

import com.google.common.collect.Maps;
import com.ychp.log.model.Java;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 16/12/7
 */
@Slf4j
public class JavaLogParser extends Parser<Java> {

    @Override
    public Java parserLine(String content) {
        Java java = new Java();
        String [] strArr = content.split(" ");

        String time = strArr[0] + " " + strArr[1];
        java.setTimeStr(time);
        java.setLevel(strArr[2]);
        java.setClassFullName(strArr[4]);
        java.setClassLine(strArr[5].substring(1, strArr[5].length() - 1));
        java.setMsg(strArr[6]);
        return java;
    }

    @Override
    public void summary() {
        log.info("summary start");
        summaryDatas = Maps.newHashMap();
        Map<String, Long> levelCount = Maps.newTreeMap();
        Map<String, Long> classLineCount = Maps.newTreeMap();
        for(Java java : datas){
            levelCount.put(java.getLevel(), levelCount.get(java.getLevel()) == null ? 1 : levelCount.get(java.getLevel()) + 1);
            classLineCount.put(java.getClassLine(), classLineCount.get(java.getClassLine()) == null ? 1 : classLineCount.get(java.getClassLine()) + 1);
        }
        summaryDatas.put("level", levelCount);
        summaryDatas.put("classLine", classLineCount);
        log.info("summary end");
    }

    @Override
    public void printAll() {
        log.info("print start");
        printOne("/Users/yingchengpeng/java/levelCount.txt", "level");
        printOne("/Users/yingchengpeng/java/classLineCount.txt", "classLine");
        log.info("print end");
    }

    public static void main(String[] args){
        Parser parser = new JavaLogParser();
        parser.setFile("/Users/yingchengpeng/schedule_error_2016-12-07.log");
        parser.parserAll();
        parser.summary();
        parser.printAll();
    }
}
