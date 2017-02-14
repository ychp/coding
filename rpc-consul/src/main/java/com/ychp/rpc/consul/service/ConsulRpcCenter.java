package com.ychp.rpc.consul.service;

import com.orbitz.consul.AgentClient;
import com.orbitz.consul.Consul;
import com.orbitz.consul.HealthClient;
import com.orbitz.consul.model.ConsulResponse;
import com.orbitz.consul.model.agent.ImmutableRegistration;
import com.orbitz.consul.model.health.ServiceHealth;
import com.ychp.rpc.annotation.RpcProvider;
import com.ychp.rpc.service.RpcCenter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.List;
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

    @Autowired
    public ConsulRpcCenter(Consul consul, String host, Integer port){
        this.consul = consul;
        this.host = host;
        this.port = port;
    }

    @Override
    public void serviceRegister() {
        log.info("============> service register");
        AgentClient agent = consul.agentClient();

        //健康检测
//        ImmutableRegCheck check = ImmutableRegCheck.builder().http("http://192.168.1.104:9020/health").interval("5s").build();

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
            builder.id(className).name(className.substring(0, 1).toLowerCase() + className.substring(1)).addTags(service.getClass().getAnnotation(RpcProvider.class).version()).address(host).port(port);
            agent.register(builder.build());
            log.info("===> register service {}", className);
        }

        log.info("============> service register end");
    }

    @Override
    public void serviceConsumer() {
        log.info("============> service consumer");

        HealthClient client = consul.healthClient();
//        String name = "tomcat";
        //获取所有服务
        List<ServiceHealth> services = client.getAllServiceInstances("authorityManager").getResponse();

        for(ServiceHealth serviceHealth : services){
            log.info("===> consumer service {}", serviceHealth.getService());
        }

        System.out.println(services.size());
        //获取所有正常的服务（健康检测通过的）
//        client.getHealthyServiceInstances(name).getResponse().forEach(System.out::println);
        log.info("============> service consumer end");

    }
}
