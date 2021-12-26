package com.priyaaank.dspatterns.tagging.tags.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig implements WebMvcConfigurer {

    @Value("${config.failurePercent}")
    Integer failurePercent;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HttpRequestDelayInjector(failurePercent));
//                .addPathPatterns("/myendpoint");
    }


}
