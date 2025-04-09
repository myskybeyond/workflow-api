package com.myskybeyond.business.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.myskybeyond.business.domain.HdOrderInfo;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;



/**
 * 销售订单信息视图对象 hd_order_info
 *
 * @author Lion Li
 * @date 2024-06-03
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HdOrderInfo.class)
public class HdOrderInfoVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 项目id
     */
    @ExcelProperty(value = "项目id")
    private Long projectId;

    /**
     * 销售订单编号
     */
    @ExcelProperty(value = "销售订单编号")
    private String orderNo;

    /**
     * 销售订单名称
     */
    @ExcelProperty(value = "销售订单名称")
    private String name;

    /**
     * 采购单位
     */
    @ExcelProperty(value = "采购单位")
    private String purchaseUnit;

    /**
     * 业主单位
     */
    @ExcelProperty(value = "业主单位")
    private String ownerUnit;

    /**
     * 项目地点
     */
    @ExcelProperty(value = "项目地点")
    private String location;

    /**
     * 联系人
     */
    @ExcelProperty(value = "联系人")
    private String contactPerson;

    /**
     * 联系电话
     */
    @ExcelProperty(value = "联系电话")
    private String contactPhone;

    /**
     * 邮箱
     */
    @ExcelProperty(value = "邮箱")
    private String contactEmail;

    /**
     * 其它
     */
    @ExcelProperty(value = "其它")
    private String other;

    /**
     * DC总容量(MWp)
     */
    @ExcelProperty(value = "DC总容量(MWp)")
    private String dc;

    /**
     * 销售订单描述
     */
    @ExcelProperty(value = "销售订单描述")
    private String description;

    /**
     * 支架类型;1：固定 2: 跟踪 3:车棚
     */
    @ExcelProperty(value = "支架类型;1：固定 2: 跟踪 3:车棚")
    private String supportType;

    /**
     * 状态;0:待执行 1:执行中 2:已完成
     */
    @ExcelProperty(value = "状态;0:待执行 1:执行中 2:已完成")
    private Long status;

    /**
     * 开始日期
     */
    @ExcelProperty(value = "开始日期")
    private Date startTime;

    /**
     * 预计结束日期
     */
    @ExcelProperty(value = "预计结束日期")
    private Date endTime;

    /**
     * 销售订单负责人
     */
    @ExcelProperty(value = "销售订单负责人")
    private Long manager;

    /**
     * 进度
     */
    @ExcelProperty(value = "进度")
    private String progress;


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

    /**
     * 项目文件信息
     */
    private String relaFileInfo;
}
