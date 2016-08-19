package com.ychp.coding.common.event;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.Executors;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 16/8/19
 */
@Slf4j
@Component
public class CoreEventDispatcher {


    protected final EventBus eventBus;

    @Autowired
    private ApplicationContext applicationContext;


    public CoreEventDispatcher() {
        this(Runtime.getRuntime().availableProcessors() + 1);
    }

    public CoreEventDispatcher(Integer threadCount) {
        eventBus = new AsyncEventBus(Executors.newFixedThreadPool(threadCount));
    }

    @PostConstruct
    public void registerListeners() {
        Map<String, EventListener> listeners = applicationContext.getBeansOfType(EventListener.class);
        for(EventListener eventListener : listeners.values()) {
            eventBus.register(eventListener);
        }
    }

    /**
     * 发布事件
     */
    public void publish(Object event) {
        log.debug("publish an event({})", event);
        eventBus.post(event);
    }
}
