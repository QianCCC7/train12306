package com.xiaoqian.business.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConfirmOrderStatusEnum {
    INIT("I", "初始"),
    PENDING("P", "处理中"),
    SUCCESS("S", "成功"),
    FAILURE("F", "失败"),
    EMPTY("E", "无票"),
    CANCEL("C", "取消");
    @EnumValue
    private final String code;
    @JsonValue
    private final String desc;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static ConfirmOrderStatusEnum fromCode(String code) {
        for (ConfirmOrderStatusEnum type : ConfirmOrderStatusEnum.values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("无效的订单状态: " + code);
    }
}
