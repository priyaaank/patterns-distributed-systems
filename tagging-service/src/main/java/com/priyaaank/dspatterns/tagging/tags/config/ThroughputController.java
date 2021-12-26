package com.priyaaank.dspatterns.tagging.tags.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.IntStream;

@Component
@Slf4j
public class ThroughputController<T> {

    private LinkedBlockingDeque<String> tokenQueue;
    private Integer configuredTPS;
    private Integer delayInMillis;
    private ScheduledThreadPoolExecutor threadPool;

    public ThroughputController(@Value("${config.tps}") Integer configuredTPS,
                                @Value("${config.tps.penalty.delay.millis}") Integer delayInMillis) {
        this.configuredTPS = configuredTPS;
        this.delayInMillis = delayInMillis;
        tokenQueue = new LinkedBlockingDeque<>();
    }

    @PostConstruct
    public void initiateTPSTokenGeneration() {
        threadPool = new ScheduledThreadPoolExecutor(1);
        Runnable generateTokens = () -> {
            log.debug("Adding tokens to the TPS controller. Current size {}", tokenQueue.size());
            IntStream.range(0, configuredTPS).forEach((e) -> tokenQueue.offer("token"));
        };
        threadPool.scheduleAtFixedRate(generateTokens, 0, 1000, TimeUnit.MILLISECONDS);
    }

    @PreDestroy
    public void stopTokenGeneration() {
        this.threadPool.shutdown();
    }

    public T regulate(Supplier<T> supplierFunc) throws InterruptedException {
        tokenQueue.poll(delayInMillis, TimeUnit.MILLISECONDS);
        return supplierFunc.get();
    }

}
