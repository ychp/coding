package com.ychp.rpc.consumer.resolver;

import com.ychp.rpc.annotation.RpcConsumer;
import org.springframework.beans.BeansException;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 17/2/13
 */
public interface RpcConsumerResolver {
    <T> T resolve(Class<T> var1, RpcConsumer rpcConsumer) throws BeansException;
}
