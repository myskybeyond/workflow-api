package com.myskybeyond.flowable.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.myskybeyond.flowable.domain.WfForm;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.util.Date;

/**
 * 流程分类视图对象
 *
 * @author KonBAI
 * @createTime 2022/3/7 22:07
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = WfForm.class)
public class WfFormVo {

    private static final long serialVersionUID = 1L;

    /**
     * 表单主键
     */
    @ExcelProperty(value = "表单ID")
    private Long formId;

    /**
     * 表单名称
     */
    @ExcelProperty(value = "表单名称")
    private String formName;

    /**
     * 表单内容
     */
    @ExcelProperty(value = "表单内容")
    private String content;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

    private Date createTime;

    private Date updateTime;

    /**
     * 分类
     */
    private String categoryCode;
}
