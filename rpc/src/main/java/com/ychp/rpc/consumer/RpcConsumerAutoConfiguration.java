package com.ychp.rpc.consumer;

import com.ychp.rpc.annotation.RpcConsumer;
import com.ychp.rpc.consumer.resolver.DefaultRpcConsumerResolver;
import com.ychp.rpc.consumer.resolver.RpcConsumerResolver;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 17/2/13
 */
@Configuration
public class RpcConsumerAutoConfiguration {

    @Autowired
    private RpcConsumerResolver rpcConsumerResolver;

    @Autowired
    private ApplicationContext applicationContext;

    @Bean(name = {"rpc-consumer-bean-processor"})
    public BeanPostProcessor beanPostProcessor() {
        return new BeanPostProcessor() {
            public Object postProcessBeforeInitialization(final Object bean, final String beanName) throws BeansException {
                Class objClz = bean.getClass();
                if(AopUtils.isAopProxy(bean)) {
                    objClz = AopUtils.getTargetClass(bean);
                }

                ReflectionUtils.doWithFields(objClz, field -> {
                    RpcConsumer rpcConsumer = field.getAnnotation(RpcConsumer.class);
                    if(rpcConsumer != null) {
                        Class type = field.getType();
                        Object object = null;

                        try {
                            object = applicationContext.getBean(type);
                        } catch (Exception var7) {
                            ;
                        }

                        if(object == null) {
                            object = rpcConsumerResolver.resolve(type, rpcConsumer);
                        }

                        try {
                            field.setAccessible(true);
                            field.set(bean, object);
                            field.setAccessible(false);
                        } catch (Exception var6) {
                            throw new BeanCreationException(beanName, var6);
                        }
                    }

                });
                return bean;
            }

            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                return bean;
            }
        };
    }

    @Bean
    @ConditionalOnProperty(value = {"rpc.mode"}, havingValue = "false", matchIfMissing = true)
    public RpcConsumerResolver rpcConsumerResolver(ApplicationContext applicationContext) {
        return new DefaultRpcConsumerResolver(applicationContext);
    }
}
