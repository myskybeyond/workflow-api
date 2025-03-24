package com.hdsolartech.business.domain.bo;

import com.hdsolartech.business.domain.HdQuotationStatistics;
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
 * 项目报价统计信息业务对象 hd_quotation_statistics
 *
 * @author Lion Li
 * @date 2024-06-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HdQuotationStatistics.class, reverseConvertGenerate = false)
public class HdQuotationStatisticsBo extends BaseEntity {

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
     * 项目类型
     */
    @NotBlank(message = "项目类型不能为空", groups = { AddGroup.class, EditGroup.class })
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
    @NotNull(message = "统计日期;固定、跟踪、电气、车棚不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date statisticsDate;


}
