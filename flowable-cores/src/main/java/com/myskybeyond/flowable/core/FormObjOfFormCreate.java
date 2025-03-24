package com.myskybeyond.flowable.core;

import lombok.Data;

/**
 * @author MySkyBeyond
 * @version 1.0
 * Brief description of the class.
 * @date 2024/5/16
 */
@Data
public class FormObjOfFormCreate {
    /**
     * 标题
     */
    private String title;
    /**
     * 表单尺寸
     */
    private String size;
    /**
     * 标签对齐
     */
    private String labelPosition;
    /**
     * 标签宽度
     */
    private String labelWidth;
    private Boolean hideRequiredAsterisk;

    private Boolean showMessage;

    private Boolean inlineMessage;

}
