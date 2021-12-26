package com.priyaaank.dspatterns.bookmarksmanager.bookmarks.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.SocketTimeoutException;
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
            } catch (RuntimeException ee) {
                log.error("Error occurred" + ee.getMessage());
                if(currentRetryCount >= retryCount) throw ee;
            } finally {
                currentRetryCount++;
            }
        } while ((response == null || response.getStatusCode().is5xxServerError()) && currentRetryCount <= retryCount);

        return response;
    }

}
