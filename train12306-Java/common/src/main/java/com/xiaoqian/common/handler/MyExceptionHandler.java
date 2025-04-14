package com.xiaoqian.common.handler;

import com.xiaoqian.common.domain.ResponseResult;
import com.xiaoqian.common.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class MyExceptionHandler {

    /**
     * 业务异常
     */
    @ExceptionHandler(BizException.class)
    public ResponseResult<Void> exceptionHandler(BizException ex) {
        log.debug("出现业务异常：{}", ex.getMessage());
        return ResponseResult.errorResult(400, ex.getMsg());
    }

    /**
     * validation校验异常
     */
    @ExceptionHandler(BindException.class)
    public ResponseResult<Void> exceptionHandler(BindException ex) {
        log.debug("出现校验异常：{}", ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return ResponseResult.errorResult(422, ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    /**
     * 所有异常统一处理
     */
    @ExceptionHandler(Exception.class)
    public ResponseResult<Void> exceptionHandler(Exception ex) {
        log.debug("系统出现异常：{}", ex.getMessage());
        return ResponseResult.errorResult(500, "系统出现异常，请联系管理员");
    }
}
