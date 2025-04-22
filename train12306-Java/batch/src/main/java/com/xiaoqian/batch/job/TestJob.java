package com.xiaoqian.batch.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

@Slf4j
public class TestJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext)  {
        log.info("TestJob");
    }
}
