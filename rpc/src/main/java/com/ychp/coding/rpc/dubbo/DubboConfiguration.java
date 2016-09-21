package com.ychp.coding.rpc.dubbo;

import com.alibaba.dubbo.config.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 16/9/21
 */
@Configuration
@ConditionalOnProperty(
        value = {"rpc.mode"},
        havingValue = "dubbo"
)
public class DubboConfiguration {

    @Autowired
    private DubboProperties dubboProperties;

    @Bean
    @ConditionalOnMissingBean
    public ApplicationConfig applicationConfig(){
        return new ApplicationConfig(dubboProperties.getAppName());
    }

    @Bean
    @ConditionalOnMissingBean
    public RegistryConfig registryConfig(){
        RegistryConfig registryConfig = new RegistryConfig(dubboProperties.getAddress());
        registryConfig.setProtocol(dubboProperties.getProtocol());
        return registryConfig;
    }

    @Bean
    @ConditionalOnMissingBean
    public ProtocolConfig protocolConfig(){
        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setSerialization(dubboProperties.getSerialization());
        protocolConfig.setName("dubbo");
        protocolConfig.setHost(dubboProperties.getHost());
        protocolConfig.setPort(dubboProperties.getPort());
        protocolConfig.setThreads(dubboProperties.getThreads());
        protocolConfig.setHeartbeat(dubboProperties.getHeartbeat());
        return protocolConfig;
    }

    @Bean
    @ConditionalOnMissingBean
    public ProviderConfig providerConfig(RegistryConfig registryConfig, ProtocolConfig protocolConfig, ApplicationConfig applicationConfig){
        ProviderConfig providerConfig = new ProviderConfig();
        providerConfig.setSerialization(dubboProperties.getSerialization());
        providerConfig.setThreads(dubboProperties.getThreads());
        providerConfig.setRegistry(registryConfig);
        providerConfig.setProtocol(protocolConfig);
        providerConfig.setApplication(applicationConfig);
        providerConfig.setRetries(dubboProperties.getRetries());
        providerConfig.setTimeout(dubboProperties.getTimeout());
        return providerConfig;
    }

    @Bean
    @ConditionalOnMissingBean
    public ConsumerConfig consumerConfig() {
        ConsumerConfig config = new ConsumerConfig();
        config.setCheck(this.dubboProperties.getCheck());
        config.setTimeout(this.dubboProperties.getTimeout());
        config.setVersion(this.dubboProperties.getVersion());
        return config;
    }

}
