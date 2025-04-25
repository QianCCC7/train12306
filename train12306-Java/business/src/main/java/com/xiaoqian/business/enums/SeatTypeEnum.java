package com.xiaoqian.business.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public enum SeatTypeEnum {
    YDZ("1", "一等座", new BigDecimal("0.4")),
    EDZ("2", "二等座", new BigDecimal("0.3")),
    RW("3", "软卧", new BigDecimal("0.6")),
    YW("4", "硬卧", new BigDecimal("0.5"))
    ;
    @EnumValue
    private final String code;
    @JsonValue
    private final String desc;
    /**
     * 基础票价 N元/公里，0.4即为0.4元/公里
     */
    private final BigDecimal price;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static SeatTypeEnum fromCode(String code) {
        for (SeatTypeEnum type : SeatTypeEnum.values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("无效的车座类型: " + code);
    }
}
