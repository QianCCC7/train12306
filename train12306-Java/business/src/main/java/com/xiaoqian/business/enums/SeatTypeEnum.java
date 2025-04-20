package com.xiaoqian.business.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SeatTypeEnum {
    YDZ("1", "一等座"),
    EDZ("2", "二等座"),
    RW("3", "软卧"),
    YW("4", "硬卧")
    ;
    @EnumValue
    private final String code;
    @JsonValue
    private final String desc;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static SeatTypeEnum fromCode(String code) {
        for (SeatTypeEnum type : SeatTypeEnum.values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("无效的车厢类型: " + code);
    }
}
