package com.xiaoqian.batch.config;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;

// 将quartz任务集成进来的触发配置，相当于QuartzConfig的触发器配置
@Configuration
public class SchedulerConfig {
    @Resource
    private MyJobFactory myJobFactory;

    /**
     * @param dataSource 此处的dataSource就是在spring配置文件中设置的数据库连接
     * @return factory 用于创建调度策略的工厂。
     */
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(@Qualifier("dataSource") DataSource dataSource) {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setDataSource(dataSource);
        factory.setJobFactory(myJobFactory);
        factory.setStartupDelay(5); // 程序启动（2）秒之后开始执行quartz
        return factory;
    }
}
