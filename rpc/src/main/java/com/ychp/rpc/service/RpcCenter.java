package com.ychp.rpc.service;

import javax.annotation.PostConstruct;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 17/2/13
 */
public abstract class RpcCenter {

    @PostConstruct
    public void init(){
        serviceRegister();
        serviceConsumer();
    }

    public abstract void serviceRegister();

    public abstract void serviceConsumer();
}
