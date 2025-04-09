package com.myskybeyond.business.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.tenant.core.TenantEntity;

import java.io.Serial;
import java.util.Date;

/**
 * 销售订单信息对象 hd_order_info
 *
 * @author Lion Li
 * @date 2024-06-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hd_order_info")
public class HdOrderInfo extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 项目id
     */
    private Long projectId;

    /**
     * 销售订单编号
     */
    private String orderNo;

    /**
     * 销售订单名称
     */
    private String name;

    /**
     * 采购单位
     */
    private String purchaseUnit;

    /**
     * 业主单位
     */
    private String ownerUnit;

    /**
     * 项目地点
     */
    private String location;

    /**
     * 联系人
     */
    private String contactPerson;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 邮箱
     */
    private String contactEmail;

    /**
     * 其它
     */
    private String other;

    /**
     * DC总容量(MWp)
     */
    private String dc;

    /**
     * 销售订单描述
     */
    private String description;

    /**
     * 支架类型;1：固定 2: 跟踪 3:车棚
     */
    private String supportType;

    /**
     * 状态;0:待执行 1:执行中 2:已完成
     */
    private Long status;

    /**
     * 开始日期
     */
    private Date startTime;

    /**
     * 预计结束日期
     */
    private Date endTime;

    /**
     * 销售订单负责人
     */
    private Long manager;

    /**
     * 进度
     */
    private String progress;

    /**
     * 删除标志;0：存在 2：删除
     */
    @TableLogic
    private String delFlag;

    /**
     * 关联文件信息  需要查询文件信息
     */
    private String relaFileInfo;
}
