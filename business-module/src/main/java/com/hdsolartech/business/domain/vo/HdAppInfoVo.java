package com.hdsolartech.business.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hdsolartech.business.domain.HdAppInfo;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;



/**
 * 应用信息视图对象 hd_app_info
 *
 * @author Lion Li
 * @date 2024-06-04
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HdAppInfo.class)
public class HdAppInfoVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 应用名称
     */
    @ExcelProperty(value = "应用名称")
    private String name;

    /**
     * 应用类型
     */
    @ExcelProperty(value = "应用类型")
    private String appType;

    /**
     * 客户端编号
     */
    @ExcelProperty(value = "客户端编号")
    private String appKey;

    /**
     * 客户端密钥
     */
    @ExcelProperty(value = "客户端密钥")
    private String appSecret;

    /**
     * AGENT_ID
     */
    @ExcelProperty(value = "AGENT_ID")
    private String agentId;

    /**
     * 状态;0:正常 1:停用
     */
    @ExcelProperty(value = "状态;0:正常 1:停用")
    private Long status;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

    private String appCode;

    private Integer sort;

}
