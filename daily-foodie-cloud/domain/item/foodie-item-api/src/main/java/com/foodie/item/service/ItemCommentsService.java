package com.foodie.item.service;

import com.foodie.pojo.PagedGridResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by perl on 2020-02-29.
 */
@RequestMapping("item-comments-api")
public interface ItemCommentsService {

    /**
     * 保存评论
     * @param params
     */
    @PostMapping("save-comments")
    void saveComments(@RequestBody Map<String, Object> params);


    /**
     * 我的评价查询 分页
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("my-comments")
    PagedGridResult queryMyComments(@RequestParam("userId") String userId,
                                    @RequestParam(value = "page", required = false) Integer page,
                                    @RequestParam(value = "pageSize", required = false) Integer pageSize);
}
