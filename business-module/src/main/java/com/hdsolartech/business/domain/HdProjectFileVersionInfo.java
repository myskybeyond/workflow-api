package com.hdsolartech.business.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 项目文件版本信息对象 hd_project_file_version_info
 *
 * @author Lion Li
 * @date 2024-06-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hd_project_file_version_info")
public class HdProjectFileVersionInfo extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 项目文件信息主键;1：项目日志 2:销售订单日志
     */
    private Long projectFileId;

    /**
     * 文件名称
     */
    private String name;

    /**
     * 当前版本
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
     * 执行结果;0:成功 1:失败
     */
    private Long result;

    /**
     * 说明
     */
    private String description;

    /**
     * 删除标志;0：存在 2：删除
     */
    @TableLogic
    private String delFlag;

    /**
     * 是否同步;0:未同步 1:已同步
     */
    private Long isSync;

}
