package com.xiaoqian.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HttpCodeEnum {
    SUCCESS(200,"操作成功！"),
    MEMBER_MOBILE_EXIST(400, "手机号已注册！"),
    MEMBER_MOBILE_NOT_EXIST(400, "手机号不存在！"),
    CODE_ERROR(400, "验证码错误！"),

    ;

    private final Integer code;
    private final String desc;
}
