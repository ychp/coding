package com.ychp.spider.enums;

import lombok.Data;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 16/10/12
 */
public enum ScanType {
    VIDEO("video","视频"),
    IMAGE("image","图片"),
    TEXT("text","文本"),
    TAG("tag","标签");

    private String value;

    private String desc;

    private ScanType(String value, String desc){
        this.value = value;
        this.desc = desc;
    }

    public String getValue(){
        return this.value;
    }

    @Override
    public String toString(){
        return this.desc;
    }

}
