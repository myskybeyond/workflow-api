package com.myskybeyond.business.domain.bo;

import com.myskybeyond.business.domain.HdProjectFileVersionInfo;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;

/**
 * 项目文件版本信息业务对象 hd_project_file_version_info
 *
 * @author Lion Li
 * @date 2024-06-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HdProjectFileVersionInfo.class, reverseConvertGenerate = false)
public class HdProjectFileVersionInfoBo extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 项目文件信息主键;1：项目日志 2:销售订单日志
     */
    @NotNull(message = "项目文件信息主键;1：项目日志 2:销售订单日志不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long projectFileId;

    /**
     * 文件名称
     */
    @NotBlank(message = "文件名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 下载地址
     */
    @NotBlank(message = "下载地址不能为空", groups = { AddGroup.class, EditGroup.class })
    private String downloadUrl;

    /**
     * 是否在线;0:否 1:是
     */
    @NotNull(message = "是否在线;0:否 1:是不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long isOnline;

    /**
     * 执行结果;0:成功 1:失败
     */
    @NotNull(message = "执行结果;0:成功 1:失败不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long result;

    /**
     * 说明
     */
    @NotBlank(message = "说明不能为空", groups = { AddGroup.class, EditGroup.class })
    private String description;

    private Long version;
}
