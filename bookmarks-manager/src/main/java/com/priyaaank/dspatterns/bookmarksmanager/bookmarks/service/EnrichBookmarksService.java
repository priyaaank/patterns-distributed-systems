package com.priyaaank.dspatterns.bookmarksmanager.bookmarks.service;

import com.priyaaank.dspatterns.bookmarksmanager.bookmarks.domain.Bookmark;
import com.priyaaank.dspatterns.bookmarksmanager.bookmarks.domain.BookmarkFieldSelector;
import com.priyaaank.dspatterns.bookmarksmanager.circuitbreaker.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class EnrichBookmarksService {

    private CircuitBreaker<Bookmark, Bookmark> tagsResolverCircuit;

    @Autowired
    public EnrichBookmarksService(BookmarkTagsResolver bookmarkTagsResolver,
                                  @Value("${circuitbreaker.enabled}") Boolean isCircuitBreakerEnabled,
                                  @Value("${circuitbreaker.failOver.enabled}") Boolean isFailOverEnabled) {
        this.tagsResolverCircuit = isFailOverEnabled ?
                new CircuitBreaker<>(isCircuitBreakerEnabled, bookmarkTagsResolver::fetchTags, bookmarkTagsResolver::generateTagsLocally) :
                new CircuitBreaker<>(isCircuitBreakerEnabled, bookmarkTagsResolver::fetchTags);
    }

    public Bookmark enrichBookmark(String fieldsRequested, Bookmark bookmark) {
        BookmarkFieldSelector fieldSelector = new BookmarkFieldSelector(fieldsRequested);
        List<String> updatedTags = fieldSelector.enrichTags((b) -> tagsResolverCircuit.check(b), bookmark);

        return Bookmark.builder()
                .tags(updatedTags)
                .longUrl(bookmark.getLongUrl())
                .build();
    }

}