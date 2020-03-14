package com.foodie.order.service.center;

import com.foodie.order.pojo.OrderItems;
import com.foodie.order.pojo.bo.center.OrderItemsCommentBO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient("foodie-order-service")
@RequestMapping("comment-api")
public interface MyCommentsService {

    /**
     * 根据订单id查询关联的商品
     * @param orderId
     * @return
     */
    @GetMapping("query")
    List<OrderItems> queryPendingComment(String orderId);

    /**
     * 保存用户的评论
     * @param orderId
     * @param userId
     * @param commentList
     */
    @PostMapping("save")
    void saveComments(String orderId, String userId, List<OrderItemsCommentBO> commentList);


}
