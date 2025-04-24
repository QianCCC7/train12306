package com.xiaoqian.batch.client;

import com.xiaoqian.common.domain.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;


@FeignClient(name = "business", url = "localhost:8000/business", path = "/admin/daily-train")
public interface BusinessClient {
    @GetMapping("/generateDailyTrain/{date}")
    ResponseResult<Void> generateDailyTrain(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date);
}
