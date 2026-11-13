package com.winter.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 接口权限注解：声明允许访问的角色编号。
 * 方法上的配置优先；方法没配时取类上的配置。
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)   // 必须是 RUNTIME，否则反射读不到，注解等于没写
public @interface RequireRole {
    int[] value();
}