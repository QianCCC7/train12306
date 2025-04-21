package com.xiaoqian.business.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

@Getter
@AllArgsConstructor
public enum SeatColEnum {
    YDZ_A("A1", "一等座座位A", "1"),
    YDZ_C("C1", "一等座座位C", "1"),
    YDZ_D("D1", "一等座座位D", "1"),
    YDZ_F("F1", "一等座座位F", "1"),

    EDZ_A("A2", "二等座座位A", "2"),
    EDZ_B("B2", "二等座座位B", "2"),
    EDZ_C("C2", "二等座座位C", "2"),
    EDZ_D("D2", "二等座座位D", "2"),
    EDZ_F("F2", "二等座座位F", "2");
    @EnumValue
    private final String code;
    @JsonValue
    private final String desc;
    private final String type;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static SeatColEnum fromCode(String code) {
        for (SeatColEnum type : SeatColEnum.values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("无效的座位信息: " + code);
    }

    /**
     * 根据车箱的座位类型，筛选出所有的列，比如车箱类型是一等座，则筛选出columnList={ACDF}
     */
    public static List<SeatColEnum> getColsByType(String seatType) {
        List<SeatColEnum> colList = new ArrayList<>();
        EnumSet<SeatColEnum> seatColEnums = EnumSet.allOf(SeatColEnum.class);
        for (SeatColEnum anEnum : seatColEnums) {
            if (seatType.equals(anEnum.getType())) {
                colList.add(anEnum);
            }
        }
        return colList;
    }
}
