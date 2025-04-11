package com.myskybeyond.web.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author MySkyBeyond
 * @version 1.0
 * Brief description of the class.
 * @date 2024/6/25
 */
@Data
@AllArgsConstructor
public class AppVo {

    private Long id;

    private String appCode;

    private String appName;

    private String appKey;

    private String appType;

    private String agentId;
}
