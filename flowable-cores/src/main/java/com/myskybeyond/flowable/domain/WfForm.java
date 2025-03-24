package com.myskybeyond.flowable.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;

/**
 * 流程表单对象 wf_form
 *
 * @author KonBAI
 * @createTime 2022/3/7 22:07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wf_form")
public class WfForm extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 表单主键
     */
    @TableId(value = "form_id")
    private Long formId;

    /**
     * 表单名称
     */
    private String formName;

    /**
     * 表单内容
     */
    private String content;

    /**
     * 备注
     */
    private String remark;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableLogic
    private String delFlag;
    /**
     * 分类
     */
    private String categoryCode;
}
