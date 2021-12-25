package com.priyaaank.dspatterns.bookmarksmanager.bookmarks.controller;

import com.priyaaank.dspatterns.bookmarksmanager.bookmarks.domain.Bookmark;
import com.priyaaank.dspatterns.bookmarksmanager.bookmarks.presenter.BookmarksPresenter;
import com.priyaaank.dspatterns.bookmarksmanager.bookmarks.presenter.NewBookmarkRequest;
import com.priyaaank.dspatterns.bookmarksmanager.bookmarks.service.EnrichBookmarksService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@Slf4j
@RestController
@RequestMapping("/bookmarks")
public class BookmarksController {

    private EnrichBookmarksService enrichBookmarksService;
    private RestTemplate restTemplate;

    @Autowired
    public BookmarksController(EnrichBookmarksService enrichBookmarksService, RestTemplate restTemplate) {
        this.enrichBookmarksService = enrichBookmarksService;
        this.restTemplate = restTemplate;
    }

    @PostMapping("/new")
    public BookmarksPresenter addBookmark(@RequestBody NewBookmarkRequest bookmarkRequest) {
        Bookmark bookmark = this.enrichBookmarksService.enrichBookmark(bookmarkRequest.toDomain());
        log.info("Bookmark {}", bookmark);
        return BookmarksPresenter.fromDomain(bookmark);
    }

}
