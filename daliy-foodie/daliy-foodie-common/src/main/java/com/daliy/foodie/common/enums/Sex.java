package com.daliy.foodie.common.enums;

import lombok.AllArgsConstructor;

/**
 * @Desc: 性别 枚举类
 * Created by perl on 2019-12-06.
 */
@AllArgsConstructor
public enum Sex {
    WOMAN(0, "女"),
    MAN(1, "男"),
    SECRET(2, "保密");

    public final Integer type;
    public final String value;
}
