package com.winter.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

//@Aspect
@Component
@Slf4j
@Order(5)
public class MyAspect2 {
    //切入点
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
    public void before() {
        log.info("before");
    }

    // 后置通知 方法执行后运行 不管成功异常都执行
    @After("pointcut()")
    public void after() {
        log.info("after");
    }

    // 返回后通知 方法返回后运行，如果有异常，则不会运行
    @AfterReturning("pointcut()")
    public void afterReturning() {
        log.info("afterReturning");
    }

    // 异常后通知 只在方法抛出异常时运行
    @AfterThrowing("pointcut()")
    public void afterThrowing() {
        log.info("afterThrowing");
    }

}
