package com.myskybeyond.web.service.impl;

import cn.hutool.core.util.StrUtil;
import com.myskybeyond.business.common.enums.SmsInfoSourceEnum;
import com.myskybeyond.business.service.IHdSmsInfoService;
import com.myskybeyond.business.service.ISmsTemplateVariables;
import com.myskybeyond.dingtalk.sevice.DingTalkServiceApi;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.service.DingTalkService;
import org.dromara.common.core.service.IDingTalkUserService;
import org.dromara.common.core.service.IMessageSendService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

/**
 * @author MySkyBeyond
 * @version 1.0
 * 钉钉通知消息发送实现类
 * @date 2024/6/6
 */
@Service
@Slf4j
@Data
public class DingTalkMsgSend implements DingTalkService, IMessageSendService {
    private final DingTalkServiceApi dingTalkServiceApi;
    private final IHdSmsInfoService smsInfoService;
    private final IDingTalkUserService dingTalkUserService;
    private final ISmsTemplateVariables smsTemplateVariables;

    @Override
    public void send(String toUser, String content, String fromUser, Map<String,Object> params) {
        saveMsgLog(content, toUser, fromUser, params);
        content = smsTemplateVariables.format(content, params);
        callDingTalkSendMsg(content, toUser, null);
    }

    @Override
    public void send(String toUser, String content, String fromUser, String appOrg, Map<String, Object> params) {
        saveMsgLog(content, toUser, fromUser, params);
        content = smsTemplateVariables.format(content, params);
        callDingTalkSendMsg(content, toUser, appOrg);
    }

    @Override
    public void send(String toUser, Long templateId, String fromUser, String appOrg, Map<String, Object> params) {
        DingTalkService.super.send(toUser, templateId, fromUser, appOrg, params);
    }

    /**
     * @param content    发送内容
     * @param toUser     接收用户
     * @param appOrg     接收用户组织（用于限制用户属于多个组织，设定此参数则只发送此参数组织）
     */
    private void callDingTalkSendMsg(String content, String toUser, String appOrg) {
        if (StrUtil.isEmpty(content) || StrUtil.isEmpty(toUser)) {
            throw new IllegalArgumentException("调用钉钉通知接口异常:content or toUser is null");
        }
        Map<String, String> map = dingTalkUserService.queryByUserIdsGroupBySource(toUser);
        if (StrUtil.isNotEmpty(appOrg)) {
            Set<String> keysToRetain = Set.of(appOrg);
            map.keySet().retainAll(keysToRetain);
            log.info("只发送组织: {}", appOrg);
        }
        map.forEach((k, v) -> {
            log.info("钉钉通知消息,钉钉组织: 【{}】,通知消息: {} 被通知用户: {}", k, content, v);
            dingTalkServiceApi.sendMessage(v, content, k);
        });
    }

    private void saveMsgLog(String content, String toUser, String fromUser,Map<String, Object> params) {
        Long fromUserId = null;
        try {
            fromUserId = StrUtil.isNotEmpty(fromUser) ? Long.valueOf(fromUser) : null;
        } catch (NumberFormatException e) {
            log.warn("来源用户参数解析错误,期待Long类型,传参值为: {}", fromUser);
        } finally {
            smsInfoService.insertByDingTalk(SmsInfoSourceEnum.innerCall, content, toUser, fromUserId, params);
        }
    }
}
