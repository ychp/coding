package com.ychp.spider.parser;

import com.ychp.spider.model.BaseData;

import java.util.List;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 16/10/12
 */
public class DefaultParser extends BaseParser<BaseData> {

    public void printDatas(List<BaseData> datas){
        for(BaseData data : datas) {
            System.out.println(data);
        }
    }

    @Override
    protected void initConfigPrex() {
        setConfigPrex("default");
    }

    public static void main(String[] args){
        DefaultParser parser = new DefaultParser();
        List<BaseData> datas = parser.parseContext();
        parser.printDatas(datas);
        System.out.println();
    }
}
