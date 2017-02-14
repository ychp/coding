package com.ychp.rpc.consul.service;

import com.orbitz.consul.AgentClient;
import com.orbitz.consul.Consul;
import com.orbitz.consul.HealthClient;
import com.orbitz.consul.model.agent.ImmutableRegCheck;
import com.orbitz.consul.model.agent.ImmutableRegistration;
import com.ychp.rpc.annotation.RpcProvider;
import com.ychp.rpc.service.RpcCenter;
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
public class ConsulRpcCenter extends RpcCenter {

    @Autowired
    private ApplicationContext applicationContext;

    private final Consul consul;

    private String host;

    private Integer port;

    private String defaultVersion;

    private String health;

    private Integer interval;

    @Autowired
    public ConsulRpcCenter(Consul consul, String host, Integer port, String defaultVersion, String health, Integer interval){
        this.consul = consul;
        this.host = host;
        this.port = port;
        this.defaultVersion = defaultVersion;
        this.health = health;
        this.interval = interval;
    }

    @Override
    public void serviceRegister() {
        log.info("============> service register");
        AgentClient agent = consul.agentClient();

        //健康检测
        ImmutableRegCheck check = ImmutableRegCheck.builder().http(health).interval(interval + "s").build();

        Map<String,Object> serviceMap = applicationContext.getBeansWithAnnotation(RpcProvider.class);

        ImmutableRegistration.Builder builder;
        String className;
        Object service;
        for(String name : serviceMap.keySet()){
            service = serviceMap.get(name);
            builder = ImmutableRegistration.builder();
            className = service.getClass().getSimpleName();
            if(service.getClass().getInterfaces() != null && service.getClass().getInterfaces().length > 0){
                className = service.getClass().getInterfaces()[0].getSimpleName();
            }
            builder.id(className)
                    .name(className.substring(0, 1).toLowerCase() + className.substring(1))
                    .addTags(RpcUtils.firstNonNull(service.getClass().getAnnotation(RpcProvider.class).version(), defaultVersion))
                    .address(host).port(port).addChecks(check);
            agent.register(builder.build());
            log.info("===> register service {}", className);
        }

        log.info("============> service register end");
    }

    @Override
    public void serviceConsumer() {
        log.info("============> service consumer");

        HealthClient client = consul.healthClient();
        String name = "authorityManager";

        //获取所有正常的服务（健康检测通过的）
        client.getHealthyServiceInstances(name).getResponse().forEach((resp) -> {
            log.info("===> consumer service {}", resp.getService());
        });
        log.info("============> service consumer end");

    }
}
