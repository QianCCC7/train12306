package com.xiaoqian.business.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RocketMQTopicEnum {
    CONFIRM_ORDER("CONFIRM_ORDER", "确认订单排队");

    private final String topic;
    private final String desc;
}
