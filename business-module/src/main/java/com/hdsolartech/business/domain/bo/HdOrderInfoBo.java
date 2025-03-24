package com.hdsolartech.business.domain.bo;

import com.hdsolartech.business.domain.HdOrderInfo;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 销售订单信息业务对象 hd_order_info
 *
 * @author Lion Li
 * @date 2024-06-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HdOrderInfo.class, reverseConvertGenerate = false)
public class HdOrderInfoBo extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 项目id
     */
    @NotNull(message = "项目id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long projectId;

    /**
     * 销售订单编号
     */
    @NotBlank(message = "销售订单编号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String orderNo;

    /**
     * 销售订单名称
     */
    @NotBlank(message = "销售订单名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 项目名称
     */
    private String projectName;


    /**
     * 项目编号
     */
    private String projectNo;

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
    @NotBlank(message = "支架类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String supportType;

    /**
     * 状态;0:待执行 1:执行中 2:已完成
     */
    private Long status;

    /**
     * 开始日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startTime;

    /**
     * 预计结束日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
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
     * 关联文件信息  需要查询文件信息
     */
    private String relaFileInfo;

    /**
     * 状态数组
     */
    private Long[] statusArr;

}
