package com.foodie.item.service;

import com.foodie.item.pojo.Items;
import com.foodie.item.pojo.ItemsImg;
import com.foodie.item.pojo.ItemsParam;
import com.foodie.item.pojo.ItemsSpec;
import com.foodie.item.pojo.vo.CommentLevelCountsVO;
import com.foodie.item.pojo.vo.ShopcartVO;
import com.foodie.pojo.PagedGridResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("foodie-item-service")
@RequestMapping("item-api")
public interface ItemService {

    /**
     * 根据商品ID查询详情
     * @param itemId
     * @return
     */
    @GetMapping("/item")
    Items queryItemById(@RequestParam("itemId") String itemId);

    /**
     * 根据商品id查询商品图片列表
     * @param itemId
     * @return
     */
    @GetMapping("item-images")
    List<ItemsImg> queryItemImgList(@RequestParam("itemId") String itemId);

    /**
     * 根据商品id查询商品规格
     * @param itemId
     * @return
     */
    @GetMapping("item-specs")
    List<ItemsSpec> queryItemSpecList(@RequestParam("itemId") String itemId);

    /**
     * 根据商品id查询商品参数
     * @param itemId
     * @return
     */
    @GetMapping("item-param")
    ItemsParam queryItemParam(@RequestParam("itemId") String itemId);

    /**
     * 根据商品id查询商品的评价等级数量
     * @param itemId
     */
    @GetMapping("commont-counts")
    CommentLevelCountsVO queryCommentCounts(@RequestParam("itemId") String itemId);

    /**
     * 根据商品id查询商品的评价（分页）
     * @param itemId
     * @param level
     * @return
     */
    @GetMapping("paged-comments")
    PagedGridResult queryPagedComments(@RequestParam("itemId") String itemId,
                                       @RequestParam("level") Integer level,
                                       @RequestParam(value = "page", required = false) Integer page,
                                       @RequestParam(value = "pageSize", required = false) Integer pageSize);
    /**
     * 根据规格ids查询最新的购物车中商品数据（用于刷新渲染购物车中的商品数据）
     * @param specIds
     * @return
     */
    @GetMapping("item-spec-ids")
    List<ShopcartVO> queryItemsBySpecIds(@RequestParam("specIds") String specIds);

    /**
     * 根据商品规格id获取规格对象的具体信息
     * @param specId
     * @return
     */
    @GetMapping("item-spec")
    ItemsSpec queryItemSpecById(@RequestParam("specId") String specId);

    /**
     * 根据商品id获得商品图片主图url
     * @param itemId
     * @return
     */
    @GetMapping("item-main-image")
    String queryItemMainImgById(@RequestParam("itemId") String itemId);

    /**
     * 减少库存
     * @param specId
     * @param buyCounts
     */
    @PostMapping("decrease-item-spec")
    void decreaseItemSpecStock(@RequestParam("specId") String specId,
                               @RequestParam("buyCounts") int buyCounts);
}
