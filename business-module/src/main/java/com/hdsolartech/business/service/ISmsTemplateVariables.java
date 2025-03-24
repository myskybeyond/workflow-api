package com.hdsolartech.business.service;

import java.util.Map;

/**
 * @author mySkyBeyond
 * @version 1.0
 * 模板变量填充接口
 * @date 2024/6/24
 */
public interface ISmsTemplateVariables {

    /**
     * 消息模板format
     *
     * @param template  消息模板内容
     * @param variables 原变量
     * @param params    其他参数数组 0-被通知人id 1...
     */
    String format(String template, Map<String, Object> variables, String... params);

    /**
     * 添加业务数据属性
     *
     * @param variables      原变量
     * @param procInstanceId 流程实例id
     * @param params         预留其他业务参数...
     */
    void fillProperties(Map<String, Object> variables, String procInstanceId, String... params);
}
