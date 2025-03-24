package org.dromara.common.core.service;


public interface OrderService {

    /**
     * 根据订单id查询订单名称
     * @param orderId
     * @return
     */
    String selectNameByOrderId(Long orderId);


}
