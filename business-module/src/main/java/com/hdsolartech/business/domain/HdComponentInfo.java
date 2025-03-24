package com.hdsolartech.business.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

import java.io.Serial;

/**
 * 构件信息对象 hd_component_info
 *
 * @author Lion Li
 * @date 2024-05-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hd_component_info")
public class HdComponentInfo extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 分类
     */
    private String category;

    /**
     * 所属分类
     */
    private Long categoryId;

    /**
     * 父ID
     */
    private Long parentId;

    /**
     * 品名
     */
    private String name;

    /**
     * 物料号
     */
    private String partNo;

    /**
     * 型号
     */
    private String type;

    /**
     * 图号
     */
    private String drgNo;

    /**
     * 材料
     */
    private String material;

    /**
     * 规格
     */
    private String spec;

    /**
     * 理论尺寸
     */
    private String theoreticalSize;

    /**
     * 物料尺寸
     */
    private String materialSize;

    /**
     * 数量
     */
    private Long num;

    /**
     * 单位
     */
    private String unit;

    /**
     * 长度（mm）
     */
    private String length;

    /**
     * 米重(kg/m）
     */
    private String meterWeight;

    /**
     * 单重(kg)
     */
    private String weight;

    /**
     * 模具编号
     */
    private String mouldNo;

    /**
     * 开条尺寸
     */
    private String stripSize;

    /**
     * 卷宽
     */
    private String seamWidth;

    /**
     * 备注
     */
    private String remark;

    /**
     * 说明
     */
    private String description;

    /**
     * 成品完工物料号
     */
    private String finishPartNo;

    /**
     * 黑件完工物料号
     */
    private String blackPartNo;

    /**
     * 外协完工物料号
     */
    private String outPartNo;

    /**
     * 生产工艺
     */
    private String manufactureProcess;

    /**
     * 状态
     */
    private Long status;

    /**
     * 排序
     */
    private Long sort;

    /**
     * 删除标志;0：存在 2：删除
     */
    @TableLogic
    private String delFlag;

    /**
     * 关联文件信息
     */
    private String relaFileInfo;

}
