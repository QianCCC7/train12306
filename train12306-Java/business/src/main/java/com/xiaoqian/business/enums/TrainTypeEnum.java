package com.xiaoqian.business.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum TrainTypeEnum {

    G("G", "高铁"),
    D("D", "动车"),
    K("K", "快速");

    @EnumValue
    private final String code;
    @JsonValue
    private final String desc;

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
