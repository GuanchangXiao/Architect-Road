package com.daliy.foodie.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by perl on 11/23/19.
 */
@AllArgsConstructor
@Getter
public enum StatusCode {
    SUCCESS(200,"成功"),
    MSG_ERROR(500,"error_msg"),
    MAP_ERROR(501,"error_map"),
    TOKEN_ERROR(502,"验证信息错误"),
    EXCEPTION_ERROR(555,"发生异常"),
    USER_QQ_ERROR(556,"错误,用户qq");

    int code;
    String message;
}
