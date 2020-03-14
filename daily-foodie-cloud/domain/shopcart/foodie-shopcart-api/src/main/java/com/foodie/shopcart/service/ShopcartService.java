package com.foodie.shopcart.service;

import com.foodie.shopcart.pojo.bo.ShopcartBO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by perl on 2020-03-08.
 */
@FeignClient("foodie-shopcart-service")
@RequestMapping("shopcart-api")
public interface ShopcartService {

    /**
     * 计算buyCounts
     * @param shopcartList
     * @param specId
     * @return
     */
    @PostMapping("counts")
    ShopcartBO getBuyCountsFromShopcart(@RequestParam("shopcartList") List<ShopcartBO> shopcartList,
                                        @RequestParam("specId") String specId);

    /**
     * 获取购物车中所有商品
     * @param userId
     * @return
     */
    @GetMapping("list")
    List<ShopcartBO> listItemFormCart(@RequestParam("userId") String userId);

    /**
     * 添加购物车商品
     * @param userId
     * @param shopcartBO
     * @return
     */
    @PostMapping("add-item")
    boolean addItemToCart(@RequestParam("userId") String userId,
                          @RequestBody ShopcartBO shopcartBO);

    /**
     * 删除购物车商品
     * @param userId
     * @param itemSpecId
     * @return
     */
    @PostMapping("delete-item")
    boolean deleteItemFormCart(@RequestParam("userId") String userId,
                               @RequestParam("itemSpecId") String itemSpecId);

    /**
     * 清空购物车
     * @param userId
     * @return
     */
    @PostMapping("clear-cart")
    boolean clearCart(@RequestParam("userId") String userId);

    /**
     * 修改购物车中商品数量
     * @param userId
     * @param itemSpecId
     * @param amount
     * @return
     */
    @PostMapping("update-amount")
    boolean updateAmountFormCart(@RequestParam("userId") String userId,
                                 @RequestParam("itemSpecId") String itemSpecId,
                                 @RequestParam("amount") Integer amount);
}
