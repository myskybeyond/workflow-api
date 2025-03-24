package com.myskybeyond.flowable.listener;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.myskybeyond.flowable.common.constant.ProcessConstants;
import com.myskybeyond.flowable.common.enums.ProcessStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEntityEvent;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.event.AbstractFlowableEngineEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Flowable 全局监听器
 *
 * @author konbai
 * @since 2023/3/8 22:45
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class GlobalEventListener extends AbstractFlowableEngineEventListener {

    @Autowired
    private RuntimeService runtimeService;

    /**
     * 流程结束监听器
     */
    @Override
    protected void processCompleted(FlowableEngineEntityEvent event) {
        String processExecutionId = event.getExecutionId();
        Object variable = runtimeService.getVariable(processExecutionId, ProcessConstants.PROCESS_STATUS_KEY);
        ProcessStatus status = ProcessStatus.getProcessStatus(Convert.toStr(variable));
        if (ObjectUtil.isNotNull(status) && ProcessStatus.RUNNING == status) {
            /*
             * edit by JianTao Li
             * 添加区分手工取消还是自然结束
             * 手工取消流程状态设置为已终止
             */
            Object handleToEnd = runtimeService.getVariable(processExecutionId, ProcessConstants.PROCESS_STATUS_HANDLE_TO_END);
            if (ObjectUtil.isNotNull(handleToEnd)) {
                Boolean handleToEndBoolVal = Convert.toBool(handleToEnd);
                if (handleToEndBoolVal) {
                    log.warn("流程: {} 设置为已终止状态.", processExecutionId);
                    runtimeService.setVariable(processExecutionId, ProcessConstants.PROCESS_STATUS_KEY, ProcessStatus.TERMINATED.getStatus());
                }
            } else {
                runtimeService.setVariable(processExecutionId, ProcessConstants.PROCESS_STATUS_KEY, ProcessStatus.COMPLETED.getStatus());
            }
        }
        super.processCompleted(event);
    }

    @Override
    protected void taskCreated(FlowableEngineEntityEvent event) {
    }

    @Override
    protected void taskAssigned(FlowableEngineEntityEvent event) {
        log.info("用户任务分配人员后执行的办法");
    }

    @Override
    protected void taskCompleted(FlowableEngineEntityEvent event) {
        log.info("用户任务完成后执行的办法");
    }
}
