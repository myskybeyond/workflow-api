package com.myskybeyond.business.domain.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.myskybeyond.business.domain.HdProjectInfo;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;

import java.util.Date;

/**
 * 项目信息业务对象 hd_project_info
 *
 * @author Lion Li
 * @date 2024-06-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HdProjectInfo.class, reverseConvertGenerate = false)
public class HdProjectInfoBo extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 项目编号
     */
    @NotBlank(message = "项目编号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String projectNo;

    /**
     * 类型;1：一般项目 2:重要项目 3:关键项目
     */
    @NotBlank(message = "类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String type;

    /**
     * 项目名称
     */
    @NotBlank(message = "项目名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * DC总容量(MWp)
     */
    private String dc;

    /**
     * 客户类型;1：总包 2:开发 3:投资 4:同行 5:其它
     */
    @NotBlank(message = "客户类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String customerType;

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
    @NotBlank(message = "联系人不能为空", groups = { AddGroup.class, EditGroup.class })
    private String contactPerson;

    /**
     * 联系电话
     */
    @NotBlank(message = "联系电话不能为空", groups = { AddGroup.class, EditGroup.class })
    private String contactPhone;

    /**
     * 邮箱
     */
    @NotBlank(message = "邮箱不能为空", groups = { AddGroup.class, EditGroup.class })
    private String contactEmail;

    /**
     * 其它
     */
    private String other;

    /**
     * 支架类型;1：固定 2: 跟踪 3:车棚 4：固定+跟踪 5:固定+车棚 6:跟踪+车棚 7:固定+跟踪+车棚 8:其它
     */
    @NotBlank(message = "支架类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String supportType;

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
     * 项目负责人
     */
    private Long manager;

    /**
     * 项目内容描述
     */
    private String description;

    /**
     * 状态;0:待执行 1:执行中 2:已完成
     */
    @NotNull(message = "状态不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long status;


    /**
     * 关联文件信息  需要查询文件信息
     */
    private String relaFileInfo;
    /**
     * 最新版本
     */
    private Long version;

    /**
     * 状态数组
     */
    private Long[] statusArr;
}
