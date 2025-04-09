package com.myskybeyond.business.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.tenant.core.TenantEntity;

import java.io.Serial;

/**
 * 项目资料模板对象 hd_template_info
 *
 * @author Lion Li
 * @date 2024-05-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hd_template_info")
public class HdTemplateInfo extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 模板名称
     */
    private String name;

    /**
     * 说明
     */
    private String description;

    /**
     * 类型;0:报价 1:下单
     */
    private Long type;

    /**
     * 分类;固定、跟踪、电气、车棚
     */
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

    /**
     * 删除标志;0：存在 2：删除
     */
    @TableLogic
    private String delFlag;


}
