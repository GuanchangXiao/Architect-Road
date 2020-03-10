package com.foodie.shopcart.service.impl;

import com.foodie.shopcart.pojo.bo.ShopcartBO;
import com.fooide.shopcart.service.ShopcartService;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by perl on 2020-03-08.
 */
@RestController
public class ShopcartServiceImpl implements ShopcartService {

    @Override
    public ShopcartBO getBuyCountsFromShopcart(List<ShopcartBO> shopcartList, String specId) {
        for (ShopcartBO cart : shopcartList) {
            if (cart.getSpecId().equals(specId)) {
                return cart;
            }
        }
        return null;
    }
}
