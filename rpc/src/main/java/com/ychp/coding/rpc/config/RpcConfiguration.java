package com.ychp.coding.rpc.config;

import com.ychp.coding.rpc.dubbo.DubboConfiguration;
import com.ychp.coding.rpc.exception.RpcException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Objects;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 16/9/21
 */
@Slf4j
@Configuration
public class RpcConfiguration {

    @Autowired
    private RpcProperties rpcProperties;

    @PostConstruct
    public void initRpc(){
        if((Objects.equals(RpcModel.DUBBO.getValue(), rpcProperties.getMode())) || (Objects.equals(RpcModel.MOTAN.getValue(), rpcProperties.getMode()))){
            log.info(" start rpc [" + rpcProperties.getMode() + "]");
        } else {
          throw new RpcException("rpc model cannot empty");
        }
    }

}
