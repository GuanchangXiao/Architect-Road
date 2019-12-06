package com.daliy.foodie.common.enums;

import lombok.AllArgsConstructor;

/**
 * @Desc: 评论等级枚举类
 * Created by perl on 2019-12-06.
 */
@AllArgsConstructor
public enum CommentLevel {
    GOOD(1, "好评"),
    NORMAL(2, "中评"),
    BAD(3, "差评");

    public final Integer type;
    public final String value;
}
