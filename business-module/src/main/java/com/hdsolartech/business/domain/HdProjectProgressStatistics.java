package com.hdsolartech.business.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serial;

/**
 * 项目进度统计信息对象 hd_project_progress_statistics
 *
 * @author Lion Li
 * @date 2024-06-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hd_project_progress_statistics")
public class HdProjectProgressStatistics extends TenantEntity {

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
     * 关联进度ID
     */
    private Long projectProgressId;

    /**
     * 到货进度
     */
    private String arrival;

    /**
     * 生产进度
     */
    private String production;

    /**
     * 镀锌进度 黑件
     */
    private String black;

    /**
     * 镀锌进度 白件
     */
    private String white;

    /**
     * 发货进度
     */
    private String delivery;

    /**
     * 下单日期
     */
    private Date orderDate;

    /**
     * 发货开始时间
     */
    private Date deliverStartDate;

    /**
     * 发货结束时间
     */
    private Date deliverEndDate;

    /**
     * 统计日期;固定、跟踪、电气、车棚
     */
    private Date statisticsDate;

    /**
     * 删除标志;0：存在 2：删除
     */
    @TableLogic
    private String delFlag;


}
