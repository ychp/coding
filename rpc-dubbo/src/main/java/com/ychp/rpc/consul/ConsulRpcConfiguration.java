package com.ychp.rpc.consul;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.ProviderConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.google.common.net.HostAndPort;
import com.orbitz.consul.Consul;
import com.ychp.rpc.consul.properties.ConsulProperties;
import com.ychp.rpc.consul.provider.ConsulRpcRegistry;
import com.ychp.rpc.dubbo.DubboBaseAutoConfiguration;
import com.ychp.rpc.dubbo.properties.DubboProperties;
import com.ychp.rpc.provider.RpcRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 17/2/13
 */
@Configuration
@ConditionalOnProperty(value = {"rpc.mode"}, havingValue = "true")
@Import(DubboBaseAutoConfiguration.class)
@EnableConfigurationProperties({ConsulProperties.class})
public class ConsulRpcConfiguration {

    @Autowired
    private ConsulProperties properties;

    @Bean
    public Consul consul(){
        return Consul.builder().withHostAndPort(HostAndPort.fromString(properties.getAddress())).build();
    }
    @Bean
    public RpcRegistry rpcRegistry(Consul consul, ApplicationContext applicationContext, DubboProperties dubboProperties, ProtocolConfig protocolConfig, RegistryConfig registryConfig, ApplicationConfig applicationConfig, ProviderConfig providerConfig){
        return new ConsulRpcRegistry(consul, properties, applicationContext, dubboProperties, protocolConfig, registryConfig, applicationConfig, providerConfig);
    }
}
