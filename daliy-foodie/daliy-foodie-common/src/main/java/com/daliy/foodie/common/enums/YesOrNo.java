package com.daliy.foodie.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by perl on 11/23/19.
 */
@AllArgsConstructor
@Getter
public enum YesOrNo {
    NO(0,"no"),
    YES(1,"yes");

    int key;
    String value;
}
