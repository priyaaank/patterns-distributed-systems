package com.priyaaank.dspatterns.urishortner.bookmarks.controller;

import com.priyaaank.dspatterns.urishortner.bookmarks.domain.Uri;
import com.priyaaank.dspatterns.urishortner.bookmarks.presenter.ShortUriResponse;
import com.priyaaank.dspatterns.urishortner.bookmarks.presenter.ShortenUriRequest;
import com.priyaaank.dspatterns.urishortner.bookmarks.service.UriShorteningService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/uri")
public class UriShortnerController {

    private UriShorteningService uriShorteningService;

    @Autowired
    public UriShortnerController(UriShorteningService uriShorteningService) {
        this.uriShorteningService = uriShorteningService;
    }

    @PostMapping("/shorten")
    public ShortUriResponse shorten(@RequestBody ShortenUriRequest shortenUriRequest) {
        Uri shortUri = uriShorteningService.generateShortUri(shortenUriRequest.toDomain());
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
}
