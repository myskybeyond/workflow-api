package com.hdsolartech.business.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 项目文件信息对象 hd_project_file_info
 *
 * @author Lion Li
 * @date 2024-06-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hd_project_file_info")
public class HdProjectFileInfo extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 文件分类ID
     */
    private Long categoryId;

    /**
     * 文件名称
     */
    private String name;

    /**
     * 最新版本
     */
    private Long version;

    /**
     * 下载地址
     */
    private String downloadUrl;

    /**
     * 是否在线;0:否 1:是
     */
    private Long isOnline;

    /**
     * ACT_RU_VARIABLE表主键
     */
    private String actRuVariableId;

    /**
     * 流程实例ID
     */
    private String procInstId;

    /**
     * 参数执行ID
     */
    private String executionId;

    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 资源ID
     */
    private String bytearrayId;

    /**
     * 说明
     */
    private String description;

    /**
     * 在线文件id
     */
    private Long onlineFileId;

    /**
     * 删除标志;0：存在 2：删除
     */
    @TableLogic
    private String delFlag;

    /**
     * oss文件id
     */
    private Long  ossFileId;

}
