package com.daliy.foodie.api.controller;

import com.daliy.foodie.common.enums.YesOrNo;
import com.daliy.foodie.common.utils.JSONResult;
import com.daliy.foodie.common.utils.JsonUtils;
import com.daliy.foodie.common.utils.RedisOperator;
import com.daliy.foodie.pojo.Carousel;
import com.daliy.foodie.pojo.Category;
import com.daliy.foodie.service.CarouselService;
import com.daliy.foodie.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @Description: 首页展示的相关接口
 * Created by perl on 11/23/19.
 */
@Api(value = "首页", tags = {"首页展示的相关接口"})
@RestController
@RequestMapping("index")
public class IndexController {

    @Autowired
    private CarouselService carouselService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RedisOperator redisOperator;

    //  轮播图对应redis的key
    public static final String CAROUSEL_KEY = "carousel";
    public static final String CATS_KEY = "cats";

    @ApiOperation(value = "获取首页轮播图列表", notes = "获取首页轮播图列表", httpMethod = "GET")
    @GetMapping("/carousel")
    public JSONResult carousel() {

        // 首页轮播图使用redis缓存来处理
        List<Carousel> carouselList;
        String redisValue = redisOperator.get(CAROUSEL_KEY);

        if (StringUtils.isBlank(redisValue)) {
            // 如果redis中没有数据，则查询数据库并将数据写入缓存
            carouselList = carouselService.queryAll(YesOrNo.YES.type);
            redisOperator.set(CAROUSEL_KEY,JsonUtils.objectToJson(carouselList));
        }else {
            // 反之，则直接将缓存中的数据返回
            carouselList = JsonUtils.jsonToList(redisValue,Carousel.class);
        }
        return JSONResult.ok(carouselList);
    }

    /**
     * 首页分类展示需求：
     * 1. 第一次刷新主页查询大分类，渲染展示到首页
     * 2. 如果鼠标上移到大分类，则加载其子分类的内容，如果已经存在子分类，则不需要加载（懒加载）
     */
    @ApiOperation(value = "获取商品分类(一级分类)", notes = "获取商品分类(一级分类)", httpMethod = "GET")
    @GetMapping("/cats")
    public JSONResult cats() {
        // 使用redis缓存
        List<Category> categoryList;
        String redisValue = redisOperator.get(CATS_KEY);

        if (StringUtils.isBlank(redisValue)) {
            categoryList = categoryService.queryAllRootLevelCat();
            redisOperator.set(CATS_KEY,JsonUtils.objectToJson(categoryList));
        }else {
            categoryList = JsonUtils.jsonToList(redisValue,Category.class);
        }
        return JSONResult.ok(categoryList);
    }

    @ApiOperation(value = "获取商品子分类", notes = "获取商品子分类", httpMethod = "GET")
    @GetMapping("/subCat/{rootCatId}")
    public JSONResult subCat(
            @ApiParam(name = "rootCatId", value = "一级分类id", required = true)
            @PathVariable Integer rootCatId) {
        if (rootCatId == null || rootCatId < 0) {
            return JSONResult.errorMsg("该分类不存在");
        }
        return JSONResult.ok(categoryService.getSubCatList(rootCatId));
    }

    @ApiOperation(value = "查询每个一级分类下的最新6条商品数据", notes = "查询每个一级分类下的最新6条商品数据", httpMethod = "GET")
    @GetMapping("/sixNewItems/{rootCatId}")
    public JSONResult sixNewItems(
            @ApiParam(name = "rootCatId", value = "一级分类id", required = true)
            @PathVariable Integer rootCatId) {
        if (rootCatId == null || rootCatId < 0) {
            return JSONResult.errorMsg("该分类不存在");
        }

        return JSONResult.ok(categoryService.getSixNewItemsLazy(rootCatId));
    }

}
