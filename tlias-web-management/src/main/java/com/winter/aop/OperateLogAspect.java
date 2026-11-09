package com.winter.aop;

import com.winter.mapper.OperateLogMapper;
import com.winter.pojo.OperateLog;
import com.winter.utils.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import tools.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.winter.anno.Log;
@Aspect
@Component
@Slf4j
public class OperateLogAspect {
    /** 需要打码的字段名（统一转小写比较） */
    private static final Set<String> SENSITIVE =
            Set.of("password", "token", "idcard", "id_card", "secret");

    /** 与表字段 varchar(2000) 对齐 */
    private static final int MAX_LEN = 2000;

    @Autowired
    private OperateLogMapper operateLogMapper;
    @Autowired
    private ObjectMapper objectMapper;

    @Around("@annotation(logAnno)")  //匹配标了@Log 注解的方法，并把注解实例赋值给同名参数 logAnno
    public Object logOperation(ProceedingJoinPoint joinPoint,Log logAnno) throws Throwable {
        LocalDateTime operateTime = LocalDateTime.now();
        long startMs  = System.currentTimeMillis();

        Object result = null;
        Throwable error = null;

        try {
            result = joinPoint.proceed();
            return result;
        } catch (Throwable e) {
            error = e;
            throw e;
        } finally {
            // 写日志失败不能影响业务
            // 构建日志实体
            try {
                OperateLog operateLog = new OperateLog();
                operateLog.setOperateEmpId(getCurrentUserId());
                operateLog.setModule(logAnno.module());
                operateLog.setOperateType(logAnno.type());
                operateLog.setOperateTime(operateTime);
                operateLog.setClassName(joinPoint.getSignature().getDeclaringTypeName());
                operateLog.setMethodName(joinPoint.getSignature().getName());
                operateLog.setMethodParams(toSafeJson(joinPoint.getArgs()));
                operateLog.setReturnValue(toSafeJson(result));
                operateLog.setCostTime(System.currentTimeMillis() - startMs);

                if(error != null){  // 执行失败
                    operateLog.setStatus(0);
                    operateLog.setErrorMsg(truncate(error.getClass().getSimpleName()
                            + ": " + error.getMessage(), 500));
                }else{              // 执行成功
                    operateLog.setStatus(1);
                }
                operateLogMapper.insert(operateLog);
            } catch (Exception e) {
                // 日志写失败只降级，不打穿业务
                log.warn("操作日志保存失败，已忽略：{}", e.getMessage());
            }
        }
    }

    // 获取当前登录用户id，和截图保持一致
    private Integer getCurrentUserId() {
       return UserContext.getUserId();
    }
    /** 转 JSON -> 脱敏 -> 截断 */
    private String toSafeJson(Object value) {
        if (value == null) {
            return null;
        }
        try {
            Object plain = objectMapper.convertValue(value, Object.class); // 转为普通对象
            maskSensitive(plain);
            return truncate(objectMapper.writeValueAsString(plain), MAX_LEN);  // 转为 JSON
        } catch (Exception e) {
            // 序列化失败时退化，保证日志能存下来
            log.warn("操作日志 JSON 序列化失败，已降级为字符串：{}", e.toString());
            return truncate(String.valueOf(value), MAX_LEN);
        }
    }

    /** 递归脱敏：Map 按字段名判断，List 逐个处理，其他类型直接忽略 */
    @SuppressWarnings("unchecked")
    private void maskSensitive(Object node) {
        if (node instanceof Map) {          // 对象 → 看每个 key 是不是敏感字段
            Map<String, Object> map = (Map<String, Object>) node;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (SENSITIVE.contains(entry.getKey().toLowerCase())) {
                    entry.setValue("******");
                } else {
                    maskSensitive(entry.getValue());
                }
            }
        } else if (node instanceof List) {   // 数组 → 每个元素再递归
            for (Object item : (List<Object>) node) {
                maskSensitive(item);
            }
        }
    }

    private String truncate(String s, int max) {
        if (s == null || s.length() <= max) {
            return s;
        }
        return s.substring(0, max - 3) + "...";
    }
}
