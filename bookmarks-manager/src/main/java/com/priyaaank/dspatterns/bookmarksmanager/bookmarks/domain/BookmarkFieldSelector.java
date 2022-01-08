package com.priyaaank.dspatterns.bookmarksmanager.bookmarks.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class BookmarkFieldSelector {

    public static final String SHORTENED_URL = "shortenedUrl";
    public static final String TITLE = "title";
    public static final String LONG_URL = "longUrl";
    public static final String TEXT = "text";
    public static final String TAGS = "tags";
    private final List<String> fields;

    public BookmarkFieldSelector(String commaDelimFields) {
        fields = commaDelimFields == null ?
                List.of(SHORTENED_URL, TITLE, LONG_URL, TEXT, TAGS) :
                List.of(commaDelimFields.split(","));
    }

    public String enrichShortUrl(Supplier<Bookmark> supplier) {
        return fields.contains(SHORTENED_URL) ? supplier.get().getShortenedUrl() : null;
    }

    public String enrichText(Supplier<Bookmark> supplier) {
        return fields.contains(TEXT) ? supplier.get().getText() : null;
    }

    public String enrichTitle(Supplier<Bookmark> supplier) {
        return fields.contains(TITLE) ? supplier.get().getTitle() : null;
    }

    public List<String> enrichTags(Function<Bookmark, Bookmark> func, Bookmark bookmark) {
        return fields.contains(TAGS) ? func.apply(bookmark).getTags() : new ArrayList<>();
    }
}
