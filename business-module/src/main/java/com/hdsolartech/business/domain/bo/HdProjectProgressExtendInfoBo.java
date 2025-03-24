package com.hdsolartech.business.domain.bo;

import com.hdsolartech.business.domain.HdProjectProgressExtendInfo;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 项目进度扩展信息业务对象 hd_project_progress_extend_info
 *
 * @author Lion Li
 * @date 2024-06-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HdProjectProgressExtendInfo.class, reverseConvertGenerate = false)
public class HdProjectProgressExtendInfoBo extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

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


}
