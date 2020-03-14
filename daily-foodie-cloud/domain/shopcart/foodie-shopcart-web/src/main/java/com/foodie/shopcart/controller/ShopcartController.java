package com.foodie.shopcart.controller;

import com.foodie.pojo.JSONResult;
import com.foodie.shopcart.pojo.bo.ShopcartBO;
import com.foodie.shopcart.service.ShopcartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by perl on 2020-03-10.
 */
@Api(value = "购物车相关", tags = {"购物车相关的api接口"})
@RequestMapping("shopcart")
@RestController
public class ShopcartController {

    @Autowired
    private ShopcartService shopcartService;

    @ApiOperation(value = "根据商品列表计算购买总数", notes = "根据商品列表计算购买总数", httpMethod = "POST")
    @PostMapping("/count")
    public JSONResult count(@RequestBody List<ShopcartBO> shopcartList,
                           @RequestParam("specId") String specId) {
        ShopcartBO shopcartBO = shopcartService.getBuyCountsFromShopcart(shopcartList, specId);
        return JSONResult.ok(shopcartBO);
    }

    @ApiOperation(value = "获取购物车商品", notes = "获取购物车中所有商品信息", httpMethod = "GET")
    @PostMapping("/list")
    public JSONResult list(@RequestParam("userId") String userId) {
        List<ShopcartBO> list = shopcartService.listItemFormCart(userId);
        return JSONResult.ok(list);
    }

    @ApiOperation(value = "添加商品到购物车", notes = "添加商品到购物车", httpMethod = "POST")
    @PostMapping("/add")
    public JSONResult add(
            @RequestParam String userId,
            @RequestBody ShopcartBO shopcartBO,
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        if (StringUtils.isBlank(userId)) {
            return JSONResult.errorMsg("");
        }
        boolean result = shopcartService.addItemToCart(userId, shopcartBO);
        return result ? JSONResult.ok() : JSONResult.errorMsg("添加商品失败");
    }

    @ApiOperation(value = "从购物车中删除商品", notes = "从购物车中删除商品", httpMethod = "POST")
    @PostMapping("/del")
    public JSONResult del(
            @RequestParam String userId,
            @RequestParam String itemSpecId,
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        if (StringUtils.isBlank(userId) || StringUtils.isBlank(itemSpecId)) {
            return JSONResult.errorMsg("参数不能为空");
        }
        boolean result = shopcartService.deleteItemFormCart(userId, itemSpecId);
        return result ? JSONResult.ok() : JSONResult.errorMsg("删除失败,商品不存在");
    }

    @ApiOperation(value = "修改商品数量", notes = "修改购物车中指定商品数量", httpMethod = "POST")
    @PostMapping("/update")
    public JSONResult update(
            @RequestParam String userId,
            @RequestParam String itemSpecId,
            @RequestParam Integer amount,
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        if (StringUtils.isBlank(userId) ||
            StringUtils.isBlank(itemSpecId) ||
            amount == null
        ) {
            return JSONResult.errorMsg("参数不能为空");
        }
        boolean result = shopcartService.updateAmountFormCart(userId, itemSpecId, amount);
        return result ? JSONResult.ok() : JSONResult.errorMsg("修改商品数量失败");
    }


    @ApiOperation(value = "清空购物车", notes = "清空购物车中所有商品", httpMethod = "POST")
    @PostMapping("/clear")
    public JSONResult clear(
            @RequestParam String userId,
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        if (StringUtils.isBlank(userId)) {
            return JSONResult.errorMsg("参数不能为空");
        }
        boolean result = shopcartService.clearCart(userId);
        return result ? JSONResult.ok() : JSONResult.errorMsg("清空购物车失败");
    }
}
