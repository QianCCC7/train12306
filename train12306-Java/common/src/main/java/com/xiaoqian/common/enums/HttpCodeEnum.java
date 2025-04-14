package com.xiaoqian.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HttpCodeEnum {
    SUCCESS(200,"操作成功！"),
    MEMBER_MOBILE_EXIST(400, "手机号已注册！"),
    ;

    private final Integer code;
    private final String desc;
}
