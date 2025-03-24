package com.hdsolartech.business.domain.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hdsolartech.business.domain.HdProjectInfo;
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
 * 项目信息视图对象 hd_project_info
 *
 * @author Lion Li
 * @date 2024-06-03
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HdProjectInfo.class)
public class HdProjectInfoVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 项目编号
     */
    @ExcelProperty(value = "项目编号")
    private String projectNo;

    /**
     * 类型;1：一般项目 2:重要项目 3:关键项目
     */
    @ExcelProperty(value = "类型;1：一般项目 2:重要项目 3:关键项目", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "project_type")
    private String type;

    /**
     * 项目名称
     */
    @ExcelProperty(value = "项目名称")
    private String name;

    /**
     * DC总容量(MWp)
     */
    @ExcelProperty(value = "DC总容量(MWp)")
    private String dc;

    /**
     * 客户类型;1：总包 2:开发 3:投资 4:同行 5:其它
     */
    @ExcelProperty(value = "客户类型;1：总包 2:开发 3:投资 4:同行 5:其它", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "customer_type")
    private String customerType;

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
     * 支架类型;1：固定 2: 跟踪 3:车棚 4：固定+跟踪 5:固定+车棚 6:跟踪+车棚 7:固定+跟踪+车棚 8:其它
     */
    @ExcelProperty(value = "支架类型;1：固定 2: 跟踪 3:车棚 4：固定+跟踪 5:固定+车棚 6:跟踪+车棚 7:固定+跟踪+车棚 8:其它", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "support_type")
    private String supportType;

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
     * 项目负责人
     */
    @ExcelProperty(value = "项目负责人")
    private Long manager;

    /**
     * 项目内容描述
     */
    @ExcelProperty(value = "项目内容描述")
    private String description;

    /**
     * 状态;0:待执行 1:执行中 2:已完成
     */
    @ExcelProperty(value = "状态;0:待执行 1:执行中 2:已完成")
    private Long status;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @ExcelProperty(value = "更新时间")
    private Date updateTime;


    private String relaFileInfo;


    private Long createBy;

    /**
     * 更新者
     */
    private Long updateBy;

}
