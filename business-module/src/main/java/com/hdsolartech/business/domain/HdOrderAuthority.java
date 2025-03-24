package com.hdsolartech.business.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 订单权限信息对象 hd_order_authority
 *
 * @author Lion Li
 * @date 2024-06-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hd_order_authority")
public class HdOrderAuthority extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
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
     * 用户ID;1：一般项目 2:重要项目 3:关键项目
     */
    private Long userId;

    /**
     * 删除标志;0：存在 2：删除
     */
    @TableLogic
    private String delFlag;


}
