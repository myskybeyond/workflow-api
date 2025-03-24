package com.hdsolartech.business.domain.vo;

import com.hdsolartech.business.domain.HdCategoryInfo;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;



/**
 * 系统类别信息视图对象 hd_category_info
 *
 * @author Lion Li
 * @date 2024-05-28
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HdCategoryInfo.class)
public class HdCategoryInfoVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 分类;标准  01：跟踪 02:固定 03：电气 04：车棚
非标  11：跟踪 12:固定 13：电气 14：车棚
项目资料 90：项目文件类别
     */
    @ExcelProperty(value = "分类", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "general_category")
    private String category;

    /**
     * 父ID
     */
    @ExcelProperty(value = "父ID")
    private Long parentId;

    /**
     * 分类名称
     */
    @ExcelProperty(value = "分类名称")
    private String name;

    /**
     * 分类编码
     */
    @ExcelProperty(value = "分类编码")
    private String code;

    /**
     * 排序;数值越大，排序越靠前
     */
    @ExcelProperty(value = "排序;数值越大，排序越靠前")
    private Long sort;

    /**
     * 备注;0:未读 1:已读
     */
    @ExcelProperty(value = "备注;0:未读 1:已读")
    private String remark;


    /**
     * 查询分类下的数量
     */
    private Long  num;


}
