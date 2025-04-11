package com.myskybeyond.web.enums;

/**
 * @author 86137
 * @version 1.0
 * <p>
 * Brief description of the class.
 * @date 2025/1/15
 */
public enum SocialEnum {

    DINGTALK("DINGTALK","钉钉"),WECHAT_ENTERPRISE("WECHAT_ENTERPRISE","企业微信");

    private String code;
    private String name;

    private SocialEnum(String code, String name){
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
