package com.xiaoqian.member.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

@AllArgsConstructor
@Getter
public enum PassengerTypeEnum {

    ADULT("1", "成人"),
    CHILD("2", "儿童"),
    STUDENT("3", "学生");

    private final String code;

    private final String desc;

    public static List<HashMap<String, String>> getEnumList() {
        List<HashMap<String, String>> list = new ArrayList<>();
        for (PassengerTypeEnum anEnum : EnumSet.allOf(PassengerTypeEnum.class)) {
            HashMap<String, String> map = new HashMap<>();
            map.put("code", anEnum.code);
            map.put("desc", anEnum.desc);
            list.add(map);
        }
        return list;
    }
}
