package com.myskybeyond.flowable.service.impl;

import cn.hutool.core.util.StrUtil;
import com.myskybeyond.flowable.service.ISmsTemplateVariables;
import com.myskybeyond.flowable.utils.CustomLookupResolver;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.dromara.system.domain.vo.SysUserVo;
import org.dromara.system.service.ISysUserService;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;


/**
 * @author MySkyBeyond
 * @version 1.0
 * Brief description of the class.
 * @date 2024/6/24
 */
@Service
@Slf4j
@Data
public class SmsTemplateVariablesImpl implements ISmsTemplateVariables {

    private final RepositoryService repositoryService;
    private final HistoryService historyService;
    private final ISysUserService sysUserService;



    @Override
    public String format(String template, Map<String, Object> variables, String... params) {
        log.info("解析模板内容,模板内容: {}, 模板变量参数: {}" ,template, variables);
        // 创建自定义查找器
        StrSubstitutor substitutor = new StrSubstitutor(variables);
        substitutor.setVariableResolver(new CustomLookupResolver(variables));
        // 替换模板中的变量
        String result = substitutor.replace(template);
        // 处理任务审批通知模板，多个候选用户时${flow.processor}无法正确解析的问题
//        if(result.contains(FLOW_PROCESSOR_STR)) {
//            log.info("开始处理模板变量{}的解析", FLOW_PROCESSOR_STR);
//            if(Objects.nonNull(params) && params.length > 0) {
//                result = result.replace(FLOW_PROCESSOR_STR, params[0]);
//                log.info("处理完成，模板变量{}解析结果为：{} ",FLOW_PROCESSOR_STR, result);
//            }else {
//                log.error("模板变量{}的解析处理未完成，接口参数未传值", FLOW_PROCESSOR_STR);
//            }
//        }
        log.info("解析模板参数后的模板内容为: {}" ,result);
        return result;
    }

    @Override
    public void fillProperties(Map<String, Object> variables, String procInstanceId, String... params) {
//        HdSmsTemplateFlowVariable hdSmsTemplateFlowVariable = new HdSmsTemplateFlowVariable();
//        if (StrUtil.isNotEmpty(procInstanceId)) {
//            // 流程定义id、流程名称、流程发起人
//            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(procInstanceId).singleResult();
//            hdSmsTemplateFlowVariable.setProcDefName(historicProcessInstance.getProcessDefinitionName());
//            hdSmsTemplateFlowVariable.setProcDefId(historicProcessInstance.getProcessDefinitionId());
//
//            hdSmsTemplateFlowVariable.setInitiator(queryUserNameByUserId(historicProcessInstance.getStartUserId()));
//
//            // 记录消息使用
//            variables.put(PROCESS_DEF_NAME,historicProcessInstance.getProcessDefinitionName());
//            variables.put(PROCESS_DEF_ID,historicProcessInstance.getProcessDefinitionId());
//
//        }
//        if(Objects.nonNull(params) && params.length > 0) {
////            节点完成时间 节点名称、节点处理人、节点接收时间
//            String taskId = params[0];
//            HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
//            hdSmsTemplateFlowVariable.setProcNodeName(historicTaskInstance.getName());
//            hdSmsTemplateFlowVariable.setProcessor(queryUserNameByUserId(historicTaskInstance.getAssignee()));
//            hdSmsTemplateFlowVariable.setReceiveTime(DateUtil.format(historicTaskInstance.getCreateTime(), DatePattern.NORM_DATETIME_FORMAT));
//            hdSmsTemplateFlowVariable.setCompleteTime(DateUtil.format(historicTaskInstance.getEndTime(), DatePattern.NORM_DATETIME_FORMAT));
//
//            // 记录消息使用
//            variables.put(PROCESS_NODE_NAME,historicTaskInstance.getName());
//        }
//        variables.put(TEMPLATE_FLOW, hdSmsTemplateFlowVariable);
    }

    private String queryUserNameByUserId(String userIdStr) {
        if(StrUtil.isNotEmpty(userIdStr)) {
            try {
                Long userId = Long.valueOf(userIdStr);
                SysUserVo sysUserVo = sysUserService.selectUserById(userId);
                if(Objects.nonNull(sysUserVo)) {
                    return sysUserVo.getNickName();
                }
            }catch (NumberFormatException ex) {
                log.error("用户id: {} 由String转换为Long发生异常: {}", userIdStr,ex.getMessage());
            }
        }
        return null;
    }
}
