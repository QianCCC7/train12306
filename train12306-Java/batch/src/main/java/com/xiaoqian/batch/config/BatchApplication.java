package com.xiaoqian.batch.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

@SpringBootApplication
@ComponentScan("com.xiaoqian")
public class BatchApplication {
    private final static Logger LOG = LoggerFactory.getLogger(BatchApplication.class);
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(BatchApplication.class);
        Environment environment = app.run(args).getEnvironment();
        LOG.info("batch服务启动成功");
        LOG.info("地址: http://127.0.0.1:{}", environment.getProperty("server.port"));
    }

}
