package com.foodie.order.service.center;

import com.foodie.order.pojo.Orders;
import com.foodie.order.pojo.vo.OrderStatusCountsVO;
import com.foodie.pojo.JSONResult;
import com.foodie.pojo.PagedGridResult;
import org.springframework.web.bind.annotation.*;

@RequestMapping("myorder-api")
public interface MyOrdersService {

    /**
     * 查询我的订单列表
     *
     * @param userId
     * @param orderStatus
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("my-orders")
    PagedGridResult queryMyOrders(@RequestParam("userId") String userId,
                                  @RequestParam("orderStatus") Integer orderStatus,
                                  @RequestParam(value = "page", required = false) Integer page,
                                  @RequestParam(value = "pageSize", required = false) Integer pageSize);

    /**
     * @Description: 订单状态 --> 商家发货
     */
    @PostMapping("update-deliver-order-status")
    void updateDeliverOrderStatus(@RequestParam("orderId") String orderId);

    /**
     * 查询我的订单
     *
     * @param userId
     * @param orderId
     * @return
     */
    @GetMapping("my-order")
    Orders queryMyOrder(@RequestParam("userId") String userId,
                        @RequestParam("orderId") String orderId);

    /**
     * 更新订单状态 —> 确认收货
     *
     * @return
     */
    @PutMapping("update-receive-order-status")
    boolean updateReceiveOrderStatus(@RequestParam("orderId") String orderId);

    /**
     * 删除订单（逻辑删除）
     * @param userId
     * @param orderId
     * @return
     */
    @DeleteMapping("order")
    boolean deleteOrder(@RequestParam("userId") String userId,
                        @RequestParam("orderId") String orderId);

    /**
     * 查询用户订单数
     * @param userId
     */
    @GetMapping("order-status-counts")
    OrderStatusCountsVO getOrderStatusCounts(@RequestParam("userId") String userId);

    /**
     * 获得分页的订单动向
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("orders-trend")
    PagedGridResult getOrdersTrend(@RequestParam("userId") String userId,
                                   @RequestParam(value = "page", required = false) Integer page,
                                   @RequestParam(value = "pageSize", required = false) Integer pageSize);

    @GetMapping("check-order")
    JSONResult checkUserOrder(@RequestParam("userId") String userId,
                              @RequestParam("orderId") String orderId);
}