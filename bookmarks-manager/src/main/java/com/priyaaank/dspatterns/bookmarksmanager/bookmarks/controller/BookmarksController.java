package com.priyaaank.dspatterns.bookmarksmanager.bookmarks.controller;

import com.priyaaank.dspatterns.bookmarksmanager.bookmarks.domain.Bookmark;
import com.priyaaank.dspatterns.bookmarksmanager.bookmarks.presenter.BookmarksPresenter;
import com.priyaaank.dspatterns.bookmarksmanager.bookmarks.service.EnrichBookmarksService;
import com.priyaaank.dspatterns.bookmarksmanager.circuitbreaker.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/bookmark")
public class BookmarksController {

    private CircuitBreaker<String, Bookmark, Bookmark> enrichCircuit;

    @Autowired
    public BookmarksController(@Value("${circuitbreaker.enabled}") Boolean isCircuitBreakerEnabled ,
                               EnrichBookmarksService enrichBookmarksService) {
        this.enrichCircuit = new CircuitBreaker<>(isCircuitBreakerEnabled, enrichBookmarksService::enrichBookmark);
    }

    @GetMapping("/enrich")
    public BookmarksPresenter enrichBookmark(@RequestParam String fieldsRequested, @RequestParam String url) {
        Bookmark bookmark = enrichCircuit.check(fieldsRequested, new Bookmark(url));
        log.debug("Bookmark {}", bookmark);
        return BookmarksPresenter.fromDomain(bookmark);
    }

}
