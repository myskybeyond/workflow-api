package com.myskybeyond.flowable.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Primary
public class FlowMessageServiceImpl extends AbstractModelService implements IFlowMessageService {

//    private final DingTalkServiceApi dingTalkServiceApi;
//    private final IHdSmsTemplateInfoService templateInfoService;
//    private final IHdSmsInfoService smsInfoService;
//    private final IDingTalkUserService dingTalkUserService;
//    private final ISmsTemplateVariables smsTemplateVariables;

    @Override
    @Async
    public void send(String toUserIds, String msgTemplateIds, Map<String, Object> variables) {
//        List<UserSocial> userSocials = dingTalkUserService.queryByUserIdsUnGroup(toUserIds);
//        // 添加消息记录
//        smsInfoService.insertByDingTalk(SmsInfoSourceEnum.flowCall, msgTemplateIds, toUserIds, variables, userSocials);
//        // 消息通知(节点接收)
//        callDingTalkSendMsgOfUserByUser(msgTemplateIds, userSocials, variables);
    }

    @Override
    @Async
    public void send(BpmnModel bpmnModel, Task task, String copyUserIds, Map<String, Object> variables) {
//        String completeNoticeIds = ModelUtils.getUserTaskAttributeValue(bpmnModel, task.getTaskDefinitionKey(), PROCESS_CUSTOM_COMPLETE_NOTICE_IDS);
//        String copyNoticeIds = ModelUtils.getUserTaskAttributeValue(bpmnModel, task.getTaskDefinitionKey(), PROCESS_CUSTOM_COPY_NOTICE_IDS);
//        // 消息通知(节点通知)
//        String completeCandidateUserIds = getCompleteUserIdsFromModel(bpmnModel, task);
//        if (StrUtil.isNotEmpty(completeNoticeIds) && StrUtil.isNotEmpty(completeCandidateUserIds)) {
//            smsInfoService.insertByDingTalk(SmsInfoSourceEnum.flowCall, completeNoticeIds, completeCandidateUserIds, variables);
//            callDingTalkSendMsg("节点通知", completeNoticeIds, completeCandidateUserIds, variables);
//        }
//        if (StrUtil.isNotEmpty(copyUserIds) && StrUtil.isNotEmpty(copyNoticeIds)) {
//            smsInfoService.insertByDingTalk(SmsInfoSourceEnum.flowCall, copyNoticeIds, copyUserIds, variables);
//            // 消息通知(节点抄送)
//            callDingTalkSendMsg("节点抄送", copyNoticeIds, copyUserIds, variables);
//        }
    }

//    private List<HdSmsTemplateInfoVo> queryAndConvertTemplate(String idsStr) {
//        if (StrUtil.isEmpty(idsStr)) {
//            log.warn("通知消息模板id参数 {} 为空", idsStr);
//            return null;
//        }
//        String[] ids = idsStr.split(",");
//        // String[]转换为Long[]
//        Long[] pks = new Long[ids.length];
//        for (int i = 0; i < ids.length; i++) {
//            pks[i] = Long.valueOf(ids[i]);
//        }
//        return templateInfoService.queryByIds(pks);
//    }

    /**
     * 发送钉钉通知消息(一次发多个用户)
     *
     * @param idsStr     消息模板id,多个以英文逗号分割
     * @param toUser     接收用户id,多个以英文逗号分割
     * @param noticeType 通知类别，记录日志使用
     * @param params     流程变量 记录日志使用
     */
//    public void callDingTalkSendMsg(String noticeType, String idsStr, String toUser, Map<String, Object> params) {
//        List<HdSmsTemplateInfoVo> copyTemplates = queryAndConvertTemplate(idsStr);
//        Map<String, String> map = dingTalkUserService.queryByUserIdsGroupBySource(toUser);
//        if (copyTemplates != null && !copyTemplates.isEmpty() && Objects.nonNull(map)) {
//            copyTemplates.forEach(
//                template -> {
//                    if (template.getStatus() == 0) {
//                        String finalContent = smsTemplateVariables.format(template.getContent(), params);;
//                        map.forEach((k, v) -> {
//                            log.info("发送钉钉通知消息(一次发多个用户),钉钉通知消息({}),钉钉组织: 【{}】,通知消息模板: {} 被通知用户: {}", noticeType, k, finalContent, v);
//                            dingTalkServiceApi.sendMessage(v, finalContent, k);
//                        });
//                    } else {
//                        log.error("发送钉钉通知消息(一次发多个用户),模板id: {} 状态: {} 不可用,请检查设置.", template.getId(), template.getStatus());
//                    }
//                }
//            );
//        } else {
//            log.warn("发送钉钉通知消息(一次发多个用户),钉钉通知消息({})错误,参数不对,不发送钉钉通知消息: 模板参数: {} 或 接收人:{} 为空", noticeType, copyTemplates, map);
//        }
//    }

    /**
     * 发送钉钉通知消息(一个用户一个用户发)
     *
     * @param idsStr     消息模板id,多个以英文逗号分割
     * @param userSocials     接收用户对象
     * @param params     流程变量 记录日志使用
     */
//    public void callDingTalkSendMsgOfUserByUser(String idsStr, List<UserSocial> userSocials, Map<String, Object> params) {
//        List<HdSmsTemplateInfoVo> copyTemplates = queryAndConvertTemplate(idsStr);
//        if (copyTemplates != null && !copyTemplates.isEmpty() && Objects.nonNull(userSocials) && !userSocials.isEmpty()) {
//            copyTemplates.forEach(
//                template -> {
//                    if (template.getStatus() == 0) {
//                        userSocials.forEach((u) -> {
//                            String content = smsTemplateVariables.format(template.getContent(), params, String.valueOf(u.getNickName()));
//                            log.info("发送钉钉通知消息(一个用户一个用户发),节点接收调用钉钉通知消息,钉钉组织: 【{}】,通知消息模板: {} 被通知用户(钉钉组织用户id): {}", u.getSource(), content, u.getSocialUserId());
//                            dingTalkServiceApi.sendMessage(u.getSocialUserId(), content, u.getSource());
//                        });
//                    } else {
//                        log.error("发送钉钉通知消息(一个用户一个用户发),节点接收调用,模板id: {} 状态: {} 不可用,请检查设置.", template.getId(), template.getStatus());
//                    }
//                }
//            );
//        } else {
//            log.warn("发送钉钉通知消息(一个用户一个用户发),节点接收调用钉钉通知消息错误,参数不对,不发送钉钉通知消息: 模板参数: {} 或 接收人:{} 为空", copyTemplates, userSocials);
//        }
//    }

}
