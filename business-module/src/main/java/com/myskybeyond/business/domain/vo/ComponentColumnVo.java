package com.myskybeyond.business.domain.vo;

import lombok.Data;

/**
 * @ClassName ComponentCloumVo
 * @Description TODO
 * @Author zyf
 * @create 2024/5/31 16:31
 * @Version 1.0
 */
@Data
public class ComponentColumnVo {

    /**
     * 类型
     */
    private String category;

    /**
     * 获取的column
     */
    private String column;
}
