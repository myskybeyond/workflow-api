package com.hdsolartech.business.listener;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * @author MySkyBeyond
 * @version 1.0
 * 通用的事件发布-用于项目多模块间通讯
 * @date 2024/6/4
 */
@Component
public class GlobalEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public GlobalEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void publishEvent(ApplicationEvent event) {
        eventPublisher.publishEvent(event);
    }
}
