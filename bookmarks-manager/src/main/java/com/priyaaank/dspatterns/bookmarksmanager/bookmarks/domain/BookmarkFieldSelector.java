package com.priyaaank.dspatterns.bookmarksmanager.bookmarks.domain;

import java.util.List;
import java.util.function.Supplier;

public class BookmarkFieldSelector {

    public static final String LONG_URL = "longUrl";
    public static final String TEXT = "text";
    private final List<String> fields;

    public BookmarkFieldSelector(String commaDelimFields) {
        fields = commaDelimFields == null ?
                List.of(LONG_URL, TEXT) :
                List.of(commaDelimFields.split(","));
    }

    public String enrichText(Supplier<Bookmark> supplier) {
        return fields.contains(TEXT) ? supplier.get().getText() : null;
    }
}
