package com.myskybeyond.business.domain.bo;

import com.myskybeyond.business.domain.HdCategoryInfo;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 系统类别信息业务对象 hd_category_info
 *
 * @author Lion Li
 * @date 2024-05-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HdCategoryInfo.class, reverseConvertGenerate = false)
public class HdCategoryInfoBo extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 分类;标准  01：跟踪 02:固定 03：电气 04：车棚
非标  11：跟踪 12:固定 13：电气 14：车棚
项目资料 90：项目文件类别
     */
    @NotBlank(message = "分类;", groups = { AddGroup.class, EditGroup.class })
    private String category;

    /**
     * 父ID
     */
    @NotNull(message = "上级分类不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long parentId;

    /**
     * 分类名称
     */
    @NotBlank(message = "分类名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 分类编码
     */
    @NotBlank(message = "分类编码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String code;

    /**
     * 排序;数值越大，排序越靠前
     */
    @NotNull(message = "排序不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long sort;

    /**
     * 备注;0:未读 1:已读
     */
    private String remark;


}
