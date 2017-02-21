package com.ychp.rpc.consul.consumer;

import com.google.common.collect.Maps;
import com.ychp.rpc.annotation.RpcConsumer;
import com.ychp.rpc.consul.properties.ConsulProperties;
import com.ychp.rpc.consumer.resolver.RpcConsumerResolver;
import com.ychp.rpc.utils.RpcUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 17/2/13
 */
@Slf4j
public class ConsulRpcConsumerResolver implements RpcConsumerResolver {
    private Map<String, Object> references = Maps.newHashMap();
    private final ApplicationContext applicationContext;
    private final ConsulProperties properties;

    public ConsulRpcConsumerResolver(ApplicationContext applicationContext, ConsulProperties properties) {
        this.applicationContext = applicationContext;
        this.properties = properties;
    }

    public <T> T resolve(Class<T> interfaceClazz, RpcConsumer rpcConsumer) throws BeansException {
//        ReferenceBean consumerBean = this.getConsumerBean(interfaceClazz, rpcConsumer, this.getConsumerRegistry(interfaceClazz.getCanonicalName(), rpcConsumer, this.registryConfig));
//        String id = interfaceClazz.getCanonicalName() + ":" + RpcUtils.firstNonNull(rpcConsumer.version(), this.registryConfig.getVersion());
//        if(!this.references.containsKey(id)) {
//            try {
//                this.references.put(id, consumerBean.getObject());
//            } catch (Exception var6) {
//                throw new BeanCreationException(interfaceClazz.getCanonicalName(), var6);
//            }
//        }
//
//        return this.references.get(id);
        return (T) references.get("");
    }

//    private RegistryConfig getConsumerRegistry(String beanName, RpcConsumer rpcConsumer, RegistryConfig registryConfig) {
//        if(rpcConsumer.specifiedRegistry()) {
//            RegistryProperties properties = getProperties(rpcConsumer.registryKey(), this.dubboProperties.getRegistryMap());
//            if(properties == null) {
//                log.error("RpcConsumer:({}) specifiedRegistry is enabled, but RegistryProperties not found, key:{}, properties:{}", new Object[]{beanName, rpcConsumer.registryKey(), this.dubboProperties.getRegistries()});
//                throw new IllegalArgumentException("consumer.registry.not.found");
//            } else {
//                RegistryConfig newConfig = new RegistryConfig();
//                newConfig.setAddress(properties.getAddress());
//                newConfig.setProtocol(properties.getProtocol());
//                newConfig.setTimeout(registryConfig.getTimeout());
//                if(StringUtils.hasText(properties.getVersion())) {
//                    newConfig.setVersion(properties.getVersion());
//                }
//
//                newConfig.setFile(registryConfig.getFile());
//                return newConfig;
//            }
//        } else {
//            return registryConfig;
//        }
//    }
//
//    private static RegistryProperties getProperties(String key, Map<String, RegistryProperties> registryMap) {
//        return StringUtils.hasText(key) && registryMap.containsKey(key)?(RegistryProperties)registryMap.get(key):null;
//    }
//
}
