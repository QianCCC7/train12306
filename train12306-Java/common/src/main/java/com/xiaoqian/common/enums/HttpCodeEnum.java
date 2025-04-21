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
    STATION_NAME_EXIST(400, "站名已存在"),
    TRAIN_CODE_EXIST(400, "车次编码已存在"),
    TRAIN_STATION_CODE_INDEX_EXIST(400, "同车次站序已存在"),
    TRAIN_STATION_CODE_NAME_EXIST(400, "同车次站名已存在"),
    TRAIN_CARRIAGE_CODE_INDEX_EXIST(400, "同车次厢号已存在"),
    ;

    private final Integer code;
    private final String desc;
}
