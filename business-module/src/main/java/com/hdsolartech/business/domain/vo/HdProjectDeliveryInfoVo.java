package com.hdsolartech.business.domain.vo;

import com.hdsolartech.business.domain.HdProjectDeliveryInfo;
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
 * 项目发货信息视图对象 hd_project_delivery_info
 *
 * @author Lion Li
 * @date 2024-06-19
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HdProjectDeliveryInfo.class)
public class HdProjectDeliveryInfoVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

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
     * 名称
     */
    @ExcelProperty(value = "名称")
    private String name;

    /**
     * 是否同步;0:未同步 1:已同步
     */
    @ExcelProperty(value = "是否同步;0:未同步 1:已同步")
    private Long isSync;

    /**
     * 项目文件信息表主键
     */
    @ExcelProperty(value = "项目文件信息表主键")
    private Long projectFileId;

    /**
     * 创建人
     */
    @ExcelProperty(value = "创建人")
    private Long createBy;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 更新人
     */
    @ExcelProperty(value = "更新人")
    private Long updateBy;

    /**
     * 更新时间
     */
    @ExcelProperty(value = "更新时间")
    private Date updateTime;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 最新版本
     */
    private Long version;
}
