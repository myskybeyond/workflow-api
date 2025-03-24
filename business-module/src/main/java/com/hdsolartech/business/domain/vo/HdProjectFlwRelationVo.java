package com.hdsolartech.business.domain.vo;

import com.hdsolartech.business.domain.HdProjectFlwRelation;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;



/**
 * 项目流程关系视图对象 hd_project_flw_relation
 *
 * @author Lion Li
 * @date 2024-06-24
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HdProjectFlwRelation.class)
public class HdProjectFlwRelationVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 1:报价
     */
    @ExcelProperty(value = "1:报价")
    private Long type;

    /**
     * 项目ID
     */
    @ExcelProperty(value = "项目ID")
    private Long projectId;

    /**
     * 任务D
     */
    @ExcelProperty(value = "任务D")
    private String actHiTaskinstId;

    /**
     * 流程实例ID
     */
    @ExcelProperty(value = "流程实例ID")
    private String procInstId;

    /**
     * 流程定义ID
     */
    @ExcelProperty(value = "流程定义ID")
    private String procDefId;


}
