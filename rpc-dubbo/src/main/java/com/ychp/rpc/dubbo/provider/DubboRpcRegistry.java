package com.ychp.rpc.dubbo.provider;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.ProviderConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.spring.ServiceBean;
import com.ychp.rpc.annotation.RpcProvider;
import com.ychp.rpc.dubbo.properties.DubboProperties;
import com.ychp.rpc.provider.RpcRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.SpringProxy;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 17/2/21
 */
@Slf4j
public class DubboRpcRegistry extends RpcRegistry {

    private final ApplicationContext applicationContext;

    private final DubboProperties dubboProperties;

    private final ProtocolConfig protocolConfig;

    private final RegistryConfig registryConfig;

    private final ApplicationConfig applicationConfig;

    private final ProviderConfig providerConfig;

    @Autowired
    public DubboRpcRegistry(ApplicationContext applicationContext, DubboProperties dubboProperties, ProtocolConfig protocolConfig, RegistryConfig registryConfig, ApplicationConfig applicationConfig, ProviderConfig providerConfig) {
        this.applicationContext = applicationContext;
        this.dubboProperties = dubboProperties;
        this.protocolConfig = protocolConfig;
        this.registryConfig = registryConfig;
        this.applicationConfig = applicationConfig;
        this.providerConfig = providerConfig;
    }

    @Override
    public void serviceRegister() throws Exception {
        log.info("============> service register");
        Map<String,Object> serviceMap = applicationContext.getBeansWithAnnotation(com.ychp.rpc.annotation.RpcProvider.class);

        String className;
        Object service;
        ServiceBean providerBean;
        for(String name : serviceMap.keySet()){
            service = serviceMap.get(name);
            providerBean = publishRpcService(name, service);
            className = providerBean.getPath();
            log.info("===> register service {}", className);
        }

        log.info("============> service register end");
    }

    protected ServiceBean publishRpcService(String beanName, Object bean) throws Exception {
        RpcProvider rpcProvider = applicationContext.findAnnotationOnBean(beanName, RpcProvider.class);
        ServiceBean providerBean = new ServiceBean();
        Class interfaceType;
        if(rpcProvider.interfaceClass() != Void.TYPE) {
            interfaceType = rpcProvider.interfaceClass();
        } else if(StringUtils.hasText(rpcProvider.interfaceName())) {
            interfaceType = Class.forName(rpcProvider.interfaceName());
        } else {
            Class targetClass = realTargetClass(bean);
            if(targetClass.getInterfaces().length == 0) {
                throw new IllegalStateException("Failed to export remote rpcProvider class " + bean.getClass().getName() + ", cause: The @RpcService undefined interfaceClass or interfaceName," + " and the rpcProvider class unimplemented any interfaces.");
            }

            interfaceType = targetClass.getInterfaces()[0];
        }

        providerBean.setInterface(interfaceType);
        if(StringUtils.hasText(rpcProvider.group())) {
            providerBean.setGroup(rpcProvider.group());
        }

        providerBean.setVersion(StringUtils.hasText(rpcProvider.version())?rpcProvider.version():dubboProperties.getVersion());
        providerBean.setApplicationContext(applicationContext);
        providerBean.setApplication(applicationConfig);
        providerBean.setProtocol(protocolConfig);
        providerBean.setRegistry(registryConfig);
        providerBean.setProvider(providerConfig);
        providerBean.setRef(bean);
        providerBean.afterPropertiesSet();
        providerBean.export();

        return providerBean;
    }

    private Class<?> realTargetClass(Object bean) {
        return bean instanceof SpringProxy?AopUtils.getTargetClass(bean):bean.getClass();
    }

}
