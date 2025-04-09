package com.myskybeyond.business.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.tenant.core.TenantEntity;

import java.io.Serial;

/**
 * 项目发货信息对象 hd_project_delivery_info
 *
 * @author Lion Li
 * @date 2024-06-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hd_project_delivery_info")
public class HdProjectDeliveryInfo extends TenantEntity {

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
     * 名称
     */
    private String name;

    /**
     * 当前版本
     */
    private Long version;

    /**
     * 是否同步;0:未同步 1:已同步
     */
    private Long isSync;

    /**
     * 项目文件信息表主键
     */
    private Long projectFileId;

    /**
     * 删除标志;0：存在 2：删除
     */
    @TableLogic
    private String delFlag;

    /**
     * 备注信息
     */
    private String remark;

}
