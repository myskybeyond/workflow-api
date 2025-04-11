package com.myskybeyond.business.domain.vo;

import lombok.Data;

/**
 * @author MySkyBeyond
 * @version 1.0
 * 消息模板流程变量对象
 * @date 2024/6/24
 */
@Data
public class HdSmsTemplateFlowVariable {

    /**
     * 流程定义id
     */
    private String procDefId;
    /**
     * 流程名称
     */
    private String procDefName;
    /**
     * 流程节点名称
     */
    private String procNodeName;
    /**
     * 流程发起人
     */
    private String initiator;
    /**
     * 节点处理人
     */
    private String processor;
    /**
     * 节点创建时间
     */
    private String receiveTime;
    /**
     * 节点完成时间
     */
    private String completeTime;
}
