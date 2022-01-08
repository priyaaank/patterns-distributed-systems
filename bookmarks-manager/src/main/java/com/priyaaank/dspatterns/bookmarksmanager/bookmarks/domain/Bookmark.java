package com.priyaaank.dspatterns.bookmarksmanager.bookmarks.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.binary.Hex;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Bookmark {

    private String longUrl;
    private List<String> tags = new ArrayList<>();

    public Bookmark(String longUrl) {
        this.longUrl = longUrl;
    }

    public String getId() {
        return Hex.encodeHexString(this.longUrl.getBytes(StandardCharsets.UTF_8));
    }

    public BookmarkBuilder cloneBuilder() {
        return builder().longUrl(longUrl).tags(tags);
    }

    @Override
    public String toString() {
        return "Bookmark = (" +
                "tags=" + String.join(",", tags) +
                "longUrl=" + longUrl + ")";
    }

}
