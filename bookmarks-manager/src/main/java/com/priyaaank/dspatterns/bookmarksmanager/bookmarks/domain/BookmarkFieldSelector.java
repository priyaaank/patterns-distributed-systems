package com.priyaaank.dspatterns.bookmarksmanager.bookmarks.domain;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class BookmarkFieldSelector {

    private final List<String> fields;

    public BookmarkFieldSelector(String fieldJoinedByComma) {
        fields = Arrays.asList(fieldJoinedByComma.split(","));
    }

    public Bookmark enrichShortUrl(Supplier<Bookmark> supplier, Bookmark bookmark) {
        if (fields.contains("shortenedUrl"))
            return supplier.get();
        return bookmark;
    }

    public Bookmark enrichTitle(Supplier<Bookmark> supplier, Bookmark bookmark) {
        if (fields.contains("title"))
            return supplier.get();
        return bookmark;
    }
}
