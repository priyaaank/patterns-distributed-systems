package com.priyaaank.dspatterns.bookmarksmanager.bookmarks.service;

import com.priyaaank.dspatterns.bookmarksmanager.bookmarks.domain.Bookmark;
import com.priyaaank.dspatterns.bookmarksmanager.bookmarks.domain.BookmarkFieldSelector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EnrichBookmarksService {

    private BookmarkTextResolver textResolver;

    @Autowired
    public EnrichBookmarksService(BookmarkTextResolver textResolver) {
        this.textResolver = textResolver;
    }

    public Bookmark enrichBookmark(String fieldsRequested, Bookmark bookmark) {
        BookmarkFieldSelector fieldSelector = new BookmarkFieldSelector(fieldsRequested);
        String updatedText = fieldSelector.enrichText(() -> this.textResolver.fetchText(bookmark));

        return Bookmark.builder()
                .text(updatedText)
                .longUrl(bookmark.getLongUrl())
                .build();
    }
}
