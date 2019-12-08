package com.daliy.foodie.common.enums;

import lombok.AllArgsConstructor;

/**
 * @Desc: 订单状态 枚举类
 * Created by perl on 2019-12-06.
 */
@AllArgsConstructor
public enum OrderStatusEnum {
    WAIT_PAY(10, "待付款"),
    WAIT_DELIVER(20, "已付款，待发货"),
    WAIT_RECEIVE(30, "已发货，待收货"),
    SUCCESS(40, "交易成功"),
    CLOSE(50, "交易关闭");

    public final Integer type;
    public final String value;
}
