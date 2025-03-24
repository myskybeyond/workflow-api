package com.hdsolartech.business.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 消息历史记录信息对象 hd_sms_info
 *
 * @author Lion Li
 * @date 2024-04-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hd_sms_info")
public class HdSmsInfo extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 消息模板ID
     */
    private Long smsTemplateId;

    /**
     * 模板名称
     */
    private String name;

    /**
     * 模板编码
     */
    private String code;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 销售订单ID
     */
    private Long orderId;

    /**
     * 销售订单名称
     */
    private String orderName;

    /**
     * 流程定义ID;关联 act_re_procdef 主键
     */
    private String procId;

    /**
     * 流程定义名称
     */
    private String procName;

    /**
     * 来源
     */
    private String source;

    /**
     * 来源用户ID
     */
    private Long sourceUserId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 是否已读;0:未读 1:已读
     */
    private Long isRead;

    /**
     * 参数
     */
    private String smsParams;

    /**
     * 模板内容
     */
    private String templateContent;

    /**
     * 删除标志;0：存在 2：删除
     */
    @TableLogic
    private String delFlag;

    /**
     * 流程节点名称
     */
    private String procNodeName;


}
