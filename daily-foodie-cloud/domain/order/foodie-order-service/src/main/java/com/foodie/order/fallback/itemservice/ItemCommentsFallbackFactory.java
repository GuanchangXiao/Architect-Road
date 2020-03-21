package com.foodie.order.fallback.itemservice;

import com.foodie.pojo.PagedGridResult;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by perl on 2020-03-15.
 */
@Component
@Slf4j
public class ItemCommentsFallbackFactory implements FallbackFactory<ItemCommentsFeignClient> {
    @Override
    public ItemCommentsFeignClient create(Throwable throwable) {
        return new ItemCommentsFeignClient() {
            @Override
            public void saveComments(Map<String, Object> params) {
                log.info("fallbackFactory ....");
            }

            @Override
            public PagedGridResult queryMyComments(String userId, Integer page, Integer pageSize) {
                log.info("fallbackFactory ....");
                return null;
            }
        };
    }
}
