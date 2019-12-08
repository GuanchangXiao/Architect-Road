package com.daliy.foodie.service;

import com.daliy.foodie.pojo.OrderStatus;
import com.daliy.foodie.pojo.bo.SubmitOrderBO;
import com.daliy.foodie.pojo.vo.OrderVO;

/**
 * Created by perl on 2019-12-07.
 */
public interface OrderService {
    /**
     * 创建订单
     * @param submitOrderBO
     * @return
     */
    OrderVO createOrder(SubmitOrderBO submitOrderBO);

    /**
     * 更新订单状态
     * @param merchantOrderId
     * @param type
     */
    void updateOrderStatus(String merchantOrderId, Integer type);

    /**
     * 查询订单状态信息
     * @param orderId
     * @return
     */
    OrderStatus queryOrderStatusInfo(String orderId);

    /**
     * 关闭订单
     */
    void closeOrder();
}
