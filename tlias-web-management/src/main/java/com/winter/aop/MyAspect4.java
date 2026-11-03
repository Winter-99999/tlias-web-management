package com.winter.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

//@Aspect
@Component
@Slf4j
@Order(10)
public class MyAspect4 {

    @Pointcut("execution(* com.winter.controller.*.*(..))")    //匹配controller包下所有类的所有方法
    public void pointcut() {}

    // 环绕通知 方法执行前，后都会运行
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        log.info("around before");
        Object ret = pjp.proceed();
        log.info("around after");
        return ret;
    }

    // 前置通知 方法执行前运行
    @Before("pointcut()")
    public void before(JoinPoint joinPoint) {
        //获取目标对象
        Object target = joinPoint.getTarget();
        log.info("目标对象 {}", target);
        //获取目标类
        String className = joinPoint.getSignature().getClass().getName();
        log.info("目标类 {}", className);
        //获取目标方法
        String methodName = joinPoint.getSignature().getName();
        log.info("目标方法 {}", methodName);
        //获取目标方法参数
        Object[] args = joinPoint.getArgs();
        log.info("目标方法参数 {}", Arrays.toString(args));

    }
}
