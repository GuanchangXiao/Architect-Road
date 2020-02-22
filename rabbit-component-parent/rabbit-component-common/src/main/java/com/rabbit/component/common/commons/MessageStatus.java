package com.rabbit.component.common.commons;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by perl on 2020-02-21.
 */
@AllArgsConstructor
@Getter
public enum MessageStatus {
    SEND_READY("0"),
    SEND_OK("1"),
    SEND_FAIL("2");

    private String code;
}
