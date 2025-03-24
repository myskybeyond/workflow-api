package com.hdsolartech.business.domain.vo;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.hdsolartech.business.domain.HdComponentInfo;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import org.dromara.common.excel.convert.ExcelFormulaConvert;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 构件信息视图对象 hd_component_info
 *
 * @author Lion Li
 * @date 2024-05-29
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HdComponentInfo.class)
public class HdComponentInfoVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    private Long id;

    /**
     * 分类信息
     */
    private String category;
    /**
     * 所属分类
     */
   // @ExcelProperty(value = "所属分类")
    private Long categoryId;

    @ExcelProperty(value = "分类编码")
    private String categoryCode;

   // @ColumnWidth(200)
    @ExcelProperty(value = "所属分类")
    private String categoryName;

    /**
     * 父ID
     */
   // @ExcelProperty(value = "上级构件")
    private Long parentId;

   // @ColumnWidth(200)
    @ExcelProperty(value = "上级构件")
    private String parentName;

    /**
     * 物料号
     */
   // @ColumnWidth(200)
    @ExcelProperty(value = "料号")
    private String partNo;
    /**
     * 品名
     */
    @ExcelProperty(value = "品名")
    private String name;



    /**
     * 型号
     */
    @ExcelProperty(value = "型号")
    private String type;

    /**
     * 图号
     */
    @ExcelProperty(value = "图号")
    private String drgNo;

    /**
     * 材料
     */
    @ExcelProperty(value = "材料")
    private String material;

    /**
     * 规格
     */
    @ExcelProperty(value = "规格")
    private String spec;


    /**
     * 数量
     */
    @ExcelProperty(value = "数量")
    private Long num;

    /**
     * 单位
     */
    @ExcelProperty(value = "单位")
    private String unit;

    /**
     * 长度（mm）
     */
    @ExcelProperty(value = "长度")
    private String length;

    /**
     * 米重(kg/m）
     */
    @ExcelProperty(value = "米重(kg/m)")
    private String meterWeight;

    /**
     * 单重(kg)
     */
    @ExcelProperty(value = "单重(kg)",converter = ExcelFormulaConvert.class )
    private String weight;

    /**
     * 模具编号
     */
    @ExcelProperty(value = "模具编号")
    private String mouldNo;

    /**
     * 开条尺寸
     */
    @ExcelProperty(value = "开条尺寸")
    private String stripSize;

    /**
     * 卷宽
     */
    @ExcelProperty(value = "卷宽")
    private String seamWidth;

    /**
     * 理论尺寸
     */
    @ExcelProperty(value = "理论尺寸")
    private String theoreticalSize;

    /**
     * 物料尺寸
     */
    @ExcelProperty(value = "物料尺寸")
    private String materialSize;


    /**
     * 成品完工物料号
     */
    @ExcelProperty(value = "成品完工物料号")
    private String finishPartNo;

    /**
     * 黑件完工物料号
     */
    @ExcelProperty(value = "黑件完工物料号")
    private String blackPartNo;

    /**
     * 外协完工物料号
     */
    @ExcelProperty(value = "外协完工物料号")
    private String outPartNo;

    /**
     * 生产工艺
     */
    @ExcelProperty(value = "生产工艺",converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "mfg_process")
    private String manufactureProcess;

    /**
     * 状态
     */
    //@ExcelProperty(value = "状态")
    private Long status;

    /**
     * 排序
     */
    @ExcelProperty(value = "排序")
    private Long sort;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

    /**
     * 说明
     */
    @ExcelProperty(value = "说明")
    private String description;
    /**
     * 关联文件信息
     */
    private String relaFileInfo;



    public Boolean checkParams(){
        if(StrUtil.isEmpty(this.categoryCode)){
            return Boolean.FALSE;
        }
//        if(StrUtil.isEmpty(this.parentName)){
//            return Boolean.FALSE;
//        }
        if(StrUtil.isEmpty(this.name)){
            return Boolean.FALSE;
        }
        return Boolean.TRUE;

    }

}
