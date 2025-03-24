package com.hdsolartech.business.common.util.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.List;

/**
 * @ClassName DecoposeOutVo
 * @Description  分解返回结果
 * @Author zyf
 * @create 2024/6/25 15:11
 * @Version 1.0
 */
@Data
public class DecomposeOutVo {

    /**
     * 发货清单名称
     */
    private  String deliverName;

    /**
     * 发货清单json
     */
    private List<JSONObject> deliverFileJson;

    /**
     * 发货备注
     */
    private  String deliverRemark;

    /**
     * 清单名称
     */
    private  String bomName;


    /**
     * 材料清单json
     */
    private List<JSONObject> bomFileJson;


    /**
     * 清单备注
     */
    private  String bomRemark;



}
