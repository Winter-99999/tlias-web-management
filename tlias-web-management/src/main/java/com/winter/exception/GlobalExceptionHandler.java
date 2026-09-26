package com.winter.exception;

import com.winter.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//全局异常处理器

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler
    public Result handleException(Exception e){
        log.info("程序出错了");
        return Result.error("出错了，请联系管理员");
    }
}
