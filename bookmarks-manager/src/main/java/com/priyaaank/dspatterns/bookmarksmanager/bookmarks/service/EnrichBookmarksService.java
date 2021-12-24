package com.priyaaank.dspatterns.bookmarksmanager.bookmarks.service;

import com.priyaaank.dspatterns.bookmarksmanager.bookmarks.domain.Bookmark;
import org.springframework.stereotype.Service;

@Service
public class EnrichBookmarksService {

    public Bookmark enrichBookmark(Bookmark bookmark) {
        return bookmark;
    }
}
