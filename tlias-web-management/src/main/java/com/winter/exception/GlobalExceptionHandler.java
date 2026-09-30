package com.winter.exception;

import com.winter.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.stream.Collectors;

//全局异常处理器
//
//匹配规则：Spring 会选择【最具体】的处理器。
//例如 DuplicateKeyException 同时满足 ②和⑦，但 ②更具体，所以走 ②。
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    //① 业务异常：message 是写给用户看的，原样返回
    @ExceptionHandler(BusinessException.class)
    public Result handleBusinessException(BusinessException e) {
        log.error("业务异常：{}", e.getMessage());
        return Result.error(e.getMessage());
    }

    //② 唯一约束冲突（学号、手机号、用户名重复）
    @ExceptionHandler(DuplicateKeyException.class)
    public Result handleDuplicateKey(DuplicateKeyException e) {
        log.error("数据重复", e);
        return Result.error("数据已存在，请检查学号/手机号是否重复");
    }

    //③ POJO 参数绑定失败，例如 /clazzs?page=abc（ClazzQueryParam 的 page 转不成 Integer）
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleArgumentNotValid(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldErrors().stream()
                .map(fe -> "参数【" + fe.getField() + "】格式不正确")
                .distinct()
                .collect(Collectors.joining("；"));
        log.error("参数绑定失败：{}", e.getMessage());
        return Result.error(msg.isEmpty() ? "请求参数格式不正确" : msg);
    }

    //④ 简单类型参数转换失败，例如 /clazzs/abc（路径变量 id 转不成 Integer）
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        log.error("参数类型错误：{}", e.getMessage());
        return Result.error("参数【" + e.getName() + "】格式不正确");
    }

    //⑤ 请求方法不支持，例如用 GET 调一个只支持 POST 的接口
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Result> handleMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        log.warn("请求方法不支持：{}", e.getMessage());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(Result.error("请求方法不支持"));
    }

    //⑥ 请求路径不存在：保持真正的 404，不要被⑦伪装成业务错误
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Result> handleNotFound(NoResourceFoundException e) {
        log.warn("请求路径不存在：{}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Result.error("请求的资源不存在"));
    }

    //⑦ 兜底：真正的未知异常（空指针、SQL错误等），只给通用提示，详情进日志
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        log.error("系统异常", e);
        return Result.error("出错了，请联系管理员");
    }
}
