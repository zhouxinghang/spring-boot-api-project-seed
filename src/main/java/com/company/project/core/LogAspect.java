package com.company.project.core;

import com.alibaba.fastjson.JSON;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zhouxinghang
 * @date 2019-09-17
 */
@Profile({"dev", "test"})
@Component
@Aspect
@Slf4j
public class LogAspect {
    /**
     * 定义一个公共的方法，实现切入点
     * 拦截Controller下面的所有方法  任何参数(..表示拦截任何参数)
     * 以@RestController注解作为切入点  可切入其他业务模块的方法
     *
     * @within和@target针对类的注解,
     * @annotation是针对方法的注解，为自定义注解
     */
    /*    @Pointcut("execution(public * com.*.controller..*.*(..))")*/
    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void log() {
    }

    /**
     * 拦截方法之前的一段业务逻辑
     *
     * @param joinPoint
     */
    @Before("log()")
    public void doBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        Map params = new LinkedHashMap(10);
        params.put("url", request.getRequestURL()); // 获取请求的url
        params.put("method", request.getMethod()); // 获取请求的方式
        params.put("args", joinPoint.getArgs()); // 请求参数
        params.put("className", joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName()); // 获取类名和获取类方法
        params.put("ip", request.getRemoteAddr()); // 获取请求的ip地址

        // 输出格式化后的json字符串
        log.info("\n{} 日志请求参数: \n{}\n", request.getRequestURI(), JSON.toJSONString(params));
    }

    /**
     * 获取响应返回值  方法执行return之后
     *
     * @param object
     */
    @AfterReturning(returning = "object", pointcut = "log()")
    public void doAfterReturning(Object object) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 会打印出一个对象，想打印出具体内容需要在定义模型处加上toString()
        log.info("\n{} 日志响应结果: \n{}\n", request.getRequestURI(), object.toString());
    }

    /**
     * 环绕通知  在方法的调用前、后执行
     */
    @Around("log()")
    public Object doAround(ProceedingJoinPoint point) throws Throwable {
        //开始时间
        long begin = System.currentTimeMillis();
        //方法环绕proceed结果
        Object obj = point.proceed();
        //结束时间
        long end = System.currentTimeMillis();
        //时间差
        long timeDiff = (end - begin);
        String msg = "\n方法性能分析: 执行耗时 {}毫秒  来自Dream PWJ的表情 ";
        if (timeDiff < 200) {
            log.info(msg + "\uD83D\uDE02 \n", timeDiff);
        } else {
            log.warn(msg + "\uD83D\uDE31 \n", timeDiff);
        }
        return obj;
    }

    /**
     * 拦截方法之后的一段业务逻辑
     */
    @After("log()")
    public void doAfter() {
        log.info("doAfter");
    }

}
