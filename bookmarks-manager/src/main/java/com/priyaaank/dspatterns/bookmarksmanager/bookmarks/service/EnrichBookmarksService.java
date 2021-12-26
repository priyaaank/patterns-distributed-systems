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
    private BookmarkTagsResolver bookmarkTagsResolver;

    @Autowired
    public EnrichBookmarksService(BookmarkShorteningService bookmarkShorteningService,
                                  BookmarkTitleResolver titleResolver,
                                  BookmarkTagsResolver bookmarkTagsResolver) {
        this.bookmarkShorteningService = bookmarkShorteningService;
        this.titleResolver = titleResolver;
        this.bookmarkTagsResolver = bookmarkTagsResolver;
    }

    public Bookmark enrichBookmark(String fieldsRequested, Bookmark bookmark) {
        log.info("Shortening url for bookmark {}", bookmark.getLongUrl());
        BookmarkFieldSelector fieldSelector = new BookmarkFieldSelector(fieldsRequested);
        Bookmark updatedShortUrl = fieldSelector.enrichShortUrl(() -> this.bookmarkShorteningService.shorten(bookmark), bookmark);
        Bookmark updatedTitle = fieldSelector.enrichTitle(() -> this.titleResolver.fetchTitle(bookmark), bookmark);
        Bookmark updatedTags = fieldSelector.enrichTags(() -> this.bookmarkTagsResolver.fetchTags(bookmark), bookmark);

        return Bookmark
                .builder()
                .bookmark(bookmark)
                .shortenedUrl(updatedShortUrl.getShortenedUrl())
                .title(updatedTitle.getTitle())
                .tags(updatedTags.getTags())
                .build();
    }
}
