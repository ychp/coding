package com.ychp.rpc.consul.consumer;

import com.ychp.rpc.consul.ConsulRpcConfiguration;
import com.ychp.rpc.consul.properties.ConsulProperties;
import com.ychp.rpc.consumer.RpcConsumerAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 17/2/14
 */
@Configuration
@Import({RpcConsumerAutoConfiguration.class, ConsulRpcConfiguration.class})
@EnableConfigurationProperties({ConsulProperties.class})
public class ConsulRpcConsumerAutoConfiguration {

    @Bean
    public ConsulRpcConsumerResolver consulRpcConsumerResolver(ApplicationContext applicationContext, ConsulProperties consulProperties) {
        return new ConsulRpcConsumerResolver(applicationContext, consulProperties);
    }
}