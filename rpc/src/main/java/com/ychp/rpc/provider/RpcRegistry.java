package com.ychp.rpc.provider;

import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 17/2/13
 */
@Slf4j
public abstract class RpcRegistry {

    @PostConstruct
    public void init(){
        try {
            serviceRegister();
        } catch (Exception e) {
            log.error("service registry fail, case {}", Throwables.getStackTraceAsString(e));
        }
    }

    public abstract void serviceRegister() throws Exception;

}
