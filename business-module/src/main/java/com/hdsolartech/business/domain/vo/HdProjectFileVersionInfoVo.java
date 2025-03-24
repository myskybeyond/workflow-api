package com.hdsolartech.business.domain.vo;

import com.hdsolartech.business.domain.HdProjectFileVersionInfo;
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
 * 项目文件版本信息视图对象 hd_project_file_version_info
 *
 * @author Lion Li
 * @date 2024-06-03
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HdProjectFileVersionInfo.class)
public class HdProjectFileVersionInfoVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 项目文件信息主键;1：项目日志 2:销售订单日志
     */
    @ExcelProperty(value = "项目文件信息主键;1：项目日志 2:销售订单日志")
    private Long projectFileId;

    /**
     * 文件名称
     */
    @ExcelProperty(value = "文件名称")
    private String name;

    /**
     * 下载地址
     */
    @ExcelProperty(value = "下载地址")
    private String downloadUrl;

    /**
     * 是否在线;0:否 1:是
     */
    @ExcelProperty(value = "是否在线;0:否 1:是")
    private Long isOnline;

    /**
     * 执行结果;0:成功 1:失败
     */
    @ExcelProperty(value = "执行结果;0:成功 1:失败")
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

    /**
     * 当前版本
     */
    private Long version;


    /**
     * 是否同步;0:未同步 1:已同步
     */
    private Long isSync;

}
