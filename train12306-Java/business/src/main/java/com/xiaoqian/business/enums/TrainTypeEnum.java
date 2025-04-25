package com.xiaoqian.business.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;


@AllArgsConstructor
@Getter
public enum TrainTypeEnum {

    G("G", "高铁", new BigDecimal("1.2")),
    D("D", "动车", new BigDecimal("1.0")),
    K("K", "快速", new BigDecimal("0.8"));

    @EnumValue
    private final String code;
    @JsonValue
    private final String desc;
    /**
     * 票价比例：比如1.1 => 则票价为 1.1 * 每公里单价（SeatTypeEnum.price） * 里程(trainStation.km)
     */
    private final BigDecimal priceRate;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static TrainTypeEnum fromCode(String code) {
        for (TrainTypeEnum type : TrainTypeEnum.values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("无效的车次类型: " + code);
    }
}
