package org.dromara.system.domain.vo;

import org.dromara.system.domain.HdUserWf;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;


/**
 * 用户于流程关联
视图对象 hd_user_wf
 *
 * @author Lion Li
 * @date 2024-06-14
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HdUserWf.class)
public class HdUserWfVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @ExcelProperty(value = "用户id")
    private Long userId;

    /**
     * 流程id
     */
    @ExcelProperty(value = "流程id")
    private String wfId;

//    @ExcelProperty(value = "流程id")
    private String deploymentId;

    /**
     * 流程名
     */
    @ExcelProperty(value = "流程名")
    private String wfName;

    /**
     * 流程标识
     */
    @ExcelProperty(value = "流程标识")
    private String processKey;


}
