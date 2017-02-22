package com.ychp.rpc.consul.provider;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.ProviderConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.spring.ServiceBean;
import com.orbitz.consul.AgentClient;
import com.orbitz.consul.Consul;
import com.orbitz.consul.model.agent.ImmutableRegCheck;
import com.orbitz.consul.model.agent.ImmutableRegistration;
import com.ychp.rpc.consul.properties.ConsulProperties;
import com.ychp.rpc.dubbo.properties.DubboProperties;
import com.ychp.rpc.dubbo.provider.DubboRpcRegistry;
import com.ychp.rpc.utils.RpcUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 17/2/13
 */
@Slf4j
public class ConsulRpcRegistry extends DubboRpcRegistry {

    @Autowired
    private ApplicationContext applicationContext;

    private final Consul consul;

    private final ConsulProperties consulProperties;

    @Autowired
    public ConsulRpcRegistry(Consul consul, ConsulProperties consulProperties, ApplicationContext applicationContext, DubboProperties dubboProperties, ProtocolConfig protocolConfig, RegistryConfig registryConfig, ApplicationConfig applicationConfig, ProviderConfig providerConfig){
        super(applicationContext, dubboProperties, protocolConfig, registryConfig, applicationConfig, providerConfig);
        this.consul = consul;
        this.consulProperties = consulProperties;
    }

    @Override
    public void serviceRegister() throws Exception {
        log.info("============> service register");
        AgentClient agent = consul.agentClient();

        //健康检测
        Integer interval = consulProperties.getInterval();
        ImmutableRegCheck check;

        String host = consulProperties.getHost();
        Integer port = consulProperties.getPort();

        Map<String,Object> serviceMap = applicationContext.getBeansWithAnnotation(com.ychp.rpc.annotation.RpcProvider.class);

        ImmutableRegistration.Builder builder;
        String className;
        Object service;
        ServiceBean providerBean;
        for(String name : serviceMap.keySet()){
            service = serviceMap.get(name);
            providerBean = publishRpcService(name, service);
            builder = ImmutableRegistration.builder();
            className = providerBean.getPath();
            String health = providerBean.getProtocol().getNetworker();
            check = ImmutableRegCheck.builder().http(health).interval(interval + "s").build();
            builder.id(className)
                    .name(className.substring(0, 1).toLowerCase() + className.substring(1))
                    .addTags(RpcUtils.firstNonNull(service.getClass().getAnnotation(com.ychp.rpc.annotation.RpcProvider.class).version(), providerBean.getVersion()))
                    .address(host).port(port).addChecks(check);
            agent.register(builder.build());
            log.info("===> register service {}", className);
        }

        log.info("============> service register end");
    }

}
