package com.priyaaank.dspatterns.urishortner.bookmarks.service;

import com.priyaaank.dspatterns.urishortner.bookmarks.domain.Url;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UrlTextExtractorService {

    private LinkedBlockingDeque<Runnable> tokenQueue;
    private ExecutorService threadPool;
    private DeferredResultRegistry resultRegistry;
    private DeferredResultRegistry<String> deferredResultRegistry;

    public UrlTextExtractorService(DeferredResultRegistry resultRegistry, DeferredResultRegistry<String> deferredResultRegistry) {
        this.resultRegistry = resultRegistry;
        this.deferredResultRegistry = deferredResultRegistry;
        tokenQueue = new LinkedBlockingDeque<>(10000);
        this.threadPool = new ThreadPoolExecutor(5, 5, 0, TimeUnit.MILLISECONDS, tokenQueue);
    }

    public void extractText(ExtractionRequest<Url> urlRequest) {
        this.threadPool.execute(() -> extractTextR(urlRequest));
    }

    public void extractTextR(ExtractionRequest<Url> urlReq) {
        Url url = urlReq.object;
        String retrievedText = null;
        try {
            Document doc = Jsoup.connect(url.getLongUrl()).get();
            retrievedText = doc.text();
        } catch (IOException ioException) {
            log.error("Could not fetch text for the webpage with uri {}", url.getLongUrl());
        } finally {
            retrievedText = StringUtil.isBlank(retrievedText) ? "Text not available for " + url.getLongUrl() : retrievedText;
        }

        log.info("Got text for {} as {}", url.getLongUrl(), retrievedText);
        if (deferredResultRegistry != null) deferredResultRegistry.updateResult(urlReq.getKey(), retrievedText);
    }

    public static class ExtractionRequest<T> {
        Long uniqueReqId;
        T object;

        public ExtractionRequest(Long uniqueReqId, T object) {
            this.uniqueReqId = uniqueReqId;
            this.object = object;
        }

        public String getKey() {
            return this.uniqueReqId + this.object.toString();
        }
    }

}
