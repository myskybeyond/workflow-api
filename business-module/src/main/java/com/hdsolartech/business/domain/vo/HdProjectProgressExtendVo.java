package com.hdsolartech.business.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;


/**
 * 项目进度扩展信息视图对象 hd_project_progress_extend_info
 *
 * @author Lion Li
 * @date 2024-06-29
 */
@Data
public class HdProjectProgressExtendVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

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
     * 项目id
     */
    private Long projectId;

    /**
     * 订单ids
     */
    private Long orderId;


    private Date orderDate;

    private Date deliverEndDate;

    private Date deliverStartDate;

}
