package com.xiaoqian.common.exception;

import com.xiaoqian.common.enums.HttpCodeEnum;

public class BizException extends BaseException{
    public BizException(HttpCodeEnum httpCodeEnum) {
        super(httpCodeEnum);
    }
}
