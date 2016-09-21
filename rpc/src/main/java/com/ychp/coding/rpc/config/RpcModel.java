package com.ychp.coding.rpc.config;

import lombok.Getter;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 16/9/21
 */
public enum RpcModel {
    DUBBO("dubbo", "dubbo"),
    MOTAN("motan", "motan");

    @Getter
    private String value;

    private String desc;

    private RpcModel(String value, String desc){
        this.value = value;
        this.desc = desc;
    }

    @Override
    public String toString() {
        return this.desc;
    }
}
