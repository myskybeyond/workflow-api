package com.hdsolartech.business.domain.bo;

import com.hdsolartech.business.domain.HdProjectProgressStatistics;
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
 * 项目进度统计信息业务对象 hd_project_progress_statistics
 *
 * @author Lion Li
 * @date 2024-06-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HdProjectProgressStatistics.class, reverseConvertGenerate = false)
public class HdProjectProgressStatisticsBo extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 项目ID
     */
    @NotNull(message = "项目ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long projectId;

    /**
     * 订单ID
     */
    @NotNull(message = "订单ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long orderId;

    /**
     * 关联进度ID
     */
    @NotNull(message = "关联进度ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long projectProgressId;

    /**
     * 到货进度
     */
    @NotBlank(message = "到货进度不能为空", groups = { AddGroup.class, EditGroup.class })
    private String arrival;

    /**
     * 生产进度
     */
    @NotBlank(message = "生产进度不能为空", groups = { AddGroup.class, EditGroup.class })
    private String production;

    /**
     * 镀锌进度 黑件
     */
    @NotBlank(message = "镀锌进度 黑件不能为空", groups = { AddGroup.class, EditGroup.class })
    private String black;

    /**
     * 镀锌进度 白件
     */
    @NotBlank(message = "镀锌进度 白件不能为空", groups = { AddGroup.class, EditGroup.class })
    private String white;

    /**
     * 发货进度
     */
    @NotBlank(message = "发货进度不能为空", groups = { AddGroup.class, EditGroup.class })
    private String delivery;

    /**
     * 下单日期
     */
    @NotNull(message = "下单日期不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date orderDate;

    /**
     * 发货开始时间
     */
    @NotNull(message = "发货开始时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date deliverStartDate;

    /**
     * 发货结束时间
     */
    @NotNull(message = "发货结束时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date deliverEndDate;

    /**
     * 统计日期;固定、跟踪、电气、车棚
     */
    @NotNull(message = "统计日期;固定、跟踪、电气、车棚不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date statisticsDate;


}
