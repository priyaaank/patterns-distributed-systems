package com.priyaaank.dspatterns.bookmarksmanager.bookmarks.service;

import com.priyaaank.dspatterns.bookmarksmanager.bookmarks.domain.Bookmark;
import com.priyaaank.dspatterns.bookmarksmanager.bookmarks.domain.BookmarkFieldSelector;
import com.priyaaank.dspatterns.bookmarksmanager.circuitbreaker.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EnrichBookmarksService {

    private BookmarkShorteningService bookmarkShorteningService;
    private BookmarkTitleResolver titleResolver;
    private BookmarkTextResolver textResolver;
    private CircuitBreaker<Bookmark, Bookmark> tagsResolverCircuit;

    @Autowired
    public EnrichBookmarksService(BookmarkShorteningService bookmarkShorteningService,
                                  BookmarkTitleResolver titleResolver,
                                  BookmarkTagsResolver bookmarkTagsResolver,
                                  BookmarkTextResolver textResolver,
                                  @Value("${circuitbreaker.enabled}") Boolean isCircuitBreakerEnabled,
                                  @Value("${circuitbreaker.failOver.enabled}") Boolean isFailOverEnabled) {
        this.bookmarkShorteningService = bookmarkShorteningService;
        this.titleResolver = titleResolver;
        this.textResolver = textResolver;
        this.tagsResolverCircuit = isFailOverEnabled ?
                new CircuitBreaker<>(isCircuitBreakerEnabled, bookmarkTagsResolver::fetchTags, bookmarkTagsResolver::generateTagsLocally) :
                new CircuitBreaker<>(isCircuitBreakerEnabled, bookmarkTagsResolver::fetchTags);
    }

    public Bookmark enrichBookmark(String fieldsRequested, Bookmark bookmark) {
        BookmarkFieldSelector fieldSelector = new BookmarkFieldSelector(fieldsRequested);
        Bookmark updatedShortUrl = fieldSelector.enrichShortUrl(() -> this.bookmarkShorteningService.shorten(bookmark), bookmark);
        Bookmark updatedTitle = fieldSelector.enrichTitle(() -> this.titleResolver.fetchTitle(bookmark), bookmark);
        Bookmark updatedText = fieldSelector.enrichText(() -> this.textResolver.fetchText(bookmark), bookmark);
        Bookmark updatedTags = fieldSelector.enrichTags((b) -> tagsResolverCircuit.check(b), bookmark);

        return Bookmark
                .builder()
                .bookmark(bookmark)
                .shortenedUrl(updatedShortUrl.getShortenedUrl())
                .title(updatedTitle.getTitle())
                .text(updatedText.getText())
                .tags(updatedTags.getTags())
                .build();
    }
}
