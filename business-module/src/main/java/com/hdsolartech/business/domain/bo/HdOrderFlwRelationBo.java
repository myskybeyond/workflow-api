package com.hdsolartech.business.domain.bo;

import com.hdsolartech.business.domain.HdOrderFlwRelation;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 订单流程关系业务对象 hd_order_flw_relation
 *
 * @author Lion Li
 * @date 2024-06-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HdOrderFlwRelation.class, reverseConvertGenerate = false)
public class HdOrderFlwRelationBo extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = {EditGroup.class})
    private Long id;

    /**
     * 1:下单
     */
    @NotNull(message = "1:下单不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long type;

    /**
     * 项目ID
     */
    @NotNull(message = "项目ID不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long projectId;

    /**
     * 订单ID
     */
    @NotNull(message = "订单ID不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long orderId;

    /**
     * 任务D
     */
    @NotBlank(message = "任务D不能为空", groups = {AddGroup.class, EditGroup.class})
    private String actHiTaskinstId;

    /**
     * 流程实例ID
     */
    @NotBlank(message = "流程实例ID不能为空", groups = {AddGroup.class, EditGroup.class})
    private String procInstId;

    /**
     * 流程定义ID
     */
    @NotBlank(message = "流程定义ID不能为空", groups = {AddGroup.class, EditGroup.class})
    private String procDefId;

    /**
     * 处理人
     */
    private String userName;
    /**
     * 进行状态
     */
    private String status;
}
