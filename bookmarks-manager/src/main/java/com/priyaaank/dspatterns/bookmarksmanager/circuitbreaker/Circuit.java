package com.priyaaank.dspatterns.bookmarksmanager.circuitbreaker;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

@Slf4j
public class Circuit implements Runnable {

    private static final Boolean OPEN = TRUE;
    private static final Boolean CLOSED = FALSE;
    private static final long RESET_WINDOW_MILLIS = 30000L;
    private Set<ApiRequest> requestHistory;
    private BlockingQueue<ApiRequest> apiCallStream;
    private Long windowTimeMillis;
    private Double failureThreshold;
    private Consumer<Boolean> toggleCircuit;
    private Supplier<Boolean> isCircuitOpen;
    private Long lastCircuitToggledTime;

    public Circuit(BlockingQueue<ApiRequest> apiCallStream, Long windowTimeMillis, Double failureThreshold,
                   Consumer<Boolean> toggleCircuit, Supplier<Boolean> isCircuitOpen) {
        this.apiCallStream = apiCallStream;
        this.windowTimeMillis = windowTimeMillis;
        this.failureThreshold = failureThreshold;
        this.toggleCircuit = toggleCircuit;
        this.isCircuitOpen = isCircuitOpen;
        this.requestHistory = new TreeSet<>();
        this.lastCircuitToggledTime = System.currentTimeMillis();
    }

    @SneakyThrows
    @Override
    public void run() {
        Long currentTime;
        ApiRequest apiReq;
        while (true) {
            if ((apiReq = this.apiCallStream.poll(RESET_WINDOW_MILLIS, TimeUnit.MILLISECONDS)) != null)
                requestHistory.add(apiReq);
            currentTime = System.currentTimeMillis();
            dropOldRequests(currentTime);
            reviewCircuitState(currentTime);
        }
    }

    private void dropOldRequests(Long currentTime) {
        log.debug("Old size {}", this.requestHistory.size());
        this.requestHistory = this.requestHistory
                .stream()
                .filter(status -> (currentTime - status.epochInMillis) < windowTimeMillis)
                .collect(Collectors.toSet());
        log.debug("New size {}", this.requestHistory.size());
    }

    private void reviewCircuitState(Long currentTime) {
        if (isCircuitOpen.get() && (isFailureRatioBelowThreshold() || isTimeToResetCircuit(currentTime)))
            markCircuit(CLOSED, currentTime);

        if (!isCircuitOpen.get() && !isFailureRatioBelowThreshold())
            markCircuit(OPEN, currentTime);
    }

    private void markCircuit(Boolean isOpen, Long currentTime) {
        log.info("Circuit breaker is now {}!", isOpen ? "Open" : "Closed");
        toggleCircuit.accept(isOpen);
        requestHistory.clear();
        this.lastCircuitToggledTime = currentTime;
    }

    private boolean isTimeToResetCircuit(Long currentTime) {
        //every 30 seconds
        return (currentTime - lastCircuitToggledTime) >= RESET_WINDOW_MILLIS;
    }

    public Boolean isFailureRatioBelowThreshold() {
        long successCount = this.requestHistory
                .stream()
                .filter(ApiRequest::isSuccess)
                .count();

        if (this.requestHistory.size() == 0 || successCount == this.requestHistory.size()) return true;
        boolean isBelowThreshold = ((1 - (successCount * 1.0 / this.requestHistory.size())) < failureThreshold);

        log.debug("Total success {} | Total count {} | Is failure below threshold: {}", successCount, requestHistory.size(), isBelowThreshold);
        return isBelowThreshold;
    }

}