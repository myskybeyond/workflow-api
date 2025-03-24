package com.hdsolartech.business.domain.vo;

import java.util.Date;

import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.hdsolartech.business.domain.HdQuotationStatistics;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDeptNameConvert;
import org.dromara.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.common.excel.convert.ExcelProjectNameConvert;
import org.dromara.common.excel.convert.ExcelUserNameConvert;

import java.io.Serial;
import java.io.Serializable;



/**
 * 项目报价统计信息视图对象 hd_quotation_statistics
 *
 * @author Lion Li
 * @date 2024-06-28
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HdQuotationStatistics.class)
public class HdQuotationStatisticsVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 项目ID
     */
    @ExcelProperty(value = "项目",converter = ExcelProjectNameConvert.class)
    private Long projectId;

    @ExcelProperty(value = "项目类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "support_type")
    private String projectType;

    /**
     * 部门ID
     */
    @ExcelProperty(value = "部门",converter = ExcelDeptNameConvert.class)
    private String deptId;

    /**
     * 发起业务员
     */
    @ExcelProperty(value = "发起业务员", converter = ExcelUserNameConvert.class)
    private Long salesman;

    /**
     * 设计员
     */
    @ExcelProperty(value = "设计员", converter = ExcelUserNameConvert.class)
    private Long designer;

    /**
     * 校对人
     */
    @ExcelProperty(value = "校对人", converter = ExcelUserNameConvert.class)
    private Long proofreader;

    /**
     * 审核人
     */
    @ExcelProperty(value = "审核人", converter = ExcelUserNameConvert.class)
    private Long auditer;

    /**
     * 接收日期
     */
    @DateTimeFormat("yyyy-MM-dd")
    @ExcelProperty(value = "接收日期")
    private Date receivedDate;

    /**
     * 预计完成时间
     */
    @DateTimeFormat("yyyy-MM-dd")
    @ExcelProperty(value = "预计完成时间")
    private Date expectedDate;

    /**
     * 实际完成时间
     */
    @DateTimeFormat("yyyy-MM-dd")
    @ExcelProperty(value = "实际完成时间")
    private Date actualDate;

    /**
     * 统计日期;固定、跟踪、电气、车棚
     */
    @DateTimeFormat("yyyy-MM-dd")
    @ExcelProperty(value = "统计日期")
    private Date statisticsDate;


}
