package com.ychp.rpc.consul;

import com.google.common.net.HostAndPort;
import com.orbitz.consul.Consul;
import com.ychp.rpc.consul.properties.ConsulProperties;
import com.ychp.rpc.consul.service.ConsulRpcCenter;
import com.ychp.rpc.service.RpcCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 17/2/13
 */
@Configuration
@EnableConfigurationProperties({ConsulProperties.class})
public class ConsulRpcConfiguration {

    @Autowired
    private ConsulProperties properties;

    @Bean
    public Consul consul(){
        return Consul.builder().withHostAndPort(HostAndPort.fromString(properties.getAddress())).build();
    }
    @Bean
    public RpcCenter rpcCenter(Consul consul){
        return new ConsulRpcCenter(consul, properties.getHost(), properties.getPort());
    }
}
