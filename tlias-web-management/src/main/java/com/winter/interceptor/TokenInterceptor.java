package com.winter.interceptor;

import com.winter.anno.RequireRole;
import com.winter.utils.JwtUtil;
import com.winter.utils.UserContext;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@Component
//  拦截器
//  如果过滤器和拦截器同时存在，过滤器优先级高于拦截器，先执行过滤器中的方法后继续执行拦截器中的方法
public class TokenInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtUtil jwtUtil;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //1.获取请求头中的token
        String token = request.getHeader("token");

        //2.检验token是否存在，如果不存在，返回状态码401
        if (token == null || token.isEmpty()) {
            log.info("token不存在，返回状态码401");
            response.setStatus(401);
            UserContext.remove();
            return false;
        }

        //3.如果token存在，检验token是否合法，如果不合法，返回状态码401
        try {
            Claims claims = jwtUtil.parseJwt(token);
            // 从claims取出id，存入ThreadLocal
            Integer empId = (Integer) claims.get("id");
            UserContext.setUserId(empId);
            Object roleClaim = claims.get("role");
            UserContext.setRole(roleClaim instanceof Number number ? number.intValue() : null);
            log.info("当前登录用户id : {}, 角色 : {}", empId, UserContext.getRole());
        } catch (Exception e) {
            log.info("令牌不合法，返回状态码401");
            response.setStatus(401);
            UserContext.remove();
            return false;
        }
        //4.令牌合法，再校验接口上声明的角色权限
        // 判断当前处理器是否为Controller方法（静态资源等不是HandlerMethod，跳过角色校验）
        if (handler instanceof HandlerMethod handlerMethod) {
            // 优先获取【方法上】的@RequireRole注解
            RequireRole requireRole = handlerMethod.getMethodAnnotation(RequireRole.class);
            // 如果方法上没有该注解，则去Controller【类上】获取注解
            if (requireRole == null) {
                requireRole = handlerMethod.getBeanType().getAnnotation(RequireRole.class);
            }
            // 存在注解才需要进行角色校验；无注解代表该接口不限制角色，直接放行
            if (requireRole != null) {
                Integer role = UserContext.getRole();
                /*
                 * 权限判断：
                 * 1. 用户角色不能为null
                 * 2. 当前用户角色 匹配注解允许角色数组中的任意一个
                 * 满足两个条件才允许访问
                 */
                boolean allowed = role != null
                        && Arrays.stream(requireRole.value()).anyMatch(r -> r == role);
                // 校验不通过，无访问权限
                if (!allowed) {
                    log.info("角色 {} 无权限访问 {}", role, request.getRequestURI());
                    writeJson(response, 403, "无权限访问该功能");
                    UserContext.remove();
                    return false;
                }
            }
        }
        log.info("令牌合法，放行");
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        // 请求结束，清除ThreadLocal
        UserContext.remove();
    }
    private void writeJson(HttpServletResponse response, int status, String msg) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"code\":0,\"msg\":\"" + msg + "\",\"data\":null}");
    }
}
