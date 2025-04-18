package com.xiaoqian.gateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

@SpringBootApplication
@ComponentScan("com.xiaoqian")
public class GatewayApplication {
    private final static Logger LOG = LoggerFactory.getLogger(GatewayApplication.class);
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(GatewayApplication.class);
        Environment environment = app.run(args).getEnvironment();
        LOG.info("gateway网关服务启动成功");
        LOG.info("地址: http://127.0.0.1:{}", environment.getProperty("server.port"));
    }

}
