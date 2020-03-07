package com.foodie.item.pojo.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShopcartVO {
    private String itemId;
    private String itemImgUrl;
    private String itemName;
    private String specId;
    private String specName;
    private String priceDiscount;
    private String priceNormal;
}
