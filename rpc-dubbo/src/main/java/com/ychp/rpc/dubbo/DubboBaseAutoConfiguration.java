package com.ychp.rpc.dubbo;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.ProviderConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.ychp.rpc.dubbo.properties.DubboProperties;
import com.ychp.rpc.utils.RpcUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 17/2/21
 */
@Configuration
@EnableConfigurationProperties({DubboProperties.class})
public class DubboBaseAutoConfiguration {

    @Autowired
    private DubboProperties dubboProperties;

    @Value("${spring.application.name:}")
    private String appName;

    @Bean
    @ConditionalOnMissingBean(ApplicationConfig.class)
    public ApplicationConfig applicationConfig(){
        ApplicationConfig applicationConfig = new ApplicationConfig();

        if(StringUtils.isNotBlank(dubboProperties.getOwner())){
            applicationConfig.setOwner(dubboProperties.getOwner());
        }

        if(StringUtils.isEmpty(dubboProperties.getAppName()) && StringUtils.isEmpty(appName)) {
            throw new IllegalStateException("AppName can\'t be empty, please make sure that \'rpc.dubbo.appName\' or \'spring.application.name\' have been set");
        } else {
            applicationConfig.setName(RpcUtils.firstNonNull(dubboProperties.getAppName(), appName));
        }

        if(!StringUtils.isEmpty(dubboProperties.getModelVersion())) {
            applicationConfig.setVersion(dubboProperties.getModelVersion());
        }

        if(!StringUtils.isEmpty(dubboProperties.getLoggerType())) {
            applicationConfig.setLogger(dubboProperties.getLoggerType());
        } else {
            applicationConfig.setLogger("slf4j");
        }

        return applicationConfig;
    }

    @Bean
    @ConditionalOnMissingBean(ProtocolConfig.class)
    public ProtocolConfig protocolConfig(){
        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setName("dubbo");
        protocolConfig.setPort(dubboProperties.getPort());
        if(!StringUtils.isEmpty(dubboProperties.getHost())){
            protocolConfig.setHost(dubboProperties.getHost());
        }

        if(dubboProperties.getThreads() != null){
            protocolConfig.setThreads(dubboProperties.getThreads());
        }

        return protocolConfig;
    }

    @Bean
    @ConditionalOnMissingBean(RegistryConfig.class)
    public RegistryConfig registryConfig(){
        RegistryConfig registryConfig = new RegistryConfig();

        if(StringUtils.isEmpty(dubboProperties.getRegistryAddress())) {
            throw new IllegalStateException("registry address can\'t be empty, please make sure that \'rpc.dubbo.registryAddress\' have been set");
        } else {
            registryConfig.setAddress(dubboProperties.getRegistryAddress());
        }

        return registryConfig;
    }

    @Bean
    @ConditionalOnMissingBean(ProviderConfig.class)
    public ProviderConfig providerConfig(ApplicationConfig applicationConfig, ProtocolConfig protocolConfig, RegistryConfig registryConfig){
        ProviderConfig providerConfig = new ProviderConfig();

        providerConfig.setApplication(applicationConfig);
        providerConfig.setProtocol(protocolConfig);
        providerConfig.setRegistry(registryConfig);
        providerConfig.setHost(dubboProperties.getHost());
        providerConfig.setThreads(dubboProperties.getThreads());
        providerConfig.setVersion(dubboProperties.getVersion());
        providerConfig.setTimeout(dubboProperties.getTimeout());
        providerConfig.setSerialization(dubboProperties.getSerialization());
        return providerConfig;
    }





}
