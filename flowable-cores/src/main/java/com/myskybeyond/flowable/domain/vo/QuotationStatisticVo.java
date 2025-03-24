package com.myskybeyond.flowable.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName QuotationStatisticVo
 * @Description 报价统计vo
 * @Author zyf
 * @create 2024/6/28 13:39
 * @Version 1.0
 */
@Data
public class QuotationStatisticVo {

    /**
     * 项目id
     */
    private String projectId;

    /**
     * 发起业务员
     */
    private String salesman;


    /**
     * 设计人
     */
    private String designer;

    /**
     * 校对人
     */
    private String proofreader;

    /**
     * 审核人
     */
    private String auditer;


    /**
     * 接收日期
     */
    @ExcelProperty(value = "接收日期")
    private Date receivedDate;

    /**
     * 预计完成时间
     */
    @ExcelProperty(value = "预计完成时间")
    private Date expectedDate;

    /**
     * 实际完成时间
     */
    @ExcelProperty(value = "实际完成时间")
    private Date actualDate;

}
