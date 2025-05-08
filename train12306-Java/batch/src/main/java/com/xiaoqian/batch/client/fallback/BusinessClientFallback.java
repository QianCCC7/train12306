package com.xiaoqian.batch.client.fallback;

import com.xiaoqian.batch.client.BusinessClient;
import com.xiaoqian.common.domain.ResponseResult;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class BusinessClientFallback implements BusinessClient {
    @Override
    public ResponseResult<Void> generateDailyTrain(LocalDate date) {
        return null;
    }

    @Override
    public ResponseResult<String> testSentinel2() {
        return ResponseResult.okResult("出现异常BusinessClientFallback");
    }
}
