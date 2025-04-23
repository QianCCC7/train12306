package com.xiaoqian.batch.service.impl;

import com.xiaoqian.batch.domain.dto.JobDTO;
import com.xiaoqian.batch.domain.vo.JobVo;
import com.xiaoqian.batch.service.JobService;
import com.xiaoqian.common.domain.ResponseResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {
    private final SchedulerFactoryBean schedulerFactory;

    @Override
    public ResponseResult<Void> add(JobDTO jobDTO) {
        String name = jobDTO.getName();
        String group = jobDTO.getGroup();
        String cron = jobDTO.getCronExpression();
        String desc = jobDTO.getDescription();
        log.info("定时任务开始:{}, {}, {}, {}", name, group, cron, desc);
        try {
            // 通过SchedulerFactory获取一个调度器实例
            Scheduler scheduler = schedulerFactory.getScheduler();
            // 启动调度器
            scheduler.start();
            // 声明job任务
            JobDetail jobDetail = JobBuilder.newJob((Class<? extends Job>) Class.forName(name))
                    .withIdentity(name, group).build();
            // 触发器执行任务
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
            CronTrigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(name, group)
                    .withDescription(desc)
                    .withSchedule(scheduleBuilder).build();
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            log.error("添加定时任务失败:{}", e.toString());
            return ResponseResult.errorResult(400, "创建定时任务失败:调度异常");
        } catch (ClassNotFoundException e) {
            log.error("添加定时任务失败:{}", e.toString());
            return ResponseResult.errorResult(400, "创建定时任务失败:任务类不存在");
        }

        return ResponseResult.okEmptyResult();
    }

    @Override
    public ResponseResult<Void> pause(JobDTO jobDTO) {
        String name = jobDTO.getName();
        String group = jobDTO.getGroup();
        log.info("暂停任务:{}, {}", name, group);
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            scheduler.pauseJob(JobKey.jobKey(name, group));
        } catch (SchedulerException e) {
            log.error("暂停定时任务失败:{}", e.toString());
            return ResponseResult.errorResult(400, "暂停定时任务失败:调度异常");
        }

        return ResponseResult.okEmptyResult();
    }

    @Override
    public ResponseResult<Void> resume(JobDTO jobDTO) {
        String name = jobDTO.getName();
        String group = jobDTO.getGroup();
        log.info("重启任务:{}, {}", name, group);
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            scheduler.resumeJob(JobKey.jobKey(name, group));
        } catch (SchedulerException e) {
            log.error("重启定时任务失败:{}", e.toString());
            return ResponseResult.errorResult(400, "重启定时任务失败:调度异常");
        }

        return ResponseResult.okEmptyResult();
    }

    @Override
    public ResponseResult<Void> reschedule(JobDTO jobDTO) {
        String name = jobDTO.getName();
        String group = jobDTO.getGroup();
        String cron = jobDTO.getCronExpression();
        String desc = jobDTO.getDescription();
        log.info("更新任务开始:{}, {}, {}, {}", name, group, cron, desc);
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(name, group);
            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
            CronTriggerImpl trigger1 = (CronTriggerImpl) scheduler.getTrigger(triggerKey);
            trigger1.setStartTime(new Date()); // 重新设置开始时间
            CronTrigger trigger = trigger1;

            // 按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey)
                    .withDescription(desc)
                    .withSchedule(scheduleBuilder)
                    .build();

            // 按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, trigger);
        } catch (SchedulerException e) {
            log.error("更新定时任务失败:{}", e.toString());
            return ResponseResult.errorResult(400, "更新定时任务失败:调度异常");
        }

        return ResponseResult.okEmptyResult();
    }

    @Override
    public ResponseResult<Void> delete(JobDTO jobDTO) {
        String name = jobDTO.getName();
        String group = jobDTO.getGroup();
        log.info("删除任务:{}, {}", name, group);
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            scheduler.pauseTrigger(TriggerKey.triggerKey(name, group));
            scheduler.unscheduleJob(TriggerKey.triggerKey(name, group));
            scheduler.deleteJob(JobKey.jobKey(name, group));
        } catch (SchedulerException e) {
            log.error("删除定时任务失败:{}", e.toString());
            return ResponseResult.errorResult(400, "删除定时任务失败:调度异常");
        }

        return ResponseResult.okEmptyResult();
    }

    @Override
    public ResponseResult<List<JobVo>> query() {
        log.info("查找所有任务");
        List<JobVo> jobVoList = new ArrayList<>();
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            for (String jobGroupName : scheduler.getJobGroupNames()) {
                for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(jobGroupName))) {
                    List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
                    CronTrigger cronTrigger = (CronTrigger) triggers.get(0);
                    Trigger.TriggerState triggerState = scheduler.getTriggerState(cronTrigger.getKey());
                    jobVoList.add(new JobVo(jobKey.getName(), jobKey.getGroup(), cronTrigger.getDescription(), triggerState.name(),
                            cronTrigger.getCronExpression(), cronTrigger.getNextFireTime(), cronTrigger.getPreviousFireTime()));
                }
            }
        } catch (SchedulerException e) {
            log.error("查找所有定时任务失败:{}", e.toString());
            return ResponseResult.errorResult(400, "查找所有定时任务失败:调度异常");
        }
        return ResponseResult.okResult(jobVoList);
    }

    @Override
    public ResponseResult<Void> run(JobDTO jobDTO) {
        String name = jobDTO.getName();
        String group = jobDTO.getGroup();
        log.info("手动执行任务:{}, {}", name, group);
        try {
            schedulerFactory.getScheduler().triggerJob(JobKey.jobKey(name, group));
        } catch (SchedulerException e) {
            log.error("手动执行定时任务失败:{}", e.toString());
            return ResponseResult.errorResult(400, "手动执行定时任务失败:调度异常");
        }

        return ResponseResult.okEmptyResult();
    }


}
