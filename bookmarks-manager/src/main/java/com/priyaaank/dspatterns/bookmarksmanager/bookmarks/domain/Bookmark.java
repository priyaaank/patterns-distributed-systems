package com.priyaaank.dspatterns.bookmarksmanager.bookmarks.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@AllArgsConstructor
@Getter
public class Bookmark {

    private String longUrl;
    private String shortenedUrl;
    private String name;
    private String summary;
    private List<String> tags = new ArrayList<>();

    public Bookmark(String longUrl, String name) {
        this.longUrl = longUrl;
        this.name = name;
    }

    public String getId() {
        return Base64.getEncoder().encodeToString(this.longUrl.getBytes(StandardCharsets.UTF_8));
    }
}
