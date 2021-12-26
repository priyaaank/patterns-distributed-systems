package com.priyaaank.dspatterns.tagging.tags.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Random;

@Component
public class HttpRequestDelayInjector implements HandlerInterceptor {

    private Integer failurePercent;
    private Random randomNumberGenerator;

    @Autowired
    public HttpRequestDelayInjector(@Value("${config.failurePercent}") Integer failurePercent) {
        this.failurePercent = failurePercent;
        this.randomNumberGenerator = new Random();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        introduceProbabilisticDelay();
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    private void introduceProbabilisticDelay() throws InterruptedException {
        if (failurePercent >= this.randomNumberGenerator.nextInt(100)) {
            Thread.sleep(10000);
        }
    }

}
