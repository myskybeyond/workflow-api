package com.myskybeyond.flowable.service;

import cn.hutool.core.util.StrUtil;
import com.myskybeyond.flowable.utils.ModelUtils;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.task.api.Task;

import java.util.Objects;

import static com.myskybeyond.flowable.common.constant.ProcessConstants.*;

/**
 * @author MySkyBeyond
 * @version 1.0
 * 模型设计抽象类(用于获取模型设计时自定义的业务数据)
 * @date 2024/7/18
 */
@Slf4j
public abstract class AbstractModelService extends AbstractAuditUsersService {

    /**
     * 获取抄送人
     * tips:因节点通知闲置，暂时使用completeCandidateUsers和completeCandidateUsersTypes,后续根据业务需求扩展copyCandidateUsers和completeCopyUsersTypes
     * @param bpmnModel 模型
     * @param task 任务
     * @return 抄送人id，多个以英文逗号分割
     */
    public String getCopyUserIdsFromModel(BpmnModel bpmnModel, Task task) {
        if(Objects.nonNull(bpmnModel) && StrUtil.isNotEmpty(task.getTaskDefinitionKey()) && StrUtil.isNotEmpty(task.getProcessInstanceId())) {
            String completeCandidateUsers = ModelUtils.getUserTaskAttributeValue(bpmnModel, task.getTaskDefinitionKey(), PROCESS_CUSTOM_COMPLETE_CANDIDATE_USERS);
            String completeCandidateUsersTypes = ModelUtils.getUserTaskAttributeValue(bpmnModel,task.getTaskDefinitionKey(), PROCESS_CUSTOM_COMPLETE_CANDIDATE_USERS_TYPES);
            return parseUserIdsOfSpecifiedRule(completeCandidateUsersTypes, completeCandidateUsers,task.getProcessInstanceId());
        }else {
            log.warn("任务key： {} 从模型设计中未获取到抄送人", task.getTaskDefinitionKey());
            return null;
        }
    }

    /**
     * 获取任务完成后通知人(用于发送节点完成后的通知消息)
     * @param bpmnModel 模型
     * @param task 任务
     * @return 抄送人id，多个以英文逗号分割
     */
    public String getCompleteUserIdsFromModel(BpmnModel bpmnModel, Task task) {
        if(Objects.nonNull(bpmnModel) && StrUtil.isNotEmpty(task.getTaskDefinitionKey()) && StrUtil.isNotEmpty(task.getProcessInstanceId())) {
            String completeCandidateUsers = ModelUtils.getUserTaskAttributeValue(bpmnModel, task.getTaskDefinitionKey(), PROCESS_CUSTOM_COMPLETE_CANDIDATE_USERS);
            String completeCandidateUsersTypes = ModelUtils.getUserTaskAttributeValue(bpmnModel,task.getTaskDefinitionKey(), PROCESS_CUSTOM_COMPLETE_CANDIDATE_USERS_TYPES);
            return parseUserIdsOfSpecifiedRule(completeCandidateUsersTypes, completeCandidateUsers,task.getProcessInstanceId());
        }else {
            log.warn("任务key： {} 从模型设计中未获取到通知人", task.getTaskDefinitionKey());
            return null;
        }
    }
}
