package com.priyaaank.dspatterns.bookmarksmanager.bookmarks.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class BookmarkFieldSelector {

    public static final String LONG_URL = "longUrl";
    public static final String TAGS = "tags";
    private final List<String> fields;

    public BookmarkFieldSelector(String commaDelimFields) {
        fields = commaDelimFields == null ? List.of(LONG_URL, TAGS) : List.of(commaDelimFields.split(","));
    }

    public List<String> enrichTags(Function<Bookmark, Bookmark> func, Bookmark bookmark) {
        return fields.contains(TAGS) ? func.apply(bookmark).getTags() : new ArrayList<>();
    }
}
