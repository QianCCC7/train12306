package com.xiaoqian.batch.client;

import com.xiaoqian.batch.client.fallback.BusinessClientFallback;
import com.xiaoqian.common.domain.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;


@FeignClient(value = "business", path = "/business/admin", fallback = BusinessClientFallback.class)
public interface BusinessClient {
    @GetMapping("/daily-train/generateDailyTrain/{date}")
    ResponseResult<Void> generateDailyTrain(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date);

    @GetMapping("/confirm-order/testSentinel2")
    ResponseResult<String> testSentinel2();
}
