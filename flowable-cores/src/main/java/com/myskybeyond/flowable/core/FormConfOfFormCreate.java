package com.myskybeyond.flowable.core;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author MySkyBeyond
 * @version 1.0
 * Brief description of the class.
 * @date 2024/5/16
 */
@Data
public class FormConfOfFormCreate {

    private String title;

    private OptionOfFormCreate option;

    /**
     * 表单项
     */
    private List<Map<String, Object>> rule;
}
