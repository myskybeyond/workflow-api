package org.dromara.common.core.service;

import java.util.Map;

/**
 * 通用流程服务
 */
public interface ProcessService {

    /**
     * 根据key 启动流程实例
     * @param modelKey
     * @param variables
     * @return
     */
    Boolean startProcessByModelKey(String modelKey, Map<String, Object> variables);


    /**
     * 根据任务Id获取项目id
     * @param taskId
     * @return
     */
    String getProjectIdByTaskId(String taskId);


    /**
     * 根据任务Id获取订单id
     * @param taskId
     * @return
     */
    String getOrderIdByTaskId(String taskId);

}
