package com.daliy.foodie.service;

import com.daliy.foodie.pojo.OrderStatus;
import com.daliy.foodie.pojo.bo.ShopcartBO;
import com.daliy.foodie.pojo.bo.SubmitOrderBO;
import com.daliy.foodie.pojo.vo.OrderVO;

import java.util.List;

/**
 * Created by perl on 2019-12-07.
 */
public interface OrderService {
    /**
     * 创建订单
     *
     * @param shopcartList 缓存中的购物车商品
     * @param submitOrderBO 提交订单的商品
     * @return
     */
    OrderVO createOrder(List<ShopcartBO> shopcartList, SubmitOrderBO submitOrderBO);

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
