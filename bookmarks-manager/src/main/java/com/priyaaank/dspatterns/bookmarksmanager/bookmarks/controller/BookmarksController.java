package com.priyaaank.dspatterns.bookmarksmanager.bookmarks.controller;

import com.priyaaank.dspatterns.bookmarksmanager.bookmarks.domain.Bookmark;
import com.priyaaank.dspatterns.bookmarksmanager.bookmarks.presenter.BookmarksPresenter;
import com.priyaaank.dspatterns.bookmarksmanager.bookmarks.presenter.NewBookmarkRequest;
import com.priyaaank.dspatterns.bookmarksmanager.bookmarks.service.EnrichBookmarksService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/bookmarks")
public class BookmarksController {

    private EnrichBookmarksService enrichBookmarksService;

    @Autowired
    public BookmarksController(EnrichBookmarksService enrichBookmarksService) {
        this.enrichBookmarksService = enrichBookmarksService;
    }

    @PostMapping("/new")
    public BookmarksPresenter addBookmark(@RequestBody NewBookmarkRequest bookmarkRequest) {
        Bookmark bookmark = this.enrichBookmarksService.enrichBookmark(bookmarkRequest.toDomain());
        return BookmarksPresenter.fromDomain(bookmark);
    }

}
