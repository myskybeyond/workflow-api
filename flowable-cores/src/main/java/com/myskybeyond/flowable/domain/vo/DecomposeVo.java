package com.myskybeyond.flowable.domain.vo;

import lombok.Data;

/**
 * @ClassName DecomposeVo
 * @Description 分解Vo
 * @Author zyf
 * @create 2024/6/21 17:11
 * @Version 1.0
 */
@Data
public class DecomposeVo {

    /**
     * bom 在线文件信息       //onlineFiles = 在线文件id、在线文件名称、在线文件版本、在线文件备注
     */
    private String  bom;

    /**
     * 发货 在线文件信息        //onlineFiles = 在线文件id、在线文件名称、在线文件版本、在线文件备注
     */
    private String deliver;


}
