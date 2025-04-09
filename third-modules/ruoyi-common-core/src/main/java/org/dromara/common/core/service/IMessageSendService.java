package org.dromara.common.core.service;

import org.dromara.common.core.exception.ServiceException;

import java.util.Map;

/**
 * @author MySkyBeyond
 * @version 1.0
 * <p>
 * Brief description of the class.
 * @date 2025/1/20
 */
public interface IMessageSendService {

    /**
     * 发送通知消息
     * @param toUsers 系统用户（必输）,多个用户以英文逗号分割
     * @param content 发送内容（必输）
     * @param fromUser 发送人（必输）
     * @param params 其他参数（非必输） 可用的参数: {"projectId":"1,项目名称","orderId":"1,测试销售订单名称",
     *               "procName": "流程名称","procId":"流程定义id","procNodeName":"流程节点名称"}
     */
    void send(String toUsers, String content,String fromUser, Map<String,Object> params);
    /**
     * 发送通知消息
     * @param toUser 系统用户id（必输）
     * @param content 发送内容（必输）
     * @param fromUser 发送人（必输）
     * @param appOrg 机构（非必输）
     * @param params 消息参数（非必输）
     */
    void send(String toUser, String content,String fromUser, String appOrg, Map<String,Object> params);

    /**
     * 发送通知消息
     * @param toUser 系统用户id（必输）
     * @param templateId 模板Id（必输）
     * @param fromUser 发送人（必输）
     * @param appOrg 机构（非必输）
     * @param params 消息参数（非必输）
     */
    default void send(String toUser, Long templateId,String fromUser, String appOrg, Map<String,Object> params) {
        throw new ServiceException("此接口暂未实现,请实现或使用其他方法");
    }
}
