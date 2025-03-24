package com.hdsolartech.business.domain.bo;

import com.hdsolartech.business.domain.HdOrderAuthority;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 订单权限信息业务对象 hd_order_authority
 *
 * @author Lion Li
 * @date 2024-06-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HdOrderAuthority.class, reverseConvertGenerate = false)
public class HdOrderAuthorityBo extends BaseEntity {

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
     * 用户ID;1：一般项目 2:重要项目 3:关键项目
     */
    @NotNull(message = "用户ID;1：一般项目 2:重要项目 3:关键项目不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long userId;


}
