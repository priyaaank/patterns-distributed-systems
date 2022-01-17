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
    public EnrichBookmarksService(BookmarkShorteningService bookmarkShorteningService,
                                  BookmarkTitleResolver titleResolver) {
        this.bookmarkShorteningService = bookmarkShorteningService;
        this.titleResolver = titleResolver;
    }

    public Bookmark enrichBookmark(String fieldsRequested, Bookmark bookmark) {
        BookmarkFieldSelector fieldSelector = new BookmarkFieldSelector(fieldsRequested);

        //Call a remote service to fetch short url if field has been requested
        String updatedShortUrl = fieldSelector.enrichShortUrlIfPresent(() -> this.bookmarkShorteningService.shorten(bookmark));

        //Call a remote service to fetch title if field has been requested
        String updatedTitle = fieldSelector.enrichTitleIfPresent(() -> this.titleResolver.fetchTitle(bookmark));

        //Reconstruct the url and send back
        return Bookmark.builder()
                .shortenedUrl(updatedShortUrl)
                .title(updatedTitle)
                .longUrl(bookmark.getLongUrl())
                .build();
    }
}
