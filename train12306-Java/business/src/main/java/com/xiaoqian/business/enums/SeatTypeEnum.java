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
    YDZ("1", "一等座", new BigDecimal("0.4"), "ydz"),
    EDZ("2", "二等座", new BigDecimal("0.3"), "edz"),
    RW("3", "软卧", new BigDecimal("0.6"), "rw"),
    YW("4", "硬卧", new BigDecimal("0.5"), "yw")
    ;
    @EnumValue
    private final String code;
    @JsonValue
    private final String desc;
    /**
     * 基础票价 N元/公里，0.4即为0.4元/公里
     */
    private final BigDecimal price;
    private final String type;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static SeatTypeEnum fromCode(String code) {
        for (SeatTypeEnum type : SeatTypeEnum.values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("无效的车座类型: " + code);
    }

    public static SeatTypeEnum getByType(String type) {
        for (SeatTypeEnum seatTypeEnum : SeatTypeEnum.values()) {
            if (seatTypeEnum.type.equals(type)) {
                return seatTypeEnum;
            }
        }

        throw new IllegalArgumentException("没有匹配的车座类型: " + type);
    }
}
