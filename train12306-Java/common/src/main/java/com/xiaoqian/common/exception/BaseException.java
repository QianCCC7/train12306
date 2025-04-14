package com.xiaoqian.common.exception;

import com.xiaoqian.common.enums.HttpCodeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseException extends RuntimeException {
    private Integer code;
    private String msg;

    public BaseException(HttpCodeEnum httpCodeEnum) {
        super(httpCodeEnum.getDesc());
        this.code = httpCodeEnum.getCode();
        this.msg = httpCodeEnum.getDesc();
    }
}
