package com.foodie.item.pojo.vo;


import com.foodie.item.pojo.Items;
import com.foodie.item.pojo.ItemsImg;
import com.foodie.item.pojo.ItemsParam;
import com.foodie.item.pojo.ItemsSpec;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 商品详情VO
 */
@Getter
@Setter
public class ItemInfoVO {
    private Items item;
    private List<ItemsImg> itemImgList;
    private List<ItemsSpec> itemSpecList;
    private ItemsParam itemParams;
}
