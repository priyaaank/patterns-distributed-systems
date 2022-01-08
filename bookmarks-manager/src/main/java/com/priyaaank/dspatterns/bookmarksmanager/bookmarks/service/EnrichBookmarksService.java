package com.priyaaank.dspatterns.bookmarksmanager.bookmarks.service;

import com.priyaaank.dspatterns.bookmarksmanager.bookmarks.domain.Bookmark;
import com.priyaaank.dspatterns.bookmarksmanager.bookmarks.domain.BookmarkFieldSelector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class EnrichBookmarksService {

    private BookmarkTagsResolver bookmarkTagsResolver;

    @Autowired
    public EnrichBookmarksService(BookmarkTagsResolver bookmarkTagsResolver) {
        this.bookmarkTagsResolver = bookmarkTagsResolver;
    }

    public Bookmark enrichBookmark(String fieldsRequested, Bookmark bookmark) {
        BookmarkFieldSelector fieldSelector = new BookmarkFieldSelector(fieldsRequested);
        List<String> updatedTags = fieldSelector.enrichTags((b) -> bookmarkTagsResolver.fetchTags(b), bookmark);

        return Bookmark.builder()
                .tags(updatedTags)
                .longUrl(bookmark.getLongUrl())
                .build();
    }
}
