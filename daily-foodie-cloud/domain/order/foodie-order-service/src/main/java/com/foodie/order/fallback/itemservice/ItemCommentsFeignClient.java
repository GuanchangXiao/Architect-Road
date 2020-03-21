package com.foodie.order.fallback.itemservice;

import com.foodie.item.service.ItemCommentsService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * Created by perl on 2020-03-15.
 */
@FeignClient(value = "foodie-item-service", fallback = ItemCommentsFallback.class)
//@FeignClient(value = "foodie-item-service", fallbackFactory = ItemCommentsFallbackFactory.class)
public interface ItemCommentsFeignClient extends ItemCommentsService {
}
