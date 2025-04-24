package com.xiaoqian.batch.client;

import com.xiaoqian.common.domain.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;


@FeignClient(name = "business", url = "localhost:8000/business/", path = "/admin/train")
public interface BusinessClient {
    @GetMapping("/hello")
    ResponseResult<String> hello();
}
