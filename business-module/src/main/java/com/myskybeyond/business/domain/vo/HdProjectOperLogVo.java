package com.myskybeyond.business.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.myskybeyond.business.domain.HdProjectOperLog;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;



/**
 * 项目操作信息视图对象 hd_project_oper_log
 *
 * @author Lion Li
 * @date 2024-06-04
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HdProjectOperLog.class)
public class HdProjectOperLogVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 分类
     */
    @ExcelProperty(value = "分类")
    private Long category;

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
     * 操作类别
     */
    @ExcelProperty(value = "操作类别")
    private String operatorType;

    /**
     * 执行结果
     */
    @ExcelProperty(value = "执行结果")
    private Long result;

    /**
     * 说明
     */
    @ExcelProperty(value = "说明")
    private String description;


    /**
     * 创建者
     */
    private Long createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新者
     */
    private Long updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;

}
