package com.daliy.foodie.pojo.vo;



import com.daliy.foodie.pojo.Items;
import com.daliy.foodie.pojo.ItemsImg;
import com.daliy.foodie.pojo.ItemsParam;
import com.daliy.foodie.pojo.ItemsSpec;
import lombok.Data;

import java.util.List;

/**
 * 商品详情VO
 */
@Data
public class ItemInfoVO {

    private Items item;
    private List<ItemsImg> itemImgList;
    private List<ItemsSpec> itemSpecList;
    private ItemsParam itemParams;
}
