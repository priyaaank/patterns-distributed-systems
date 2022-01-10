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

    private BookmarkShorteningService bookmarkShorteningService;
    private BookmarkTitleResolver titleResolver;
    private BookmarkTextResolver textResolver;
    private final CircuitBreaker<Bookmark, String> textResolverCircuit;
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
        this.textResolverCircuit = new CircuitBreaker<>(isCircuitBreakerEnabled, textResolver::enqueue);
    }

    public Bookmark enrichBookmark(String fieldsRequested, Bookmark bookmark) {
        BookmarkFieldSelector fieldSelector = new BookmarkFieldSelector(fieldsRequested);
        String updatedShortUrl = fieldSelector.enrichShortUrl(() -> this.bookmarkShorteningService.shorten(bookmark));
        String updatedTitle = fieldSelector.enrichTitle(() -> this.titleResolver.fetchTitle(bookmark));
        String updatedText = fieldSelector.enrichText(() -> this.textResolver.fetchText(bookmark));
        List<String> updatedTags = fieldSelector.enrichTags((b) -> tagsResolverCircuit.check(b), bookmark);

        return Bookmark.builder()
                .shortenedUrl(updatedShortUrl)
                .title(updatedTitle)
                .text(updatedText)
                .tags(updatedTags)
                .longUrl(bookmark.getLongUrl())
                .build();
    }

    public String enqueueForTextExtraction(Bookmark bookmark) {
        return this.textResolverCircuit.check(bookmark);
    }

}
