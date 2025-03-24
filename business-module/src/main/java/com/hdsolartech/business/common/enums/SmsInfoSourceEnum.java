package com.hdsolartech.business.common.enums;

/**
 * @author 86137
 * @version 1.0
 * 消息来源枚举类
 * @date 2024/6/21
 */
public enum SmsInfoSourceEnum {
    flowCall("流程触发"),innerCall("系统内部调用"),othersCall("其他");

    private String source;

    SmsInfoSourceEnum(String source) {
        this.source = source;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
