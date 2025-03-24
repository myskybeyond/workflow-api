package com.hdsolartech.business.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 订单流程关系对象 hd_order_flw_relation
 *
 * @author Lion Li
 * @date 2024-06-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hd_order_flw_relation")
public class HdOrderFlwRelation extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 1:下单
     */
    private Long type;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 任务D
     */
    private String actHiTaskinstId;

    /**
     * 流程实例ID
     */
    private String procInstId;

    /**
     * 流程定义ID
     */
    private String procDefId;

    /**
     * 删除标志;0：存在 2：删除
     */
    @TableLogic
    private String delFlag;


}
