package com.foodie.order.fallback.itemservice;

import com.foodie.pojo.PagedGridResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * Created by perl on 2020-03-15.
 */
@RequestMapping("item-comments-fallback")
@Component
@Slf4j
public class ItemCommentsFallback implements ItemCommentsFeignClient {
    @Override
    public void saveComments(Map<String, Object> params) {
        log.info("fallback saveComments...");
    }

    @Override
    public PagedGridResult queryMyComments(String userId, Integer page, Integer pageSize) {
        log.info("fallback queryMyComments...");
        return null;
    }
}
