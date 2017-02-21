package com.ychp.rpc.provider;

import javax.annotation.PostConstruct;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 17/2/13
 */
public abstract class RpcRegistry {

    @PostConstruct
    public void init(){
        serviceRegister();
    }

    public abstract void serviceRegister() throws Exception;

}
