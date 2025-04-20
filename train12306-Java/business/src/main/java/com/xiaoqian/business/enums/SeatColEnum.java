package com.xiaoqian.business.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SeatColEnum {
    YDZ_A("A", "一等座座位A"),
    YDZ_C("C", "一等座座位C"),
    YDZ_D("D", "一等座座位D"),
    YDZ_F("F", "一等座座位F"),

    EDZ_A("A", "二等座座位A"),
    EDZ_B("B", "二等座座位B"),
    EDZ_C("C", "二等座座位C"),
    EDZ_D("D", "二等座座位D"),
    EDZ_F("F", "二等座座位F");
    @EnumValue
    private final String code;
    @JsonValue
    private final String desc;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static SeatColEnum fromCode(String code) {
        for (SeatColEnum type : SeatColEnum.values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("无效的车列类型: " + code);
    }
}
