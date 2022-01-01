package com.priyaaank.dspatterns.urishortner.bookmarks.jobs;

import com.priyaaank.dspatterns.urishortner.bookmarks.domain.Url;
import com.priyaaank.dspatterns.urishortner.bookmarks.service.DeferredResultRegistry;
import com.priyaaank.dspatterns.urishortner.bookmarks.service.UrlTextExtractorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
@Service
public class UrlDetailsPopulateBatchJob {

    private String batchFileName;
    private Boolean rateControlledExecution;
    private Boolean batchMode;
    private UrlTextExtractorService enrichBookmarksService;
    private ResourceLoader resourceLoader;
    private RestTemplate restTemplate;
    private DeferredResultRegistry<String> deferredResultRegistry;

    @Autowired
    public UrlDetailsPopulateBatchJob(@Value("${batchFilePath}") String batchFileName,
                                      @Value("${extract.text.rateControl}") Boolean rateControlledExecution,
                                      @Value("${extract.text.mode.batch}") Boolean batchMode,
                                      UrlTextExtractorService urlTextExtractorService,
                                      ResourceLoader resourceLoader) {
        this.batchFileName = batchFileName;
        this.rateControlledExecution = rateControlledExecution;
        this.batchMode = batchMode;
        this.enrichBookmarksService = urlTextExtractorService;
        this.resourceLoader = resourceLoader;
        this.restTemplate = new RestTemplate();
    }

    public void triggerJob() throws IOException {
        String line;
        log.info("Loading {}", batchFileName);
        Resource resource = this.resourceLoader.getResource("classpath:" + this.batchFileName);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
        while ((line = bufferedReader.readLine()) != null) {
            if (this.batchMode) {
                extractWithLocalCall(line);
            } else {
                extractWithHttpCall(line);
            }
        }
        log.info("I have queued up all results!");
    }

    private void extractWithHttpCall(String url) {
        String textUrl = "http://bookmarksmanager:8080/bookmark/enrich?url=" + url + "&fieldsRequested=text";
        ResponseEntity<String> textResponse = restTemplate.getForEntity(textUrl, String.class);
        log.info("Response for {} was {}", url, textResponse.getBody());
    }

    private void extractWithLocalCall(String line) {
        this.enrichBookmarksService.extractText(new UrlTextExtractorService.ExtractionRequest<>(System.currentTimeMillis(), new Url(line)));
    }

}
