package com.myskybeyond.business.domain.bo;

import com.myskybeyond.business.domain.HdSmsTemplateInfo;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;

/**
 * 消息模板信息业务对象 hd_sms_template_info
 *
 * @author Lion Li
 * @date 2024-04-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HdSmsTemplateInfo.class, reverseConvertGenerate = false)
public class HdSmsTemplateInfoBo extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = {EditGroup.class})
    private Long id;

    /**
     * 模板名称
     */
    @NotBlank(message = "模板名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String name;

    /**
     * 模板编码
     */
    @NotBlank(message = "模板编码不能为空", groups = {AddGroup.class, EditGroup.class})
    private String code;

    /**
     * 模板内容
     */
    @NotBlank(message = "模板内容不能为空", groups = {AddGroup.class, EditGroup.class})
    private String content;

    /**
     * 状态;0:正常 1:停用
     */
//    @NotNull(message = "状态;0:正常 1:停用不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long status;

    /**
     * 备注
     */
    @NotBlank(message = "备注不能为空", groups = {AddGroup.class, EditGroup.class})
    private String remark;


}
