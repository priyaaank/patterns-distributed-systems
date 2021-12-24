package com.priyaaank.dspatterns.urishortner.bookmarks.controller;

import com.priyaaank.dspatterns.urishortner.bookmarks.domain.Uri;
import com.priyaaank.dspatterns.urishortner.bookmarks.presenter.ShortUriResponse;
import com.priyaaank.dspatterns.urishortner.bookmarks.presenter.ShortenUriRequest;
import com.priyaaank.dspatterns.urishortner.bookmarks.service.UriShorteningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
