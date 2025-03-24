package com.myskybeyond.flowable.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.myskybeyond.flowable.domain.WfCategory;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.util.Date;


/**
 * 流程分类视图对象 flow_category
 *
 * @author KonBAI
 * @date 2022-01-15
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = WfCategory.class)
public class WfCategoryVo {

    private static final long serialVersionUID = 1L;

    /**
     * 分类ID
     */
    @ExcelProperty(value = "分类ID")
    private Long categoryId;

    /**
     * 分类名称
     */
    @ExcelProperty(value = "分类名称")
    private String categoryName;

    /**
     * 分类编码
     */
    @ExcelProperty(value = "分类编码")
    private String code;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 父id
     */
    private Long categoryPid;

    /**
     * 排序字段
     */
    private Integer sort;


}
