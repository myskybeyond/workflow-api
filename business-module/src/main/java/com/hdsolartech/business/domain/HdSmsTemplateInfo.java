package com.hdsolartech.business.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 消息模板信息对象 hd_sms_template_info
 *
 * @author Lion Li
 * @date 2024-04-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hd_sms_template_info")
public class HdSmsTemplateInfo extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 模板名称
     */
    private String name;

    /**
     * 模板编码
     */
    private String code;

    /**
     * 模板内容
     */
    private String content;

    /**
     * 状态;0:正常 1:停用
     */
    private Long status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 删除标志;0：存在 2：删除
     */
    @TableLogic
    private String delFlag;


}
