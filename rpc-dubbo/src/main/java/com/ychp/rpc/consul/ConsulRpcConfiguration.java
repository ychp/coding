package com.ychp.rpc.consul;

import com.google.common.net.HostAndPort;
import com.orbitz.consul.Consul;
import com.ychp.rpc.consul.properties.ConsulProperties;
import com.ychp.rpc.consul.provider.ConsulRpcRegistry;
import com.ychp.rpc.dubbo.properties.DubboProperties;
import com.ychp.rpc.provider.RpcRegistry;
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
@EnableConfigurationProperties({ConsulProperties.class, DubboProperties.class})
public class ConsulRpcConfiguration {

    @Autowired
    private ConsulProperties properties;

    @Bean
    public Consul consul(){
        return Consul.builder().withHostAndPort(HostAndPort.fromString(properties.getAddress())).build();
    }
    @Bean
    public RpcRegistry rpcCenter(Consul consul){
        return new ConsulRpcRegistry(consul, properties.getHost(), properties.getPort(), properties.getVersion(), properties.getHealth(), properties.getInterval());
    }
}
