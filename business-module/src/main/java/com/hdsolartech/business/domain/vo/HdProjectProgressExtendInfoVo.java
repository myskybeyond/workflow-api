package com.hdsolartech.business.domain.vo;

import com.hdsolartech.business.domain.HdProjectProgressExtendInfo;
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
 * 项目进度扩展信息视图对象 hd_project_progress_extend_info
 *
 * @author Lion Li
 * @date 2024-06-29
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HdProjectProgressExtendInfo.class)
public class HdProjectProgressExtendInfoVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 关联进度ID
     */
    @ExcelProperty(value = "关联进度ID")
    private Long projectProgressId;

    /**
     * 到货进度
     */
    @ExcelProperty(value = "到货进度")
    private String arrival;

    /**
     * 生产进度
     */
    @ExcelProperty(value = "生产进度")
    private String production;

    /**
     * 镀锌进度 黑件
     */
    @ExcelProperty(value = "镀锌进度 黑件")
    private String black;

    /**
     * 镀锌进度 白件
     */
    @ExcelProperty(value = "镀锌进度 白件")
    private String white;

    /**
     * 发货进度
     */
    @ExcelProperty(value = "发货进度")
    private String delivery;


}
