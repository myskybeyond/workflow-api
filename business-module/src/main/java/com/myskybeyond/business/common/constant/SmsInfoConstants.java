package com.myskybeyond.business.common.constant;

/**
 * @author MySkyBeyond
 * @version 1.0
 * 消息记录常量类
 * @date 2024/6/12
 */
public class SmsInfoConstants {

    /**
     * 流程定义id
     */
    public static final String PROCESS_DEF_ID = "procDefId";
    /**
     * 流程名称
     */
    public static final String PROCESS_DEF_NAME = "procDefName";
    /**
     * 流程节点名称
     */
    public static final String PROCESS_NODE_NAME = "procNodeName";
    /**
     * 流程发起人
     */
    public static final String PROCESS_START_USER_ID = "initiator";

    /**
     * 节点处理人
     */
    public static final String PROCESS_TASK_ASSIGN_ID = "processor";

    /**
     * 节点创建时间
     */
    public static final String PROCESS_TASK_CREATE_TIME = "receiveTime";
    /**
     * 节点完成时间
     */
    public static final String PROCESS_TASK_COMPLETE_TIME = "completeTime";
    /**
     * 项目id
     */
    public static final String PROJECT_ID = "projectId";
    /**
     * 项目名称
     */
    public static final String PROJECT_NAME = "projectName";
    /**
     * 销售订单id
     */
    public static final String ORDER_ID = "orderId";
    /**
     * 销售订单名称
     */
    public static final String ORDER_NAME = "orderName";

    /**
     * 模板消息项目占位符
     */
    public static final String TEMPLATE_PROJECT = "project";
    /**
     * 模板消息销售订单占位符
     */
    public static final String TEMPLATE_ORDER = "order";
    /**
     * 模板消息流程信息占位符
     */
    public static final String TEMPLATE_FLOW = "flow";
    /**
     * 流程节点候选人名称
     */
    public static final String TEMPLATE_CANDIDATE_USER_NAME = "candidateUserName";

    public static final String FLOW_PROCESSOR_STR = "${flow.processor}";
}
