package com.myskybeyond.flowable.domain.vo;

import lombok.Data;

/**
 * @author MySkyBeyond
 * @version 1.0
 * 业务数据按流程分类统计数量的展示
 * @date 2024/5/20
 */
@Data
public class DataGroupByCategoryVo {
    private String code;
    private Integer total;
}
