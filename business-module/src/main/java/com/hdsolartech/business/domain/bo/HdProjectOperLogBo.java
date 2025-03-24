package com.hdsolartech.business.domain.bo;

import com.hdsolartech.business.domain.HdProjectOperLog;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 项目操作信息业务对象 hd_project_oper_log
 *
 * @author Lion Li
 * @date 2024-06-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HdProjectOperLog.class, reverseConvertGenerate = false)
public class HdProjectOperLogBo extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 分类
     */
    @NotNull(message = "分类不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long category;

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
     * 操作类别
     */
    @NotBlank(message = "操作类别不能为空", groups = { AddGroup.class, EditGroup.class })
    private String operatorType;

    /**
     * 执行结果
     */
    @NotNull(message = "执行结果不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long result;

    /**
     * 说明
     */
    @NotBlank(message = "说明不能为空", groups = { AddGroup.class, EditGroup.class })
    private String description;


}
