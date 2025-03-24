package com.myskybeyond.flowable.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import lombok.Data;



/**
 * 流程定义视图对象 workflow_definition
 *
 * @author KonBAI
 * @date 2022-01-17
 */
@Data
@ExcelIgnoreUnannotated
public class WfDefinitionVo extends AbstractWfDeployVo {

    private static final long serialVersionUID = 1L;
}
