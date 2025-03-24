package com.hdsolartech.business.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serial;

/**
 * 项目报价统计信息对象 hd_quotation_statistics
 *
 * @author Lion Li
 * @date 2024-06-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hd_quotation_statistics")
public class HdQuotationStatistics extends TenantEntity {

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
     * 项目类型
     */
    private String projectType;

    /**
     * 部门ID
     */
    private String deptId;

    /**
     * 发起业务员
     */
    private Long salesman;

    /**
     * 设计员
     */
    private Long designer;

    /**
     * 校对人
     */
    private Long proofreader;

    /**
     * 审核人
     */
    private Long auditer;

    /**
     * 接收日期
     */
    private Date receivedDate;

    /**
     * 预计完成时间
     */
    private Date expectedDate;

    /**
     * 实际完成时间
     */
    private Date actualDate;

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
