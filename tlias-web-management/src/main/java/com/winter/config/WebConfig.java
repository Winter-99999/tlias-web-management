package com.winter.config;

import com.winter.interceptor.DemoInterceptor;
import com.winter.interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    //private DemoInterceptor demoInterceptor;
    private TokenInterceptor tokenInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/**")   //拦截所有请求
                .excludePathPatterns("/login"); //排除登录接口
    }
     /* /** 拦截所有请求 /depts /depts/2
     /* 只能拦截一级路径 /depts

      */
}
