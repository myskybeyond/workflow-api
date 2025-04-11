package com.myskybeyond.web.service.impl;

import cn.hutool.core.util.StrUtil;
import com.myskybeyond.business.common.enums.SmsInfoSourceEnum;
import com.myskybeyond.business.service.IHdSmsInfoService;
import com.myskybeyond.business.service.ISmsTemplateVariables;
import com.myskybeyond.wechatenterprise.service.WeChatEnterpriseServiceApi;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.service.IMessageSendService;
import org.dromara.common.core.service.ISocialUserService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

/**
 * @author MySkyBeyond
 * @version 1.0
 * 企业微信通知消息发送实现类
 * @date 2025/1/20
 */
@Slf4j
@Service
@Primary
public class WeComMsgSend implements IMessageSendService{

    private final IHdSmsInfoService smsInfoService;
    private final WeChatEnterpriseServiceApi weChatEnterpriseServiceApi;
    private final ISmsTemplateVariables smsTemplateVariables;
    private final ISocialUserService socialUserService;

    public WeComMsgSend(IHdSmsInfoService smsInfoService,
                        WeChatEnterpriseServiceApi weChatEnterpriseServiceApi,
                        ISmsTemplateVariables smsTemplateVariables,
                        ISocialUserService weComUserService){
        this.smsInfoService = smsInfoService;
        this.weChatEnterpriseServiceApi = weChatEnterpriseServiceApi;
        this.smsTemplateVariables = smsTemplateVariables;
        this.socialUserService = weComUserService;

    }
    @Override
    public void send(String toUsers, String content, String fromUser, Map<String, Object> params) {
        saveMsgLog(content, toUsers, fromUser, params);
        content = smsTemplateVariables.format(content, params);
        callWeComSendMsg(content, toUsers, null);
    }

    @Override
    public void send(String toUser, String content, String fromUser, String appOrg, Map<String, Object> params) {
        saveMsgLog(content, toUser, fromUser, params);
        content = smsTemplateVariables.format(content, params);
        callWeComSendMsg(content, toUser, appOrg);
    }

    /**
     * @param content    发送内容
     * @param toUser     接收用户
     * @param appOrg     接收用户组织（用于限制用户属于多个组织，设定此参数则只发送此参数组织）
     */
    private void callWeComSendMsg(String content, String toUser, String appOrg) {
        if (StrUtil.isEmpty(content) || StrUtil.isEmpty(toUser)) {
            throw new IllegalArgumentException("调用企业微信通知接口异常:content or toUser is null");
        }
        Map<String, String> map = socialUserService.queryByUserIdsGroupBySource(toUser);
        if (StrUtil.isNotEmpty(appOrg)) {
            Set<String> keysToRetain = Set.of(appOrg);
            map.keySet().retainAll(keysToRetain);
            log.info("只发送组织: {}", appOrg);
        }
        map.forEach((k, v) -> {
            log.info("企业微信通知消息,企业微信组织: 【{}】,通知消息: {} 被通知用户: {}", k, content, v);
            weChatEnterpriseServiceApi.sendMessage(v, content, k);
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
