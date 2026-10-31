package com.winter.filter;

import com.winter.utils.JwtUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
//@WebFilter(urlPatterns = "/*") //拦截所有请求

//过滤器执行顺序 放行前->放行->资源->放行后
public class JwtFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        //1.获取请求体路径
        String requestURI = req.getRequestURI();
        //2.判断是否为登录请求，如果是，直接放行
        if (requestURI.equals("/login")) {
            log.info("登录请求，放行");
            chain.doFilter(request, response);
            return;
        }
        //3.获取请求头中的token
        String token = req.getHeader("token");
        //4.检验token是否存在，如果不存在，返回状态码401
        if (token == null || token.isEmpty()) {
            log.info("token不存在，返回状态码401");
            resp.setStatus(401);
            return;
        }
        //5.如果token存在，检验token是否合法，如果不合法，返回状态码401
        try {
            JwtUtil.parseJwt(token);
        } catch (Exception e) {
            log.info("令牌不合法，返回状态码401");
            resp.setStatus(401);
            return;
        }
        //6.如果合法，放行
        log.info("令牌合法，放行");
        chain.doFilter(request, response);
    }
}
