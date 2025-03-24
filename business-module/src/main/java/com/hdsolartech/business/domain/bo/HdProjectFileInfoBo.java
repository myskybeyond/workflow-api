package com.hdsolartech.business.domain.bo;

import com.hdsolartech.business.domain.HdProjectFileInfo;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

import java.util.Date;

/**
 * 项目文件信息业务对象 hd_project_file_info
 *
 * @author Lion Li
 * @date 2024-06-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HdProjectFileInfo.class, reverseConvertGenerate = false)
public class HdProjectFileInfoBo extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
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
     * 下载地址
     */
    private String downloadUrl;

    /**
     * 是否在线;0:否 1:是
     */
    @NotNull(message = "是否在线;0:否 1:是不能为空", groups = { AddGroup.class, EditGroup.class })
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
     * 更新者
     */
    private Long updateBy;


    private Long version;

}
