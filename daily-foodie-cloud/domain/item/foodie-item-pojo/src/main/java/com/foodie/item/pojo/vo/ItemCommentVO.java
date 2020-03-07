package com.foodie.item.pojo.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 用于展示商品评价的VO
 */
@Getter
@Setter
public class ItemCommentVO {
    private Integer commentLevel;
    private String content;
    private String specName;
    private Date createdTime;
    private String userFace;
    private String nickname;
}
