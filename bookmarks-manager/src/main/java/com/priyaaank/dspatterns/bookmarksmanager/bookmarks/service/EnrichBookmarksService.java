package com.priyaaank.dspatterns.bookmarksmanager.bookmarks.service;

import com.priyaaank.dspatterns.bookmarksmanager.bookmarks.domain.Bookmark;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EnrichBookmarksService {

    private BookmarkShorteningService bookmarkShorteningService;

    @Autowired
    public EnrichBookmarksService(BookmarkShorteningService bookmarkShorteningService) {
        this.bookmarkShorteningService = bookmarkShorteningService;
    }

    public Bookmark enrichBookmark(Bookmark bookmark) {
        log.info("Shortening url for bookmark {}", bookmark.getLongUrl());
        Bookmark updatedShortUrl = this.bookmarkShorteningService.shorten(bookmark);
        Bookmark updatedTitle = this.bookmarkShorteningService.fetchTitle(bookmark);

        return Bookmark
                .builder()
                .bookmark(bookmark)
                .shortenedUrl(updatedShortUrl.getShortenedUrl())
                .title(updatedTitle.getTitle())
                .build();
    }
}
