package com.ychp.spider.parser;

import com.ychp.spider.model.SpiderData;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 16/10/12
 */
@Component
public class DefaultParser extends Parser<SpiderData> {


    public void printDatas(List<SpiderData> datas){
        for(SpiderData data : datas) {
            System.out.println(data);
        }
    }

    @Override
    protected void initConfigPrex() {
        setConfigPrex("default");
    }

    public static void main(String[] args){
        DefaultParser parser = new DefaultParser();
        List<SpiderData> datas = parser.parseContext();
        parser.printDatas(datas);
        System.out.println();
    }
}
