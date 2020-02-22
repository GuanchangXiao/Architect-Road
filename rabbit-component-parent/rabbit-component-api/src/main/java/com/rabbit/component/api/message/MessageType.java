package com.rabbit.component.api.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by perl on 2020-02-19.
 * 消息类型枚举
 *
 * RAPID 迅速消息：
 *  不需要保证消息的可靠性，也不需要confirm确认消息
 * CONFIRM 确认消息：
 *  不需要保证消息的可靠性，但需要confirm确认消息
 * RELIANT 可靠消息：
 *  100%保证消息的可靠性
 *  保证数据库和消息的原子性
 *  最终一致
 */
@AllArgsConstructor
@Getter
public enum MessageType {
    RAPID("0","RAPID"),
    CONFIRM("1","CONFIRM"),
    RELIANT("2","RELIANT");

    private String code;
    private String type;
}
