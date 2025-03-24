package com.hdsolartech.business.domain.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hdsolartech.business.domain.HdProjectProgressInfo;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

import java.util.Date;

/**
 * 项目进度信息业务对象 hd_project_progress_info
 *
 * @author Lion Li
 * @date 2024-06-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HdProjectProgressInfo.class, reverseConvertGenerate = false)
public class HdProjectProgressInfoBo extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 项目ID
     */
    @NotNull(message = "项目ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long projectId;

    /**
     * 订单ID
     */
    @NotNull(message = "订单ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long orderId;

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 是否同步;0:未同步 1:已同步
     */
    private Long isSync;

    /**
     * 项目文件信息表主键
     */
    private Long projectFileId;
    /**
     * 备注信息
     */
    private String remark;


    /**
     * 最新版本
     */
    private Long version;

    /**
     * 统计列
     */
    private String col;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date orderDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date deliverEndDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date deliverStartDate;

}
