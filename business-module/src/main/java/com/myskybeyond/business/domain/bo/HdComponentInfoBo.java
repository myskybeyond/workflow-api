package com.myskybeyond.business.domain.bo;

import com.myskybeyond.business.domain.HdComponentInfo;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;

import java.util.List;

/**
 * 构件信息业务对象 hd_component_info
 *
 * @author Lion Li
 * @date 2024-05-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HdComponentInfo.class, reverseConvertGenerate = false)
public class HdComponentInfoBo extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 分类
     */
    @NotBlank(message = "分类不能为空", groups = { AddGroup.class, EditGroup.class })
    private String category;

    /**
     * 所属分类
     */
    @NotNull(message = "所属分类不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long categoryId;

    /**
     * 父ID
     */
    @NotNull(message = "上级组件不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long parentId;

    /**
     * 品名
     */
    @NotNull(message = "品名不能为空", groups = { AddGroup.class, EditGroup.class })
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
    @NotNull(message = "排序不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long sort;

    /**
     * 关联文件信息
     */
    private String relaFileInfo;


    private List<Long> categoryIds;

}
