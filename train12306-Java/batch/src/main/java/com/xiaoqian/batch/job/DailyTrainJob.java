package com.xiaoqian.batch.job;

import com.xiaoqian.batch.client.BusinessClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.time.LocalDate;

@Slf4j
@DisallowConcurrentExecution
@RequiredArgsConstructor
public class DailyTrainJob implements Job {
    private final BusinessClient businessClient;
    @Override
    public void execute(JobExecutionContext jobExecutionContext)  {
        log.info("生成当天所有车次数据开始");
        businessClient.generateDailyTrain(LocalDate.now());
        log.info("生成当天所有车次数据完成");
    }
}
