package com.daliy.foodie.pojo.bo;

import lombok.Data;

/**
 * Created by perl on 2019-12-08.
 */
@Data
public class OrderItemsCommentBO {
    private String commentId;
    private String itemId;
    private String itemName;
    private String itemSpecId;
    private String itemSpecName;
    private Integer commentLevel;
    private String content;
}
