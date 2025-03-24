package com.myskybeyond.flowable.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.myskybeyond.flowable.common.constant.ProcessConstants;
import com.myskybeyond.flowable.service.AbstractAuditUsersService;
import lombok.AllArgsConstructor;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 多实例处理类
 *
 * @author KonBAI
 */
@AllArgsConstructor
@Component("multiInstanceHandler")
public class MultiInstanceHandler extends AbstractAuditUsersService {

    public Set<String> getUserIds(DelegateExecution execution) {
        Set<String> candidateUserIds = new LinkedHashSet<>();
        FlowElement flowElement = execution.getCurrentFlowElement();
        if (ObjectUtil.isNotEmpty(flowElement) && flowElement instanceof UserTask userTask) {
            String dataType = userTask.getAttributeValue(ProcessConstants.NAMASPASE, ProcessConstants.PROCESS_CUSTOM_DATA_TYPE);
            if ("USERS".equals(dataType) && CollUtil.isNotEmpty(userTask.getCandidateUsers())) {
                // 添加候选用户id
                candidateUserIds.addAll(userTask.getCandidateUsers());
            } else if (CollUtil.isNotEmpty(userTask.getCandidateGroups())) {
                // 获取组的ID，角色ID集合或部门ID集合 或岗位ID集合
                List<Long> groups = userTask.getCandidateGroups().stream()
                    .map(item -> Long.parseLong(item.substring(4)))
                    .collect(Collectors.toList());
                candidateUserIds = queryCandidateUserIds(dataType, groups);
            }
        }
        return candidateUserIds;
    }
}
