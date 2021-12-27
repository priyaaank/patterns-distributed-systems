package com.priyaaank.dspatterns.bookmarksmanager.bookmarks.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Slf4j
@Component
public class HTTPRetryHandler<T> {

    private Integer retryCount;

    public HTTPRetryHandler(@Value("${http.retry.count}") Integer retryCount) {
        this.retryCount = retryCount;
    }

    public ResponseEntity<T> callWithRetry(Supplier<ResponseEntity<T>> httpCall) {
        Integer currentRetryCount = 0;
        ResponseEntity<T> response = null;
        do {
            try {
                response = httpCall.get();
            } catch (RuntimeException re) {
                bubbleUpExceptionIfRetriesOver(currentRetryCount, re);
            } finally {
                currentRetryCount++;
            }
        } while (untilRetriesArePending(currentRetryCount, response));

        return response;
    }

    private void bubbleUpExceptionIfRetriesOver(Integer currentRetryCount, RuntimeException re) {
        log.error("Error occurred" + re.getMessage());
        if (currentRetryCount >= retryCount) throw re;
    }

    private boolean untilRetriesArePending(Integer currentRetryCount, ResponseEntity<T> response) {
        return (response == null || response.getStatusCode().is5xxServerError()) && currentRetryCount <= retryCount;
    }

}
