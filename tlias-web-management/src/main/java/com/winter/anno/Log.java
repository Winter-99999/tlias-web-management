package com.winter.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {
    // 所属模块，如 "学员管理"
    String module() default "";

    // 操作类型，如 "新增" / "修改" / "删除"
    String type() default "";
}
