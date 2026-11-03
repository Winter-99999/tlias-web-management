package com.winter.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

//@Aspect
@Component
@Slf4j
public class MyAspect3 {

    /*
     *单个独立的任意符号 ..多个连续的任意符号
     execution(权限修饰符 返回值类型 包名.类名.方法名(参数列表) throws (异常) )
     权限修饰符  包名.类名 throws (异常) 可省略
     不建议省略 包名.类名
    * */
    //@Before("execution(* com.winter.controller.*.*(..))")
    //@Before("execution(public * com.winter.service.Impl.StudentServiceImpl.getById(*))")
    //@Before("execution(public * com.winter.service.Impl.StudentServiceImpl.get*(Integer))")
//    @Before("execution(public * com.winter.service.Impl.StudentServiceImpl.get*(Integer)) ||" +
//            "execution(* com.winter.service.Impl.StudentServiceImpl.delete(*))")

    @Before("@annotation(com.winter.ann.MyAnnoation)")  //在想要指定的方法上添加注解 如@MyAnnoation
    public void before() {
        log.info("before");
    }

}
