package com.hdsolartech.business.domain.vo;

import com.hdsolartech.business.domain.HdTemplateInfo;
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
 * 项目资料模板视图对象 hd_template_info
 *
 * @author Lion Li
 * @date 2024-05-24
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HdTemplateInfo.class)
public class HdTemplateInfoVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 模板名称
     */
    @ExcelProperty(value = "模板名称")
    private String name;

    /**
     * 说明
     */
    @ExcelProperty(value = "说明")
    private String description;

    /**
     * 类型;0:报价 1:下单
     */
    @ExcelProperty(value = "类型;0:报价 1:下单")
    private Long type;

    /**
     * 分类;固定、跟踪、电气、车棚
     */
    @ExcelProperty(value = "分类;固定、跟踪、电气、车棚")
    private String category;

    /**
     * 状态;0:正常 1:停用
     */
    @ExcelProperty(value = "状态;0:正常 1:停用")
    private Long status;

    /**
     * 在线文件id
     */
    @ExcelProperty(value = "在线文件id")
    private Long onlineFileId;


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
     * 版本
     */
    private Integer version;
}
