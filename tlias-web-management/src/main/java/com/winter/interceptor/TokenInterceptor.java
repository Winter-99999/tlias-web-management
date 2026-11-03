package com.winter.interceptor;

import com.winter.utils.JwtUtil;
import com.winter.utils.UserContext;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
//  拦截器
//  如果过滤器和拦截器同时存在，过滤器优先级高于拦截器，先执行过滤器中的方法后继续执行拦截器中的方法
public class TokenInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //1.获取请求头中的token
        String token = request.getHeader("token");

        //2.检验token是否存在，如果不存在，返回状态码401
        if (token == null || token.isEmpty()) {
            log.info("token不存在，返回状态码401");
            response.setStatus(401);
            return false;
        }

        //3.如果token存在，检验token是否合法，如果不合法，返回状态码401
        try {
            Claims claims = JwtUtil.parseJwt(token);
            // 从claims取出id，存入ThreadLocal
            Integer empId = (Integer) claims.get("id");
            UserContext.setUserId(empId);
            log.info("当前登录用户id : {}", empId);
        } catch (Exception e) {
            log.info("令牌不合法，返回状态码401");
            response.setStatus(401);
            return false;
        }

        //4.如果合法，放行
        log.info("令牌合法，放行");
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        // 请求结束，清除ThreadLocal
        UserContext.remove();
    }
}
