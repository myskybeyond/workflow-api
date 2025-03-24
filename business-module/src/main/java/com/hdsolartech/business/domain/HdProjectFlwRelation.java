package com.hdsolartech.business.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 项目流程关系对象 hd_project_flw_relation
 *
 * @author Lion Li
 * @date 2024-06-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hd_project_flw_relation")
public class HdProjectFlwRelation extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 1:报价
     */
    private Long type;

    /**
     * 项目ID
     */
    private Long projectId;

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
