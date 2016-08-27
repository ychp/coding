package com.ychp.coding.common.enums;

import com.google.common.base.Objects;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 16/8/27
 */
public enum IPAPIType {
    TAOBAO(1,"taobao"),
    BAIDU(2,"baidu"),
    SINA(3,"sina");

    private int value;

    private String desc;

    private IPAPIType(int vale, String desc){
        this.value = vale;
        this.desc = desc;
    }

    public IPAPIType fromValue(int value){
        for(IPAPIType item : IPAPIType.values()){
            if(Objects.equal(item.value(), value)){
                return item;
            }
        }
        return null;
    }

    public int value(){
        return this.value;
    }

    @Override
    public String toString(){
        return desc;
    }

}
