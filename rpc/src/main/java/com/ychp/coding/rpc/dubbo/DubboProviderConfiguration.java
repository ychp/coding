package com.ychp.coding.rpc.dubbo;

import com.alibaba.dubbo.config.ProviderConfig;
import com.alibaba.dubbo.config.spring.ServiceBean;
import com.ychp.coding.rpc.annotation.RpcProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.test.ImportAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 16/9/21
 */
@ImportAutoConfiguration(DubboConfiguration.class)
@Configuration
public class DubboProviderConfiguration {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private DubboProperties dubboProperties;

    @Autowired
    private ProviderConfig providerConfig;

    @PostConstruct
    public void init() throws Exception {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(RpcProvider.class);

        for(Map.Entry entry : beans.entrySet()){
            this.publishProviders((String)entry.getKey(), entry.getValue());
        }
    }

    private void publishProviders(String key, Object bean) throws Exception {
        RpcProvider rpcProvider = this.applicationContext.findAnnotationOnBean(key, RpcProvider.class);
        ServiceBean providerBean = new ServiceBean();
        Class interfaceType;
        if(rpcProvider.interfaceClass() != Void.TYPE) {
            interfaceType = rpcProvider.interfaceClass();
        } else if(StringUtils.hasText(rpcProvider.interfaceName())) {
            interfaceType = Class.forName(rpcProvider.interfaceName());
        } else {
            if(bean.getClass().getInterfaces().length == 0) {
                throw new IllegalStateException("Failed to export remote rpcProvider class " + bean.getClass().getName() + ", cause: The @RpcService undefined interfaceClass or interfaceName," + " and the rpcProvider class unimplemented any interfaces.");
            }

            interfaceType = bean.getClass().getInterfaces()[0];
        }

        providerBean.setInterface(interfaceType);
        if(StringUtils.hasText(rpcProvider.group())) {
            providerBean.setGroup(rpcProvider.group());
        }

        providerBean.setVersion(StringUtils.hasText(rpcProvider.version())?rpcProvider.version():this.dubboProperties.getVersion());
        providerBean.setApplicationContext(this.applicationContext);
        providerBean.setApplication(this.providerConfig.getApplication());
        providerBean.setProtocol(this.providerConfig.getProtocol());
        providerBean.setRegistry(this.providerConfig.getRegistry());
        providerBean.setProvider(this.providerConfig);
        providerBean.setRef(bean);
        providerBean.afterPropertiesSet();
        providerBean.export();

    }


}
