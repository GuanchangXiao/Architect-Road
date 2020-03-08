package com.foodie.order.service;

import com.foodie.order.pojo.OrderStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("order-api")
public interface OrderService {

    /**
     * 用于创建订单相关信息
     * @param submitOrderBO
     */
    // TODO 移植到购物车模块
//    OrderVO createOrder(List<ShopcartBO> shopcartList, SubmitOrderBO submitOrderBO);

    /**
     * 修改订单状态
     * @param orderId
     * @param orderStatus
     */
    @PutMapping("update-order-status")
    void updateOrderStatus(@RequestParam("orderId") String orderId,
                           @RequestParam("orderStatus") Integer orderStatus);

    /**
     * 查询订单状态
     * @param orderId
     * @return
     */
    @GetMapping("order-status-info")
    OrderStatus queryOrderStatusInfo(@RequestParam("orderId") String orderId);

    /**
     * 关闭超时未支付订单
     */
    @PostMapping("close-order")
    void closeOrder();
}
