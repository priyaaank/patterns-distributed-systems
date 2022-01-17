package com.priyaaank.dspatterns.bookmarksmanager.bookmarks.domain;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class BookmarkFieldSelector {

    public static final String SHORTENED_URL = "shortenedUrl";
    public static final String TITLE = "title";
    private final List<String> fields;

    public BookmarkFieldSelector(String commaDelimitedFields) {
        fields = commaDelimitedFields == null ?
                Arrays.asList("shortenedUrl", "title") :
                Arrays.asList(commaDelimitedFields.split(","));
    }

    public String enrichShortUrlIfPresent(Supplier<Bookmark> supplier) {
        return fields.contains(SHORTENED_URL) ? supplier.get().getShortenedUrl() : null;
    }

    public String enrichTitleIfPresent(Supplier<Bookmark> supplier) {
        return fields.contains(TITLE) ? supplier.get().getTitle() : null;
    }

}
