package com.priyaaank.dspatterns.bookmarksmanager.bookmarks.presenter;

import com.priyaaank.dspatterns.bookmarksmanager.bookmarks.domain.Bookmark;
import lombok.Getter;

@Getter
public class NewBookmarkRequest {

    private String url;
    private String name;

    public Bookmark toDomain() {
        return new Bookmark(url, name);
    }
}
