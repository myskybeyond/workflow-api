package com.myskybeyond.flowable.service;

import org.flowable.bpmn.model.BpmnModel;
import org.flowable.task.api.Task;

import java.util.Map;

public interface IFlowMessageService {

    /**
     * 发送钉钉通知消息(消息通知人支持角色，岗位单选)
     *
     * @param bpmnModel   bpmn模型
     * @param task        任务
     * @param copyUserIds 抄送用户Id(多个以英文逗号分割)
     * @param variables 流程变量
     */
    void send(BpmnModel bpmnModel, Task task, String copyUserIds, Map<String, Object> variables);

    /**
     * 发送通知消息
     *
     * @param toUserIds         接收用户Id(多个以英文逗号分割)
     * @param msgTemplateIds 消息模板id[]
     * @param variables      流程变量
     */
    void send(String toUserIds, String msgTemplateIds, Map<String, Object> variables);
}
