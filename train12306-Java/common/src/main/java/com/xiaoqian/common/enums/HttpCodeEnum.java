package com.xiaoqian.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HttpCodeEnum {
    SUCCESS(200,"操作成功！"),
    BIZ_ERROR(400, "发生业务异常！"),
    ;

    private final Integer code;
    private final String msg;
}
