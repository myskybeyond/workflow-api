package org.dromara.system.domain.bo;

import org.dromara.system.domain.HdUserWf;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 用户于流程关联
业务对象 hd_user_wf
 *
 * @author Lion Li
 * @date 2024-06-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HdUserWf.class, reverseConvertGenerate = false)
public class HdUserWfBo extends BaseEntity {

    /**
     * 用户id
     */
    @NotNull(message = "用户id不能为空", groups = { EditGroup.class })
    private Long userId;

    /**
     * 流程id
     */
    @NotNull(message = "流程id不能为空", groups = { AddGroup.class, EditGroup.class })
    private String wfId;

    /**
     * 流程名
     */
    @NotBlank(message = "流程名不能为空", groups = { AddGroup.class, EditGroup.class })
    private String wfName;

    /**
     * 流程标识
     */
    @NotBlank(message = "流程标识不能为空", groups = { AddGroup.class, EditGroup.class })
    private String processKey;

    private String deploymentId;

}
