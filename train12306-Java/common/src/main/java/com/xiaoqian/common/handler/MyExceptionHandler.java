package com.xiaoqian.common.handler;

import com.xiaoqian.common.domain.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseResult<Void> exceptionHandler(Exception ex) {
        log.debug("出现异常：{}", ex.getMessage());
        return ResponseResult.errorResult(500, ex.getMessage());
    }
}
