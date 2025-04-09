package com.myskybeyond.business.domain.bo;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

/**
 * @author MySkyBeyond
 * @version 1.0
 * 消息发送的请求对象
 * @date 2024/6/7
 */
@Data
public class TestMsgSendBo {

    @NotBlank(message = "模板内容不能为空")
    private String content;
    @NotBlank(message = "应用名称不能为空")
    private String appCode;
    @NotNull(message = "应用用户不能为空")
    @Min(value = 1, message = "应用用户编号必须大于1")
    private Long userId;
    @NotNull(message = "消息模板Id不能为空")
    @Min(value = 1, message = "消息模板Id必须大于1")
    private Long templateId;
    /**
     * 模板参数
     * examples: {projectName: 杭州亚太项目, projectModule: 不知道该写啥}
     */
    private Map<String, Object> contentParams;
}
