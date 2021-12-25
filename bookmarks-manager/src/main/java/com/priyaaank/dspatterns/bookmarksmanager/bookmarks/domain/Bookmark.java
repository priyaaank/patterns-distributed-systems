package com.priyaaank.dspatterns.bookmarksmanager.bookmarks.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.binary.Hex;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class Bookmark {

    private String longUrl;
    private String shortenedUrl;
    private String summary;
    private String title;
    private List<String> tags = new ArrayList<>();

    public Bookmark(String longUrl) {
        this.longUrl = longUrl;
    }

    public String getId() {
        return Hex.encodeHexString(this.longUrl.getBytes(StandardCharsets.UTF_8));
    }

    public static BookmarkBuilder builder() {
        return new BookmarkBuilder();
    }

    @Override
    public String toString() {
        return "Bookmark = (" +
                "title=" + title +
                "summary=" + summary +
                "tags=" + String.join(",", tags) +
                "longUrl=" + longUrl +
                "shortenedUrl=" + shortenedUrl;
    }

    public static class BookmarkBuilder {

        private Bookmark bookmark = new Bookmark();

        public BookmarkBuilder bookmark(Bookmark bookmark) {
            this.longUrl(bookmark.getLongUrl());
            this.shortenedUrl(bookmark.getShortenedUrl());
            this.tags(bookmark.getTags());
            this.title(bookmark.getTitle());
            this.summary(bookmark.getSummary());
            return this;
        }

        public BookmarkBuilder longUrl(String longUrl) {
            bookmark.longUrl = longUrl;
            return this;
        }

        public BookmarkBuilder shortenedUrl(String shortenedUrl) {
            bookmark.shortenedUrl = shortenedUrl;
            return this;
        }

        public BookmarkBuilder tags(List<String> tags) {
            bookmark.tags = tags;
            return this;
        }

        public BookmarkBuilder title(String title) {
            bookmark.title = title;
            return this;
        }

        public BookmarkBuilder summary(String summary) {
            bookmark.summary = summary;
            return this;
        }

        public Bookmark build() {
            return bookmark;
        }

    }
}
