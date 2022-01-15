package com.priyaaank.dspatterns.bookmarksmanager.bookmarks.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Slf4j
@Component
public class HTTPRetryHandler<T> {

    private Integer maxRetryCount;

    public HTTPRetryHandler(@Value("${http.retry.count}") Integer maxRetryCount) {
        this.maxRetryCount = maxRetryCount;
    }

    public ResponseEntity<T> callWithRetry(Supplier<ResponseEntity<T>> httpCall) {
        Integer currentRetryCount = -1;
        ResponseEntity<T> response = null;
        do {
            try {
                currentRetryCount++;
                response = httpCall.get();
            } catch (RuntimeException re) {
                bubbleUpExceptionIfRetriesOver(currentRetryCount, re);
            }
        } while (untilRetriesArePending(currentRetryCount, response));

        return response;
    }

    private void bubbleUpExceptionIfRetriesOver(Integer currentRetryCount, RuntimeException re) {
        log.error("Error occurred" + re.getMessage());
        if (currentRetryCount >= maxRetryCount) throw re;
    }

    private boolean untilRetriesArePending(Integer currentRetryCount, ResponseEntity<T> response) {
        return (response == null || response.getStatusCode().is5xxServerError()) && currentRetryCount < maxRetryCount;
    }

}
