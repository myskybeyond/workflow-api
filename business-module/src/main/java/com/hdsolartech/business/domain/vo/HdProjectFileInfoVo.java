package com.hdsolartech.business.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.hdsolartech.business.domain.HdProjectFileInfo;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;



/**
 * 项目文件信息视图对象 hd_project_file_info
 *
 * @author Lion Li
 * @date 2024-06-03
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HdProjectFileInfo.class)
public class HdProjectFileInfoVo implements Serializable {

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
     * 文件分类ID
     */
    @ExcelProperty(value = "文件分类ID")
    private Long categoryId;

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
     * ACT_RU_VARIABLE表主键
     */
    @ExcelProperty(value = "ACT_RU_VARIABLE表主键")
    private String actRuVariableId;

    /**
     * 流程实例ID
     */
    @ExcelProperty(value = "流程实例ID")
    private String procInstId;

    /**
     * 参数执行ID
     */
    @ExcelProperty(value = "参数执行ID")
    private String executionId;

    /**
     * 任务ID
     */
    @ExcelProperty(value = "任务ID")
    private String taskId;

    /**
     * 资源ID
     */
    @ExcelProperty(value = "资源ID")
    private String bytearrayId;

    /**
     * 说明
     */
    @ExcelProperty(value = "说明")
    private String description;

    /**
     * 在线文件id
     */
    @ExcelProperty(value = "在线文件id")
    private Long onlineFileId;

    /**
     * 最新版本
     */
    private Long version;


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
     * oss文件id
     */
    private Long  ossFileId;




}
