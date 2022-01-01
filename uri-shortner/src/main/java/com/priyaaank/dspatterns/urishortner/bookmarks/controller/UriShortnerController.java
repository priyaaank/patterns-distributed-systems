package com.priyaaank.dspatterns.urishortner.bookmarks.controller;

import com.priyaaank.dspatterns.urishortner.bookmarks.domain.Url;
import com.priyaaank.dspatterns.urishortner.bookmarks.jobs.UrlDetailsPopulateBatchJob;
import com.priyaaank.dspatterns.urishortner.bookmarks.presenter.ShortUriResponse;
import com.priyaaank.dspatterns.urishortner.bookmarks.presenter.ShortenUriRequest;
import com.priyaaank.dspatterns.urishortner.bookmarks.service.DeferredResultRegistry;
import com.priyaaank.dspatterns.urishortner.bookmarks.service.UriShorteningService;
import com.priyaaank.dspatterns.urishortner.bookmarks.service.UrlTextExtractorService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/uri")
public class UriShortnerController {

    private UriShorteningService uriShorteningService;
    private UrlTextExtractorService urlTextExtractorService;
    private UrlDetailsPopulateBatchJob urlDetailsPopulateBatchJob;
    private DeferredResultRegistry<String> deferredResultRegistry;

    @Autowired
    public UriShortnerController(UriShorteningService uriShorteningService,
                                 UrlTextExtractorService urlTextExtractorService,
                                 UrlDetailsPopulateBatchJob urlDetailsPopulateBatchJob,
                                 DeferredResultRegistry<String> deferredResultRegistry) {
        this.uriShorteningService = uriShorteningService;
        this.urlTextExtractorService = urlTextExtractorService;
        this.urlDetailsPopulateBatchJob = urlDetailsPopulateBatchJob;
        this.deferredResultRegistry = deferredResultRegistry;
    }

    @PostMapping("/shorten")
    public ShortUriResponse shorten(@RequestBody ShortenUriRequest shortenUriRequest) {
        Url shortUri = uriShorteningService.generateShortUri(shortenUriRequest.toDomain());
        return ShortUriResponse.fromDomain(shortUri);
    }

    @GetMapping("/title")
    public String getTitle(@RequestParam String longUrl) {
        if (longUrl == null) return "No longUrl provided";

        String retrievedTitle = null;
        try {
            Document doc = Jsoup.connect(longUrl).get();
            retrievedTitle = doc.title();
        } catch (IOException ioException) {
            log.error("Could not fetch title for the webpage with uri {}", longUrl);
        } finally {
            retrievedTitle = StringUtil.isBlank(retrievedTitle) ? "Title not available for " + longUrl : retrievedTitle;
        }

        return retrievedTitle;
    }

    @GetMapping("/text")
    public DeferredResult<String> getText(@RequestParam String url) {
        DeferredResult<String> deferredResult = new DeferredResult<>();
        if (url == null) {
            deferredResult.setResult("No longUrl provided");
            return deferredResult;
        }

        UrlTextExtractorService.ExtractionRequest<Url> urlRequest = new UrlTextExtractorService.ExtractionRequest<>(System.currentTimeMillis(), new Url(url));
        urlTextExtractorService.extractText(urlRequest);
        deferredResultRegistry.addToRegistry(urlRequest.getKey(), deferredResult);
        log.info("Returning the result for url {}", url);
        return deferredResult;
    }

    @GetMapping("/trigger/batch")
    public String triggerBatch() throws IOException {
        urlDetailsPopulateBatchJob.triggerJob();
        return "Triggered successfully at " + System.currentTimeMillis();
    }
}
