package com.fooide.shopcart.service;

import com.foodie.shopcart.pojo.bo.ShopcartBO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by perl on 2020-03-08.
 */
@RequestMapping("shopcart-api")
public interface ShopcartService {

    /**
     * 计算buyCounts
     * @param shopcartList
     * @param specId
     * @return
     */
    @PostMapping("counts")
    ShopcartBO getBuyCountsFromShopcart(@RequestParam("shopcartList") List<ShopcartBO> shopcartList,
                                        @RequestParam("specId") String specId);
}
