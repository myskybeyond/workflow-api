package com.hdsolartech.business.domain.vo;

import java.util.Date;

import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hdsolartech.business.domain.HdProjectProgressStatistics;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.common.excel.convert.ExcelOrderNameConvert;
import org.dromara.common.excel.convert.ExcelProgressNameConvert;
import org.dromara.common.excel.convert.ExcelProjectNameConvert;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;



/**
 * 项目进度统计信息视图对象 hd_project_progress_statistics
 *
 * @author Lion Li
 * @date 2024-06-29
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HdProjectProgressStatistics.class)
public class HdProjectProgressStatisticsVo implements Serializable {

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

    /**
     * 订单ID
     */
    @ExcelProperty(value = "订单",converter = ExcelOrderNameConvert.class)
    private Long orderId;


    /**
     * 到货进度
     */
    @ExcelProperty(value = "到货进度")
    private String arrival;

    /**
     * 生产进度
     */
    @ExcelProperty(value = "生产进度")
    private String production;

    /**
     * 镀锌进度 黑件
     */
    @ExcelProperty(value = "镀锌进度(黑件)")
    private String black;

    /**
     * 镀锌进度 白件
     */
    @ExcelProperty(value = "镀锌进度(白件)")
    private String white;

    /**
     * 发货进度
     */
    @ExcelProperty(value = "发货进度")
    private String delivery;



    /**
     * 关联进度ID
     */
    @ExcelProperty(value = "进度名称",converter = ExcelProgressNameConvert.class)
    private Long projectProgressId;
    /**
     * 下单日期
     */
    @DateTimeFormat("yyyy-MM-dd")
    @ExcelProperty(value = "下单日期")
    private Date orderDate;
    /**
     * 发货开始时间
     */
    @DateTimeFormat("yyyy-MM-dd")
    @ExcelProperty(value = "发货开始时间")
    private Date deliverStartDate;
    /**
     * 发货结束时间
     */
    @DateTimeFormat("yyyy-MM-dd")
    @ExcelProperty(value = "发货结束时间")
    private Date deliverEndDate;

    /**
     * 统计日期;固定、跟踪、电气、车棚
     */
    @DateTimeFormat("yyyy-MM-dd")
    @ExcelProperty(value = "统计日期")
    private Date statisticsDate;


}
