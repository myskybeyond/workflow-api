package com.myskybeyond.flowable.config;

import com.myskybeyond.flowable.listener.GlobalEventListener;
import lombok.AllArgsConstructor;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.engine.RuntimeService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * flowable全局监听配置
 *
 * @author ssc
 */
@Configuration
@AllArgsConstructor
public class GlobalEventListenerConfig implements ApplicationListener<ContextRefreshedEvent> {

	private final GlobalEventListener globalEventListener;
	private final RuntimeService runtimeService;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		// 注册自定义监听事件
		runtimeService.addEventListener(globalEventListener, FlowableEngineEventType.PROCESS_COMPLETED,
            FlowableEngineEventType.TASK_CREATED,
            FlowableEngineEventType.TASK_ASSIGNED,
            FlowableEngineEventType.TASK_COMPLETED);
	}
}
