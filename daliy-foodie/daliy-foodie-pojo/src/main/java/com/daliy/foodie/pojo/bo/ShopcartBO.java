package com.daliy.foodie.pojo.bo;

import lombok.Data;

/**
 * Created by perl on 2019-12-07.
 */
@Data
public class ShopcartBO {
    private String itemId;
    private String itemImgUrl;
    private String itemName;
    private String specId;
    private String specName;
    private Integer buyCounts;
    private String priceDiscount;
    private String priceNormal;
}
