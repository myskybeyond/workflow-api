package com.myskybeyond.business.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.tenant.core.TenantEntity;

import java.io.Serial;

/**
 * 项目操作信息对象 hd_project_oper_log
 *
 * @author Lion Li
 * @date 2024-06-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hd_project_oper_log")
public class HdProjectOperLog extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 分类
     */
    private Long category;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 操作类别
     */
    private String operatorType;

    /**
     * 执行结果
     */
    private Long result;

    /**
     * 说明
     */
    private String description;

    /**
     * 删除标志
     */
    @TableLogic
    private String delFlag;


}
