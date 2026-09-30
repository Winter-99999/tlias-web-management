package com.winter.exception;

/**
 * 业务异常。
 *
 * 用来区分两类完全不同的错误：
 *   ① 可预期的业务错误（"班主任不存在"、"结课时间不能早于开课时间"）
 *      —— 这些话是写给【用户】看的，应该原样返回给前端
 *   ② 程序 bug 类错误（空指针、SQL 语法错误、类型转换失败）
 *      —— 这些是写给【程序员】看的，暴露给用户既没意义也不安全
 *
 * 只有继承本类的异常，message 才会被原样返回给前端。
 * 其他异常统一走 GlobalExceptionHandler 的兜底，返回通用提示。
 */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
