package com.daliy.foodie.common.enums;

import lombok.AllArgsConstructor;

/**
 * @Desc:
 * Created by perl on 2019-12-06.
 */
@AllArgsConstructor
public enum PayMethod {
    WEIXIN(1, "微信"),
    ALIPAY(2, "支付宝");

    public final Integer type;
    public final String value;
}
