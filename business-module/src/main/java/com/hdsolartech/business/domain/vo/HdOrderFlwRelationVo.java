package com.hdsolartech.business.domain.vo;

import com.hdsolartech.business.domain.HdOrderFlwRelation;
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
 * 订单流程关系视图对象 hd_order_flw_relation
 *
 * @author Lion Li
 * @date 2024-06-24
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HdOrderFlwRelation.class)
public class HdOrderFlwRelationVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 1:下单
     */
    @ExcelProperty(value = "1:下单")
    private Long type;

    /**
     * 项目ID
     */
    @ExcelProperty(value = "项目ID")
    private Long projectId;

    /**
     * 订单ID
     */
    @ExcelProperty(value = "订单ID")
    private Long orderId;

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
