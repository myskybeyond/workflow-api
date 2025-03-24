package com.hdsolartech.business.domain.bo;

import com.hdsolartech.business.domain.HdSmsInfo;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 消息历史记录信息业务对象 hd_sms_info
 *
 * @author Lion Li
 * @date 2024-04-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HdSmsInfo.class, reverseConvertGenerate = false)
public class HdSmsInfoBo extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 消息模板ID
     */
    @NotNull(message = "消息模板ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long smsTemplateId;

    /**
     * 模板名称
     */
    @NotBlank(message = "模板名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 模板编码
     */
    @NotBlank(message = "模板编码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String code;

    /**
     * 项目ID
     */
    @NotNull(message = "项目ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long projectId;

    /**
     * 项目名称
     */
    @NotBlank(message = "项目名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String projectName;

    /**
     * 销售订单ID
     */
    @NotNull(message = "销售订单ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long orderId;

    /**
     * 销售订单名称
     */
    @NotBlank(message = "销售订单名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String orderName;

    /**
     * 流程定义ID;关联 act_re_procdef 主键
     */
    @NotBlank(message = "流程定义ID;关联 act_re_procdef 主键不能为空", groups = { AddGroup.class, EditGroup.class })
    private String procId;

    /**
     * 流程定义名称
     */
    @NotBlank(message = "流程定义名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String procName;

    /**
     * 来源
     */
    @NotBlank(message = "来源不能为空", groups = { AddGroup.class, EditGroup.class })
    private String source;

    /**
     * 来源用户ID
     */
    @NotNull(message = "来源用户ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long sourceUserId;

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long userId;

    /**
     * 消息内容
     */
    @NotBlank(message = "消息内容不能为空", groups = { AddGroup.class, EditGroup.class })
    private String content;

    /**
     * 是否已读;0:未读 1:已读
     */
    @NotNull(message = "是否已读;0:未读 1:已读不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long isRead;

    /**
     * 参数
     */
    @NotBlank(message = "参数不能为空", groups = { AddGroup.class, EditGroup.class })
    private String smsParams;

    /**
     * 模板内容
     */
    @NotBlank(message = "模板内容不能为空", groups = { AddGroup.class, EditGroup.class })
    private String templateContent;

    private String procNodeName;


}
