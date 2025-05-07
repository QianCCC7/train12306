package com.xiaoqian.business.config;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@ComponentScan("com.xiaoqian")
@MapperScan("com.xiaoqian.business.mapper")
@EnableFeignClients(basePackages = "com.xiaoqian.business.client")
@Slf4j
public class BusinessApplication {
    private final static Logger LOG = LoggerFactory.getLogger(BusinessApplication.class);
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(BusinessApplication.class);
        Environment environment = app.run(args).getEnvironment();
        LOG.info("business服务启动成功");
        LOG.info("地址: http://127.0.0.1:{}", environment.getProperty("server.port"));

        initFlowRules();
    }

    // sentinel限流规则
    private static void initFlowRules() {
        log.info("business模块初始化sentinel限流规则");
        List<FlowRule> rules = new ArrayList<>();
        FlowRule rule = new FlowRule();
        rule.setResource("testSentinel");
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule.setCount(1); // Set limit QPS
        rules.add(rule);
        FlowRuleManager.loadRules(rules);
    }
}
