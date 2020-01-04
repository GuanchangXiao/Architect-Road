package com.daliy.foodie.api.controller;

import com.daliy.foodie.common.utils.JSONResult;
import com.daliy.foodie.common.utils.JsonUtils;
import com.daliy.foodie.common.utils.RedisOperator;
import com.daliy.foodie.pojo.bo.ShopcartBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by perl on 2019-12-07.
 */
@Api(value = "购物车接口controller", tags = {"购物车接口相关的api"})
@RequestMapping("shopcart")
@RestController
public class ShopcartController {

    private static final String SHOP_CART_KEY = "shopcarts";
    @Autowired
    private RedisOperator redisOperator;

    @ApiOperation(value = "添加商品到购物车", notes = "添加商品到购物车", httpMethod = "POST")
    @PostMapping("/add")
    public JSONResult add(
            @RequestParam String userId,
            @RequestBody ShopcartBO shopcartBO,
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        if (StringUtils.isBlank(userId)) {
            return JSONResult.errorMsg("用户未登陆");
        }

        String redisValue = redisOperator.get(SHOP_CART_KEY);
        List<ShopcartBO> shopcartList;
        boolean flag = false;

        if (StringUtils.isBlank(redisValue)) {
            // 如果没有从缓存中取到购物车数据，则需要初始化购物车
            shopcartList = new ArrayList<>();
        }else {
            // 否则，需要将购物车中的数据转为list
            shopcartList = JsonUtils.jsonToList(redisValue,ShopcartBO.class);
        }

        // 判断添加到购物车中的商品是否已经在购物车中
        for (ShopcartBO shopcart : shopcartList) {
            if (shopcartBO.getItemId().equals(shopcart.getItemId())
                    && shopcartBO.getSpecId().equals(shopcart.getSpecId())) {
                // 重新计算购物车商品数量
                shopcart.setBuyCounts(shopcart.getBuyCounts() + shopcartBO.getBuyCounts());
                flag = true;
                break;
            }
        }

        // 只有购物车中不存在商品时才新增购物车数据，防止重复添加相同商品
        if (!flag) {
            shopcartList.add(shopcartBO);
        }

        // 将处理好的购物车商品信息存入redis
        redisOperator.set(SHOP_CART_KEY,JsonUtils.objectToJson(shopcartList));
        return JSONResult.ok();
    }

    @ApiOperation(value = "从购物车中删除商品", notes = "从购物车中删除商品", httpMethod = "POST")
    @PostMapping("/del")
    public JSONResult del(
            @RequestParam String userId,
            @RequestParam String itemSpecId,
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        if (StringUtils.isBlank(userId)) {
            return JSONResult.errorMsg("用户未登陆");
        }
        // 用户删除购物车内容
        String redisValue = redisOperator.get(SHOP_CART_KEY);
        if (StringUtils.isNotBlank(redisValue)) {
            List<ShopcartBO> shopcartList = JsonUtils.jsonToList(redisValue,ShopcartBO.class);
            for (ShopcartBO shopcart : shopcartList) {
                if (shopcart.getSpecId().equals(itemSpecId)) {
                    shopcartList.remove(shopcart);
                    break;
                }
            }
            redisOperator.set(SHOP_CART_KEY,JsonUtils.objectToJson(shopcartList));
        }
        return JSONResult.ok();
    }
}
