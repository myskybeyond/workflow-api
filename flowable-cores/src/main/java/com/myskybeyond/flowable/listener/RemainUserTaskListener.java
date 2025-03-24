package com.myskybeyond.flowable.listener;

import cn.hutool.core.util.StrUtil;
import com.myskybeyond.flowable.service.AbstractAuditUsersService;
import com.myskybeyond.flowable.service.IFlowMessageService;
import com.myskybeyond.flowable.utils.ModelUtils;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.utils.StringUtils;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.identitylink.api.IdentityLinkInfo;
import org.flowable.task.service.delegate.DelegateTask;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.myskybeyond.flowable.common.constant.ProcessConstants.*;


/**
 * 流转到用户任务节点时执行的动作(before)
 */
@Component("remainUserTaskListener")
@Slf4j
public class RemainUserTaskListener extends AbstractAuditUsersService implements TaskListener, ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void notify(DelegateTask delegateTask) {
        log.info("{} {}", delegateTask.getName(), delegateTask.getId());
//        RepositoryService repositoryService = applicationContext.getBean(RepositoryService.class);
//        RuntimeService runtimeService = applicationContext.getBean(RuntimeService.class);
//        IFlowMessageService flowMessageService = applicationContext.getBean(IFlowMessageService.class);
//        ISmsTemplateVariables smsTemplateVariables = applicationContext.getBean(ISmsTemplateVariables.class);
//        // 获取 bpmn 模型
//        BpmnModel bpmnModel = repositoryService.getBpmnModel(delegateTask.getProcessDefinitionId());
//        String receiveNoticeIds = ModelUtils.getUserTaskAttributeValue(bpmnModel, delegateTask.getTaskDefinitionKey(), PROCESS_CUSTOM_RECEIVE_NOTICE_IDS);
//        if (StrUtil.isEmpty(receiveNoticeIds)) {
//            log.info("任务实例 {} 未配置消息模板,不发送通知消息", delegateTask.getProcessInstanceId());
//        } else {
//            String dataType = ModelUtils.getUserTaskAttributeValue(bpmnModel, delegateTask.getTaskDefinitionKey(), PROCESS_CUSTOM_DATA_TYPE);
//            String toUsers = delegateTask.getAssignee();
//            if (StrUtil.isEmpty(toUsers)) {
//                if (PROCESS_CUSTOM_USER_TYPE_USERS.equals(dataType)) {
//                    Set<String> userIds = delegateTask.getCandidates().stream().map(IdentityLinkInfo::getUserId).collect(Collectors.toSet());
//                    toUsers = String.join(StringUtils.SEPARATOR, userIds);
//                } else {
//                    // 处理未指定用户的任务，候选人全部发送
//                    List<Long> groups = delegateTask.getCandidates().stream()
//                        .map(item -> Long.parseLong(item.getGroupId().substring(4)))
//                        .toList();
//                    Set<String> userIdSet = queryCandidateUserIds(dataType, groups);
//                    toUsers = String.join(StringUtils.SEPARATOR, userIdSet);
//                }
//            }
//            Map<String, Object> variables = runtimeService.getVariables(delegateTask.getExecutionId());
//            if (StrUtil.isNotEmpty(toUsers)) {
//                // 填充业务数据属性
//                smsTemplateVariables.fillProperties(variables, delegateTask.getProcessInstanceId(), delegateTask.getId());
//                flowMessageService.send(toUsers, receiveNoticeIds, variables);
//            } else {
//                log.info("任务实例 {} 用户类别 {} 未获取到用户,不发送通知消息", delegateTask.getProcessInstanceId(), dataType);
//            }
//        }
    }

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        RemainUserTaskListener.applicationContext = applicationContext;
    }
}
