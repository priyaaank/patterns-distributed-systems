package com.priyaaank.dspatterns.urishortner.bookmarks.service;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DeferredResultRegistry<T> {

    private Map<String, DeferredResult<T>> registry;

    public DeferredResultRegistry() {
        this.registry = new ConcurrentHashMap<>();
    }

    public void addToRegistry(String key, DeferredResult<T> deferredResult) {
        this.registry.put(key, deferredResult);
    }

    public Boolean updateResult(String key, T response) {
        DeferredResult<T> result = this.registry.get(key);
        return result == null ? Boolean.TRUE : result.setResult(response);
    }

}
