package com.hdsolartech.business.mapper;

import com.hdsolartech.business.domain.HdOrderInfo;
import com.hdsolartech.business.domain.vo.HdOrderInfoVo;
import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;

import java.util.Date;

/**
 * 销售订单信息Mapper接口
 *
 * @author Lion Li
 * @date 2024-06-03
 */
public interface HdOrderInfoMapper extends BaseMapperPlus<HdOrderInfo, HdOrderInfoVo> {

    /**
     * 完成的报价数量
     * @return
     */
    Integer countOffer(@Param("userId")Long userId, @Param("startTime") Date startDate, @Param("endTime")Date endDate);
    /**
     * 完成的下单数量
     * @return
     */
    Integer countOrder(@Param("userId")Long userId, @Param("startTime")Date startDate,@Param("endTime")Date endDate);
}
