package com.winter.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
//@WebFilter(urlPatterns = "/*")
public class DemoFilter implements Filter {

    //初始化方法，只在容器启动时执行一次
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("init方法  初始化");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("doFilter方法  拦截到了请求");
        chain.doFilter(request, response);
    }

    //销毁方法，只在容器销毁时才会执行
    @Override
    public void destroy() {
        log.info("destroy方法  销毁");
    }
}
