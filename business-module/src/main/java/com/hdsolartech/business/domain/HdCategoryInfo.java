package com.hdsolartech.business.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 系统类别信息对象 hd_category_info
 *
 * @author Lion Li
 * @date 2024-05-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hd_category_info")
public class HdCategoryInfo extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 分类;标准  01：跟踪 02:固定 03：电气 04：车棚
非标  11：跟踪 12:固定 13：电气 14：车棚
项目资料 90：项目文件类别
     */
    private String category;

    /**
     * 父ID
     */
    private Long parentId;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 分类编码
     */
    private String code;

    /**
     * 排序;数值越大，排序越靠前
     */
    private Long sort;

    /**
     * 备注;0:未读 1:已读
     */
    private String remark;

    /**
     * 删除标志;0：存在 2：删除
     */
    @TableLogic
    private String delFlag;

    /**
     * 祖级列表
     */
    private String ancestors;


}
