package com.myskybeyond.business.domain.bo;

import com.myskybeyond.business.domain.HdAppInfo;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 应用信息业务对象 hd_app_info
 *
 * @author Lion Li
 * @date 2024-06-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HdAppInfo.class, reverseConvertGenerate = false)
public class HdAppInfoBo extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 应用名称
     */
    @NotBlank(message = "应用名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 应用类型
     */
    @NotBlank(message = "应用类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String appType;

    /**
     * 客户端编号
     */
    @NotBlank(message = "客户端编号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String appKey;

    /**
     * 客户端密钥
     */
    @NotBlank(message = "客户端密钥不能为空", groups = { AddGroup.class, EditGroup.class })
    private String appSecret;

    /**
     * AGENT_ID
     */
    @NotBlank(message = "AGENT_ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private String agentId;

    /**
     * 状态;0:正常 1:停用
     */
    @NotNull(message = "状态;0:正常 1:停用不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long status;
    @NotNull(message = "应用编码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String appCode;
    @NotNull(message = "排序不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer sort;
}
