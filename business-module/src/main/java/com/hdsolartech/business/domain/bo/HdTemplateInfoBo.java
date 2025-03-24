package com.hdsolartech.business.domain.bo;

import com.hdsolartech.business.domain.HdTemplateInfo;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 项目资料模板业务对象 hd_template_info
 *
 * @author Lion Li
 * @date 2024-05-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HdTemplateInfo.class, reverseConvertGenerate = false)
public class HdTemplateInfoBo extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 模板名称
     */
    @NotBlank(message = "模板名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 说明
     */
    private String description;

    /**
     * 类型;0:报价 1:下单
     */
    @NotNull(message = "类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long type;

    /**
     * 分类;固定、跟踪、电气、车棚
     */
    @NotBlank(message = "分类不能为空", groups = { AddGroup.class, EditGroup.class })
    private String category;

    /**
     * 状态;0:正常 1:停用
     */
    private Long status;

    /**
     * 在线文件id
     */
    private Long onlineFileId;


    /**
     * 版本
     */
    private Integer version;
}
