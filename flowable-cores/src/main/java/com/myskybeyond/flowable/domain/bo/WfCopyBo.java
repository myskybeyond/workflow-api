package com.myskybeyond.flowable.domain.bo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;


/**
 * 流程抄送业务对象 wf_copy
 *
 * @author ruoyi
 * @date 2022-05-19
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class WfCopyBo extends BaseEntity {

    /**
     * 抄送主键
     */
    @NotNull(message = "抄送主键不能为空", groups = { EditGroup.class })
    private Long copyId;

    /**
     * 抄送标题
     */
    @NotNull(message = "抄送标题不能为空", groups = { AddGroup.class, EditGroup.class })
    private String title;

    /**
     * 流程主键
     */
    @NotBlank(message = "流程主键不能为空", groups = { AddGroup.class, EditGroup.class })
    private String processId;

    /**
     * 流程名称
     */
    @NotBlank(message = "流程名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String processName;

    /**
     * 流程分类主键
     */
    @NotBlank(message = "流程分类主键不能为空", groups = { AddGroup.class, EditGroup.class })
    private String category;

    /**
     * 任务主键
     */
    @NotBlank(message = "任务主键不能为空", groups = { AddGroup.class, EditGroup.class })
    private String taskId;

    /**
     * 用户主键
     */
    @NotNull(message = "用户主键不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long userId;

    /**
     * 发起人Id
     */
    @NotNull(message = "发起人主键不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long originatorId;
    /**
     * 发起人名称
     */
    @NotNull(message = "发起人名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String originatorName;

    private String processVersion;
    private String copyNodeName;
}
