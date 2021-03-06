package com.priyaaank.dspatterns.bookmarksmanager.bookmarks.presenter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.priyaaank.dspatterns.bookmarksmanager.bookmarks.domain.Bookmark;
import lombok.AllArgsConstructor;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
public class BookmarksPresenter {

    @JsonProperty("id")
    private String id;

    @JsonProperty("longUrl")
    private String longUrl;

    @JsonProperty("shortUrl")
    private String shortUrl;

    @JsonProperty("summary")
    private String summary;

    @JsonProperty("tags")
    private List<String> tags;

    @JsonProperty("title")
    private String title;

    public static BookmarksPresenter fromDomain(Bookmark bookmark) {
        return new BookmarksPresenter(bookmark.getId(), bookmark.getLongUrl(), bookmark.getShortenedUrl(),
                bookmark.getText(), bookmark.getTags(), bookmark.getTitle());
    }

}
