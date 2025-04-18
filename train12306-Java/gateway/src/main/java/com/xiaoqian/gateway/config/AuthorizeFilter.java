package com.xiaoqian.gateway.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiaoqian.gateway.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Order(0)
@Component
public class AuthorizeFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        log.info("path:{}", request.getPath());
//        log.info("uri:{}", request.getURI());
//        log.info("path2:{}", request.getURI().getPath());
        // 排除不需要拦截的请求
        String path = request.getURI().getPath();
        if (path.contains("/admin")
                || path.contains("/member/member/login")
                || path.contains("/member/member/sendCode")
                || path.contains("/member/member/register")) {
            return chain.filter(exchange);
        }
        String token = request.getHeaders().getFirst("token");
        // 校验token是否有效
        if (!StringUtils.hasText(token) || !JwtUtil.validate(token)) {
            log.error("用户未登录");
            // 构造自定义返回体
            Map<String, Object> data = new HashMap<>();
            data.put("code", 401);
            data.put("data", null);
            data.put("msg", "用户未登录或Token无效");
            byte[] responseBytes;
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                responseBytes = objectMapper.writeValueAsBytes(data);
            } catch (Exception e) {
                responseBytes = "{\"code\":500,\"msg\":\"响应序列化失败\"}".getBytes(StandardCharsets.UTF_8);
            }
            // 设置响应头
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED); // 可选
            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(responseBytes);
            return exchange.getResponse().writeWith(Mono.just(buffer)); // 拦截
        }
        log.info("AuthorizeFilter通过登录认证");
        return chain.filter(exchange);
    }
}
