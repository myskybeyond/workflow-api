package com.myskybeyond.flowable.core;

import lombok.Data;

/**
 * @author MySkyBeyond
 * @version 1.0
 * Brief description of the class.
 * @date 2024/5/16
 */
@Data
public class OptionOfFormCreate {

    private FormObjOfFormCreate form;

    private BtnObj submitBtn;
    private BtnObj resetBtn;
}

