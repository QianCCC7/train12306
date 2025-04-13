package com.xiaoqian.member.aspect;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.spring.PropertyPreFilters;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@Aspect
//@Component
public class LogAspect {
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    /**
     * 作用于所有com.xiaoqian包及其子包下的Controller
     */
    @Pointcut("execution(public * com.xiaoqian..*Controller.*(..))")
    public void controllerPointcut() {}

    /*
     * 前置通知
     */
    @Before("controllerPointcut()")
    public void doBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Signature signature = joinPoint.getSignature();
        String name = signature.getName();

        logger.info("------------ 请求开始 ------------");
        logger.info("请求地址：{}", request.getRequestURL().toString());
        logger.info("类名方法：{}.{}", signature.getDeclaringTypeName(), name);
        logger.info("远程地址：{}", request.getRemoteAddr());
        Object[] args = joinPoint.getArgs();
//        logger.info("请求参数：{}", JSONObject.toJSONString(args));

        // 排除特殊类型的参数，如文件类型
        Object[] arguments = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof ServletRequest
                    || args[i] instanceof ServletResponse
                    || args[i] instanceof MultipartFile) {
                continue;
            }
            arguments[i] = args[i];
        }

        // 排除字段，敏感字段或太长的字段不显示：身份证、手机号、邮箱、密码等
        String[] excludeProperties = {"idNumber", "mobilePhone", "emailAddress", "password"};
        PropertyPreFilters filters = new PropertyPreFilters();
        PropertyPreFilters.MySimplePropertyPreFilter excludeFilter = filters.addFilter();
        excludeFilter.addExcludes(excludeProperties);
        logger.info("请求参数: {}", JSONObject.toJSONString(arguments, excludeFilter));
    }

    @Around("controllerPointcut()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        // 排除字段，敏感字段或太长的字段不显示：身份证、手机号、邮箱、密码等
        String[] excludeProperties = {"idNumber", "mobilePhone", "emailAddress", "password"};
        PropertyPreFilters filters = new PropertyPreFilters();
        PropertyPreFilters.MySimplePropertyPreFilter excludeFilter = filters.addFilter();
        excludeFilter.addExcludes(excludeProperties);
        logger.info("返回结果: {}", JSONObject.toJSONString(result, excludeFilter));
        logger.info("------------ 请求结束，总耗时：{}ms ------------", System.currentTimeMillis() - startTime);
        return result;
    }

}
