package com.daliy.foodie.common.enums;

import lombok.AllArgsConstructor;

/**
 * @Desc: 是否 枚举类
 * Created by perl on 11/23/19.
 */
@AllArgsConstructor
public enum YesOrNo {
    NO(0, "否"),
    YES(1, "是");

    public final int type;
    public final String value;
}
