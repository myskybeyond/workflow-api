package org.dromara.common.core.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * LuckySheet
 *
 * @author Lion Li
 */
public interface LuckySheetService  {

    /**
     * 获取luckySheet dataS
     * @param fileId
     * @param version
     * @return
     */
    List<JSONObject> getLuckyDataS(Long fileId, Long version);


    /**
     * 创建luckyFile
     * @param fileName  文件名称
     * @param remark  文件备注
     * @param fileJson  文件内容
     * @return   在线文件id、在线文件名称、在线文件版本、在线文件备注
     */
    String  createLuckyFile(String fileName, String remark ,List<JSONObject> fileJson);


}
