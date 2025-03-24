package org.dromara.common.core.domain.vo;

import lombok.Data;

/**
 * @ClassName OssFileVo
 * @Description  存储文件vo信息
 * @Author zyf
 * @create 2024/6/4 13:58
 * @Version 1.0
 */
@Data
public class OssFileVo {
    /**
     * 对象存储主键
     */
    private Long ossId;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 原名
     */
    private String originalName;

    /**
     * 文件后缀名
     */
    private String fileSuffix;

    /**
     * URL地址
     */
    private String url;
}
