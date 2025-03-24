package com.hdsolartech.business.domain.vo;

import com.hdsolartech.business.domain.HdProjectAuthority;
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
 * 项目权限信息视图对象 hd_project_authority
 *
 * @author Lion Li
 * @date 2024-06-03
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HdProjectAuthority.class)
public class HdProjectAuthorityVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 项目ID
     */
    @ExcelProperty(value = "项目ID")
    private Long projectId;

    /**
     * 用户ID;1：一般项目 2:重要项目 3:关键项目
     */
    @ExcelProperty(value = "用户ID;1：一般项目 2:重要项目 3:关键项目")
    private Long userId;


}
