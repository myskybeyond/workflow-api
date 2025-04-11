package com.myskybeyond.dingtalk.domain;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

/**
 * @author MySkyBeyond
 * @version 1.0
 * 模板消息测试接口请求对象
 * @date 2024/5/6
 */
@Data
public class SmsTemplateSendBo {

    @NotNull(message = "模板内容不能为空")
    private String templateContent;
    @NotNull(message = "应用名称不能为空")
    private String orgName;
    @NotNull(message = "应用用户ID不能为空")
    private String dingTalkUserId;
    @NotNull(message = "参数不能为空")
    private Map<String, Object> vars;
}
