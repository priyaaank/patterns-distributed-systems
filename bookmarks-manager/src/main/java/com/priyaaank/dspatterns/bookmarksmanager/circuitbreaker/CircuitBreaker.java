package com.priyaaank.dspatterns.bookmarksmanager.circuitbreaker;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.Function;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

@Slf4j
public class CircuitBreaker<P, T> {

    private Boolean isCircuitEnabled;
    private Function<P, T> func;
    private Function<P, T> failover;
    private BlockingQueue<ApiRequest> apiRequestStream;
    private Boolean isCircuitOpen = FALSE;

    public CircuitBreaker(Boolean isCircuitEnabled, Function<P, T> func) {
        this(isCircuitEnabled, func, null);
    }

    public CircuitBreaker(Boolean isCircuitEnabled, Function<P, T> func, Function<P, T> failOver) {
        this.isCircuitEnabled = isCircuitEnabled;
        this.func = func;
        this.failover = failOver;
        this.apiRequestStream = new LinkedBlockingDeque<>();
        if (isCircuitEnabled) {
            new Thread(new Circuit(this.apiRequestStream, 300000L, .10,
                    (val) -> isCircuitOpen = val, () -> isCircuitOpen)).start();
        }
    }

    public T check(P param) {
        //TODO - Below statement should be run only if failover is enabled
        //if (isCircuitEnabled && isCircuitOpen) return failover.apply(param);
        if (isCircuitEnabled) return safeguardExecWithCircuitBreaker(param);
        return passThrough(param);
    }

    private T passThrough(P param) {
        return func.apply(param);
    }

    private T safeguardExecWithCircuitBreaker(P param) {
        if (isCircuitOpen) throw new RuntimeException("The service is recovering. Please try again later");

        Long currentTime = System.currentTimeMillis();
        try {
            T retValue = func.apply(param);
            this.apiRequestStream.add(new ApiRequest(TRUE, currentTime));
            return retValue;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            this.apiRequestStream.add(new ApiRequest(FALSE, currentTime));
            throw ex;
        }
    }

}
