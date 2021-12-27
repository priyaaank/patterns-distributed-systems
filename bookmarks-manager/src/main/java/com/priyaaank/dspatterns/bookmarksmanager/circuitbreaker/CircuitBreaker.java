package com.priyaaank.dspatterns.bookmarksmanager.circuitbreaker;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.BiFunction;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

@Slf4j
public class CircuitBreaker<P1, P2, T> {

    private BiFunction<P1, P2, T> func;
    private BlockingQueue<ApiRequest> apiRequestStream;
    private Boolean isCircuitOpen = FALSE;

    public CircuitBreaker(BiFunction<P1, P2, T> func) {
        this.func = func;
        this.apiRequestStream = new LinkedBlockingDeque<>();
        new Thread(new Circuit(this.apiRequestStream, 300000L, .10,
                (val) -> isCircuitOpen = val, () -> isCircuitOpen)).start();
    }

    public T check(P1 paramsOne, P2 paramTwo) {
        if (isCircuitOpen) throw new RuntimeException("The service is recovering. Please try again later");

        Long currentTime = System.currentTimeMillis();
        try {
            T retValue = func.apply(paramsOne, paramTwo);
            this.apiRequestStream.add(new ApiRequest(TRUE, currentTime));
            return retValue;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            this.apiRequestStream.add(new ApiRequest(FALSE, currentTime));
            throw ex;
        }
    }

}
