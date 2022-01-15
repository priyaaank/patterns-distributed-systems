package com.priyaaank.dspatterns.bookmarksmanager.bookmarks.controller;

import com.priyaaank.dspatterns.bookmarksmanager.bookmarks.domain.Bookmark;
import com.priyaaank.dspatterns.bookmarksmanager.bookmarks.presenter.BookmarksPresenter;
import com.priyaaank.dspatterns.bookmarksmanager.bookmarks.service.EnrichBookmarksService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/bookmark")
public class BookmarksController {

    private EnrichBookmarksService enrichBookmarksService;

    @Autowired
    public BookmarksController(EnrichBookmarksService enrichBookmarksService) {
        this.enrichBookmarksService = enrichBookmarksService;
    }

    @GetMapping("/enrich")
    public BookmarksPresenter enrichBookmark(@RequestParam String fieldsRequested, @RequestParam String url) {
        Bookmark bookmark = enrichBookmarksService.enrichBookmark(fieldsRequested, new Bookmark(url));
        log.debug("Bookmark {}", bookmark);
        return BookmarksPresenter.fromDomain(bookmark);
    }

    @PostMapping("/enqueue")
    public String enqueueBookmark(@RequestParam String url) {
        return enrichBookmarksService.enqueueForTextExtraction(new Bookmark(url));
    }

}
