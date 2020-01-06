package com.daliy.foodie.pojo.vo;

import com.daliy.foodie.pojo.bo.ShopcartBO;
import lombok.Data;

import java.util.List;

@Data
public class OrderVO {

    private String orderId;
    private MerchantOrdersVO merchantOrdersVO;
    private List<ShopcartBO> waitDelShopCartItems;
}