package com.foodie.item.pojo.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 用于展示商品评价数量的vo
 */
@Getter
@Setter
public class CommentLevelCountsVO {
    public Integer totalCounts;
    public Integer goodCounts;
    public Integer normalCounts;
    public Integer badCounts;
}
