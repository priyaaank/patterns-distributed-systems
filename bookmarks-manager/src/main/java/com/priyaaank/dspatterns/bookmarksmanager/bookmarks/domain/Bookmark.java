package com.priyaaank.dspatterns.bookmarksmanager.bookmarks.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.binary.Hex;

import java.nio.charset.StandardCharsets;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Bookmark {

    private String longUrl;
    private String shortenedUrl;
    private String title;

    public Bookmark(String longUrl) {
        this.longUrl = longUrl;
    }

    public String getId() {
        return Hex.encodeHexString(this.longUrl.getBytes(StandardCharsets.UTF_8));
    }

    public BookmarkBuilder cloneBuilder() {
        return builder().shortenedUrl(shortenedUrl).longUrl(longUrl).title(title);
    }

    @Override
    public String toString() {
        return "Bookmark = (" +
                "title=" + title +
                "longUrl=" + longUrl +
                "shortenedUrl=" + shortenedUrl;
    }

}
