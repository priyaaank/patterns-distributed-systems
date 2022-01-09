package com.priyaaank.dspatterns.urishortner.bookmarks.jobs;

import com.priyaaank.dspatterns.urishortner.bookmarks.domain.Url;
import com.priyaaank.dspatterns.urishortner.bookmarks.service.UrlTextExtractorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
@Service
public class UrlDetailsPopulateBatchJob {

    private String batchFileName;
    private UrlTextExtractorService enrichBookmarksService;
    private ResourceLoader resourceLoader;

    @Autowired
    public UrlDetailsPopulateBatchJob(@Value("${batchFilePath}") String batchFileName,
                                      UrlTextExtractorService urlTextExtractorService,
                                      ResourceLoader resourceLoader) {
        this.batchFileName = batchFileName;
        this.enrichBookmarksService = urlTextExtractorService;
        this.resourceLoader = resourceLoader;
    }

    public void triggerJob() throws IOException {
        String line;
        log.info("Loading {}", batchFileName);
        Resource resource = this.resourceLoader.getResource("classpath:" + this.batchFileName);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
        while ((line = bufferedReader.readLine()) != null) {
            enqueueRequest(line);
        }
        log.info("I have queued up all results!");
    }

    private void enqueueRequest(String line) {
        enqueueLocalCall(line);
    }

    private void enqueueLocalCall(String line) {
        UrlTextExtractorService.ExtractionRequest<Url> urlRequest = new UrlTextExtractorService.ExtractionRequest<>(System.currentTimeMillis(), new Url(line));
        this.enrichBookmarksService.extractText(urlRequest);
    }

}
