package com.hdsolartech.business.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.hdsolartech.business.domain.HdSmsTemplateInfo;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;


/**
 * 消息模板信息视图对象 hd_sms_template_info
 *
 * @author Lion Li
 * @date 2024-04-30
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HdSmsTemplateInfo.class)
public class HdSmsTemplateInfoVo implements Serializable {

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
     * 模板编码
     */
    @ExcelProperty(value = "模板编码")
    private String code;

    /**
     * 模板内容
     */
    @ExcelProperty(value = "模板内容")
    private String content;

    /**
     * 状态;0:正常 1:停用
     */
    @ExcelProperty(value = "状态;0:正常 1:停用")
    private Long status;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

    private Date createTime;
    private Date updateTime;


}
