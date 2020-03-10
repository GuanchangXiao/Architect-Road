package com.foodie.shopcart.controller;

import com.foodie.pojo.JSONResult;
import com.foodie.shopcart.pojo.bo.ShopcartBO;
import com.fooide.shopcart.service.ShopcartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/list")
    public JSONResult list(@RequestBody List<ShopcartBO> shopcartList,
                           @RequestParam("specId") String specId) {

        ShopcartBO shopcartBO = shopcartService.getBuyCountsFromShopcart(shopcartList, specId);
        return JSONResult.ok(shopcartBO);
    }
}
