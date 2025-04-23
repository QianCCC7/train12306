package com.xiaoqian.batch.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobDTO {
    // 任务名
    private String name;
    // 任务所在组
    private String group;
    // 任务描述
    private String description;
    // cron表达式
    private String cronExpression;
}
