package com.daliy.foodie.service.center;

import com.daliy.foodie.common.utils.PagedGridResult;
import com.daliy.foodie.pojo.OrderItems;
import com.daliy.foodie.pojo.bo.OrderItemsCommentBO;

import java.util.List;

/**
 * Created by perl on 2019-12-08.
 */
public interface MyCommentsService {

    /**
     * 根据订单id查询关联的商品
     * @param orderId
     * @return
     */
    public List<OrderItems> queryPendingComment(String orderId);

    /**
     * 保存用户的评论
     * @param orderId
     * @param userId
     * @param commentList
     */
    public void saveComments(String orderId, String userId, List<OrderItemsCommentBO> commentList);


    /**
     * 我的评价查询 分页
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    public PagedGridResult queryMyComments(String userId, Integer page, Integer pageSize);
}
