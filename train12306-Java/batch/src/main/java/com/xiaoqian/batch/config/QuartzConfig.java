package com.xiaoqian.batch.config;

import com.xiaoqian.batch.job.TestJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;

//@Configuration
public class QuartzConfig {
    // 声明一个任务
    @Bean
    public JobDetail jobDetail() {
        return JobBuilder.newJob(TestJob.class) // 具体执行的任务
                .withIdentity("TestJob", "testGroup") // 为任务分配任务名和所在组
                .storeDurably().build();
    }

    // 声明一个触发器，什么时候执行任务
    @Bean
    public Trigger trigger() {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail()) // 具体执行的任务
                .withIdentity("trigger", "triggerGroup") // 为触发器分配触发器名和所在组
                .startNow() // 立即执行
                .withSchedule(CronScheduleBuilder.cronSchedule("*/2 * * * * ?")) // cron表达式
                .build();
    }
}
