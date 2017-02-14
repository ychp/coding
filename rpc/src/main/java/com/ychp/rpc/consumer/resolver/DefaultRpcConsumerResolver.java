package com.ychp.rpc.consumer.resolver;

import com.ychp.rpc.annotation.RpcConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 17/2/13
 */
@Slf4j
public class DefaultRpcConsumerResolver implements RpcConsumerResolver {

    private final ApplicationContext applicationContext;

    public DefaultRpcConsumerResolver(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public <T> T resolve(Class<T> interfaceClazz, RpcConsumer rpcConsumer) throws BeansException {
        boolean check = true;
        if(StringUtils.hasText(rpcConsumer.check())) {
            check = Boolean.parseBoolean(rpcConsumer.check());
        }

        try {
            return this.applicationContext.getBean(interfaceClazz);
        } catch (Exception e) {
            log.warn("failed to resolve bean for type :{}", interfaceClazz);
            if(check) {
                throw e;
            } else {
                return null;
            }
        }
    }
}
