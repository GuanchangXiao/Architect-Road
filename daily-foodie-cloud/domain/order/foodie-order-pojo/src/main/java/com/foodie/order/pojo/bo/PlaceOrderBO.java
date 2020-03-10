package com.foodie.order.pojo.bo;

import com.foodie.shopcart.pojo.bo.ShopcartBO;
import lombok.Data;

import java.util.List;

/**
 * Created by perl on 2020-03-08.
 * 创建订单BO
 */
@Data
public class PlaceOrderBO {
    private List<ShopcartBO> shopcartList;
    private SubmitOrderBO submitOrderBO;
}
