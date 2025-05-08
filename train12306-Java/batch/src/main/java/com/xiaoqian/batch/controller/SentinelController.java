package com.xiaoqian.batch.controller;

import com.xiaoqian.batch.client.BusinessClient;
import com.xiaoqian.common.domain.ResponseResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/sentinel")
@RequiredArgsConstructor
@Slf4j
public class SentinelController {
    private final BusinessClient businessClient;

    @GetMapping("/testSentinel")
    public ResponseResult<String> hello() {
        ResponseResult<String> stringResponseResult = businessClient.testSentinel2();
        log.info("business:{}", stringResponseResult.getData());
        return ResponseResult.okResult("batch模块测试sentinel熔断");
    }
}
