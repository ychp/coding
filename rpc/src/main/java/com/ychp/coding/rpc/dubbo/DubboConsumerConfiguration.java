package com.ychp.coding.rpc.dubbo;

import com.alibaba.dubbo.config.ConsumerConfig;
import com.alibaba.dubbo.config.ProviderConfig;
import com.alibaba.dubbo.config.spring.ServiceBean;
import com.ychp.coding.rpc.annotation.RpcProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.test.ImportAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 16/9/21
 */
@Configuration
@Import({ DubboConfiguration.class})
@ConditionalOnProperty(
        value = {"rpc.mode"},
        havingValue = "dubbo"
)
public class DubboConsumerConfiguration {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private DubboProperties dubboProperties;

    @Autowired
    private ConsumerConfig consumerConfig;




}
