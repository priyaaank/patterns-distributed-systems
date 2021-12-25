package com.priyaaank.dspatterns.bookmarksmanager.bookmarks.service;

import com.priyaaank.dspatterns.bookmarksmanager.bookmarks.domain.Bookmark;
import com.priyaaank.dspatterns.bookmarksmanager.bookmarks.presenter.BookmarkShorteningRequest;
import com.priyaaank.dspatterns.bookmarksmanager.bookmarks.presenter.BookmarkShorteningResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BookmarkShorteningService {

    private RestTemplate restTemplate;
    private String urlServiceHost;

    @Autowired
    public BookmarkShorteningService(RestTemplate restTemplate, @Value("${services.urlshortner.hostport}")String urlServiceHost) {
        this.restTemplate = restTemplate;
        this.urlServiceHost = urlServiceHost;
    }

    public Bookmark shorten(Bookmark bookmark) {
        String url = urlServiceHost+"/uri/shorten";
        BookmarkShorteningRequest requestBody = new BookmarkShorteningRequest(bookmark.getLongUrl());
        ResponseEntity<BookmarkShorteningResponse> shortendUrlResponse = this.restTemplate.postForEntity(url, requestBody, BookmarkShorteningResponse.class);

        return Bookmark.builder().bookmark(bookmark).shortenedUrl(shortendUrlResponse.getBody().getShortUri()).build();
    }

    public Bookmark fetchTitle(Bookmark bookmark) {
        String url = urlServiceHost+"/uri/title?longUrl=" + bookmark.getLongUrl();
        ResponseEntity<String> urlTitleResponse = this.restTemplate.getForEntity(url, String.class);

        return Bookmark.builder().bookmark(bookmark).title(urlTitleResponse.getBody()).build();
    }
}
