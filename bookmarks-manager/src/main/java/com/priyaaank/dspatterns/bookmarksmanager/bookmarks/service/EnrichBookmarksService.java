package com.priyaaank.dspatterns.bookmarksmanager.bookmarks.service;

import com.priyaaank.dspatterns.bookmarksmanager.bookmarks.domain.Bookmark;
import com.priyaaank.dspatterns.bookmarksmanager.bookmarks.domain.BookmarkFieldSelector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EnrichBookmarksService {

    private BookmarkShorteningService bookmarkShorteningService;
    private BookmarkTitleResolver titleResolver;

    @Autowired
    public EnrichBookmarksService(BookmarkShorteningService bookmarkShorteningService, BookmarkTitleResolver titleResolver) {
        this.bookmarkShorteningService = bookmarkShorteningService;
        this.titleResolver = titleResolver;
    }

    public Bookmark enrichBookmark(String fieldsRequested, Bookmark bookmark) {
        log.info("Shortening url for bookmark {}", bookmark.getLongUrl());
        BookmarkFieldSelector fieldSelector = new BookmarkFieldSelector(fieldsRequested);
        Bookmark updatedShortUrl = fieldSelector.enrichShortUrl(() -> this.bookmarkShorteningService.shorten(bookmark), bookmark);
        Bookmark updatedTitle = fieldSelector.enrichTitle(() -> this.titleResolver.fetchTitle(bookmark), bookmark);

        return Bookmark
                .builder()
                .bookmark(bookmark)
                .shortenedUrl(updatedShortUrl.getShortenedUrl())
                .title(updatedTitle.getTitle())
                .build();
    }
}
