package com.myskybeyond.flowable.domain.bo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;

/**
 * 流程表单业务对象
 *
 * @author KonBAI
 * @createTime 2022/3/7 22:07
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WfFormBo extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 表单主键
     */
    @NotNull(message = "表单ID不能为空", groups = { EditGroup.class })
    private Long formId;

    /**
     * 表单名称
     */
    @NotBlank(message = "表单名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String formName;

    /**
     * 表单内容
     */
    @NotBlank(message = "表单内容不能为空", groups = { AddGroup.class, EditGroup.class })
    private String content;

    /**
     * 备注
     */
    private String remark;

    /**
     * 分类
     */
    @NotBlank(message = "分类不能为空", groups = { AddGroup.class, EditGroup.class })
    private String categoryCode;
}
